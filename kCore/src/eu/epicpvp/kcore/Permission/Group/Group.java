package eu.epicpvp.kcore.Permission.Group;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.spigotmc.AsyncCatcher;

import dev.wolveringer.bukkit.permissions.GroupTyp;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import eu.epicpvp.kcore.Permission.Permission;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.Events.GroupLoadedEvent;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class Group {

	private String prefix = "undefined";
	@Getter
	private String name = "undefined";
	private ArrayList<Permission> perms = new ArrayList<>();
	private PermissionManager manager;
	@Getter
	private int importance;
	@Getter
	private boolean loaded = false;

	public Group(PermissionManager manager, String name) {
		this.name = name;
		this.manager = manager;
		init();
	}

	private synchronized void init() {
		if (isLoaded())
			return;
		try {
			DataBuffer response = manager.getHandler().sendMessage(createPlayer(), new DataBuffer().writeByte(1).writeString(name)).getSync(); //Action=1 Getgroup informatin
			new NullPointerException().printStackTrace();
			try{
				AsyncCatcher.catchOp("text");
				System.out.println("§cSync");
			}catch (Exception e){
				System.out.println("§aAsnyc");
			}
			if (response == null) {
				System.out.println("Cant load group " + name + " (Response == null)");
			}
			int length = response.readInt();
			if (length == -1) {
				System.out.println("Group loading error: " + response.readString());
				return;
			}
			perms.clear();
			for (int i = 0; i < length; i++) {
				String perm = response.readString();
				GroupTyp typ = GroupTyp.values()[response.readByte()];

				if (typ == GroupTyp.ALL || typ == manager.getType()) {
					perms.add(new Permission(perm, typ));
				}
			}
			prefix = response.readString();
			importance = response.readInt();
			Bukkit.getPluginManager().callEvent(new GroupLoadedEvent(manager, this));
		} catch (Exception e) {
			if (e.getMessage().toLowerCase().contains("time")) {
				System.out.println("Timeout while loading group " + name + ".");
				e.printStackTrace();
				return;
			}
			e.printStackTrace();
		}
		loaded = true;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	private Player createPlayer() {
		return UtilServer.getPlayers().isEmpty() ? null : UtilServer.getPlayers().iterator().next();
	}

	public boolean hasPermission(String permission) {
		return hasPermission(permission, GroupTyp.ALL);
	}

	public boolean hasPermission(String permission, GroupTyp type) {
		for (Permission p : new ArrayList<>(perms))
			if ((type == GroupTyp.ALL || p.getGroup() == type) && p.acceptPermission(permission))
				return true;
		return false;
	}

	public void reload() {
		loaded = false;
		init();
	}

	public ArrayList<Permission> getPermissions() {
		return perms;
	}

	public void load() {
		init();
	}
}
