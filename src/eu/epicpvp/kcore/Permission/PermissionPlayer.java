package eu.epicpvp.kcore.Permission;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import eu.epicpvp.datenserver.definitions.dataserver.protocoll.DataBuffer;
import eu.epicpvp.datenserver.definitions.permissions.GroupTyp;
import eu.epicpvp.kcore.Permission.Group.Group;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;
import lombok.Setter;

public class PermissionPlayer {
	@Getter
	private int playerId;
	@Getter
	private ArrayList<Permission> permissions = new ArrayList<>();
	private ArrayList<Group> groups = new ArrayList<>();
	@Getter
	private PermissionAttachment permissionAttachment;
	private PermissionManager manager;
	@Getter
	private Player player;
	@Getter
	@Setter
	private boolean prefix = true;

	private boolean loaded;

	public ArrayList<Group> getGroups() {
		if (groups.size() == 0) {
			if (manager.getGroup("default") == null)
				manager.loadGroup("default");
			return new ArrayList<>(Arrays.asList(manager.getGroup("default")));
		}
		return groups;
	}

	public PermissionPlayer(Player player, PermissionManager manager, int playerId) {
		this.player = player;
		this.permissionAttachment = player.addAttachment(manager.getInstance());
		this.manager = manager;
		this.playerId = playerId;
		loadPermissions();
	}

	private synchronized void loadPermissions() {
		if(loaded)
			return;
		long start = System.currentTimeMillis();
		System.out.println("[PermissionManager]: Requesting player Permissions");
		UtilPlayer.getPermissionList(this.permissionAttachment).clear();
		DataBuffer buffer = manager.handler.sendMessage(player, new DataBuffer().writeByte(0).writeInt(playerId)).getSync(); // Action: 0 (Get-Perms) sync will let sleep the minecraft server?
		if (buffer == null) {
			System.out.println("[PermissionManager]: Response == null");
			player.kickPlayer("§cError while loading permissions. (Message: 'response == null')");
			return;
		}
		int length = buffer.readInt();
		if (length == -1) { // Error
			String message = buffer.readString();
			System.out.println("[PermissionManager]: Having an error: " + message);
			player.kickPlayer("§cError while loading permissions. (Unexpected state (" + length + ") -> Message: " + message + ")");
			return;
		}
		ArrayList<String> sgroups = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			sgroups.add(buffer.readString());
		}

		length = buffer.readInt();
		for (int i = 0; i < length; i++) {
			String permission = buffer.readString();
			GroupTyp typ = GroupTyp.values()[buffer.readByte()];

			if (typ == GroupTyp.ALL || (manager.getType() == null) || typ == manager.getType()) {
				Permission perm = new Permission(permission, typ);
				permissions.add(perm);
				player.addAttachment(manager.getInstance(), perm.getRawPermission(), !perm.isNegative());
			}
		}
		System.out.println("[PermissionManager]: Permissions geladen. Lade gruppen");

		for (String s : sgroups) {
			Group g = manager.getGroup(s);
			if (g == null)
				g = manager.loadGroup(s);
			groups.add(g);
		}

		for (Group g : getGroups()) {
			for (Permission perm : g.getPermissions()) {
				player.addAttachment(manager.getInstance(), perm.getRawPermission(), !perm.isNegative());
			}
		}

		player.recalculatePermissions();
		long end = System.currentTimeMillis();
		System.out.println("[PermissionManager]: Player geladen ("+(end-start)+"ms)");
		loaded = true;

		if (hasPermission("epicpvp.op")) {
			player.setOp(true);
		} else {
			if (player.isOp()) {
				player.setOp(false);
			}
		}
	}

	public void reloadPermissions() {
		loaded = false;
		loadPermissions();
	}

	public void addPermission(String permission) {
		addPermission(permission, GroupTyp.ALL);
	}

	public void addPermission(String permission, GroupTyp type) {
		loadPermissions();
		Permission perm = new Permission(permission, type);
		if (!permissions.contains(perm)) {
			permissions.add(perm);
			player.addAttachment(manager.getInstance(), perm.getRawPermission(), true);
			manager.handler.sendMessage(player, new DataBuffer().writeByte(2).writeInt(playerId).writeString(permission).writeByte(type.ordinal()));
			player.recalculatePermissions();
		}
	}

	public boolean hasPermission(String permission) {
		return hasPermission(permission, GroupTyp.ALL);
	}

	public boolean hasPermission(String permission, GroupTyp type) {
		loadPermissions();
		for (Permission p : new ArrayList<>(permissions))
			if ((type == GroupTyp.ALL || p.getGroup() == type) && p.acceptPermission(permission))
				return true;
		for (Group group : groups)
			if (group.hasPermission(permission, type) || group.hasPermission(permission, GroupTyp.ALL))
				return true;
		return false;
	}
}
