package eu.epicpvp.kcore.Permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import dev.wolveringer.dataserver.protocoll.DataBuffer;
import eu.epicpvp.kcore.Permission.Group.Group;
import eu.epicpvp.kcore.Permission.Group.GroupTyp;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;

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

	public ArrayList<Group> getGroups(){
		if(groups.size() == 0){
			if(manager.getGroup("default") == null)
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

	private void loadPermissions() {
		System.out.println("[PermissionManager]: Requesting player Permissions");
		UtilPlayer.getPermissionList(this.permissionAttachment).clear();
		DataBuffer buffer = manager.handler.sendMessage(player, new DataBuffer().writeByte(0).writeInt(playerId)).getSync(); // Action: 0 (Get-Perms) sync will let sleep the minecraft server?
		if (buffer == null) {
			System.out.println("[PermissionManager]: Response == null");
			return;
		}
		int length = buffer.readInt();
		if (length == -1) { // Error
			System.out.println("[PermissionManager]: Having an error: " + buffer.readString());
			return;
		}
		ArrayList<String> sgroups = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			sgroups.add(buffer.readString());
		}

		length = buffer.readInt();
		for (int i = 0; i < length; i++) {
			Permission perm = new Permission(buffer.readString(), GroupTyp.values()[buffer.readByte()]);
			permissions.add(perm);
			player.addAttachment(manager.getInstance(), perm.getRawPermission(), !perm.isNegative());
			//this.permissionAttachment.setPermission(perm.a, true);
		}
		System.out.println("[PermissionManager]: Permissions geladen. Lade gruppen");

		for (String s : sgroups) {
			Group g = manager.getGroup(s);
			if (g == null)
				g = manager.loadGroup(s);
			groups.add(g);
		}
		
		for(Group g : getGroups()){
			for(Permission perm : g.getPermissions()){
				//this.permissionAttachment.setPermission(perm.getBukkitPermission(), true);
				player.addAttachment(manager.getInstance(),perm.getRawPermission(),!perm.isNegative());
			}
		}
		
		if(player.hasPermission("*")||player.hasPermission("epicpvp.op")||player.hasPermission("epicpvp.*")){
			player.setOp(true);
		}else{
			if(player.isOp()){
				player.setOp(false);
			}
		}
		
		player.recalculatePermissions();
		System.out.println("[PermissionManager]: Player geladen");
		manager.getUser().put(playerId, this);
	}

	public void reloadPermissions() {
		loadPermissions();
	}

	public void addPermission(String permission) {
		addPermission(permission, GroupTyp.ALL);
	}

	public void addPermission(String permission, GroupTyp type) {
		Permission perm = new Permission(permission, type);
		if (!permissions.contains(perm)) {
			permissions.add(perm);
			//this.permissionAttachment.setPermission(perm.getBukkitPermission(), true);
			player.addAttachment(manager.getInstance(),perm.getRawPermission(),true);
//			manager.handler.sendMessage(player, new DataBuffer().writeByte(2).writeInt(playerId).writeByte(type.ordinal()));
			player.recalculatePermissions();
		}
	}

	public boolean hasPermission(String permission) {
		return hasPermission(permission, GroupTyp.ALL);
	}

	public boolean hasPermission(String permission, GroupTyp type) {
		for (Permission p : new ArrayList<>(permissions))
			if ((type == GroupTyp.ALL || p.getGroup() == type) && p.acceptPermission(permission))
				return true;
		for (Group group : groups)
			if (group.hasPermission(permission, type))
				return true;
		return false;
	}
}
