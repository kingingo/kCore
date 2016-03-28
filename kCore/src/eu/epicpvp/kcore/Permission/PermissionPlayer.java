package eu.epicpvp.kcore.Permission;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import dev.wolveringer.dataserver.protocoll.DataBuffer;
import eu.epicpvp.kcore.Permission.Group.Group;
import eu.epicpvp.kcore.Permission.Group.GroupTyp;
import lombok.Getter;

//WolverinDEV=57091d6f-839f-48b7-a4b1-4474222d4ad1
public class PermissionPlayer {
	@Getter
	private UUID uuid;
	@Getter
	private ArrayList<Permission> permissions = new ArrayList<>();
	@Getter
	private ArrayList<Group> groups = new ArrayList<>();
	
	private PermissionManager manager;
	private Player player;
	
	public PermissionPlayer(Player player,PermissionManager manager,UUID uuid){
		this.player=player;
		this.manager = manager;
		this.uuid = uuid;
		loadPermissions();
	}
	
	private void loadPermissions(){
		System.out.println("Requesting player Permissions");
		DataBuffer buffer = manager.handler.sendMessage(player, new DataBuffer().writeByte(0).writeUUID(uuid)).getSync(); //Action: 0 (Get-Perms)
		if(buffer == null){
			System.out.println("Response == null");
			return;
		}
		int length = buffer.readInt();
		if(length == -1){ //Error
			System.out.println("Having an error: "+buffer.readString());
			return;
		}
		
		ArrayList<String> sgroups = new ArrayList<>();
		for(int i = 0;i<length;i++){
			sgroups.add(buffer.readString());
		}
		
		length = buffer.readInt();
		for(int i = 0;i<length;i++){
			permissions.add(new Permission(buffer.readString(), GroupTyp.values()[buffer.readByte()]));
		}
		System.out.println("Permissions geladen. Lade gruppen");
		
		for(String s : sgroups){
			Group g = manager.getGroup(s);
			if(g == null)
				g = manager.loadGroup(s);
			groups.add(g);
		}
		System.out.println("Player geladen");
		
		Bukkit.getScheduler().runTask(manager.getInstance(), new BukkitRunnable() {
			
			@Override
			public void run() {
				manager.setTabList(player);
			}
		});
	}
	
	public void reloadPermissions(){
		loadPermissions();
	}
	
	public void addPermission(String permission){
		addPermission(permission, GroupTyp.ALL);
	}
	public void addPermission(String permission,GroupTyp type){
		if(!permissions.contains(new Permission(permission,type))){
			permissions.add(new Permission(permission,type));
			manager.handler.sendMessage(player, new DataBuffer().writeByte(2).writeUUID(uuid).writeByte(type.ordinal()));
		}
	}
	public boolean hasPermission(String permission){
		return hasPermission(permission, GroupTyp.ALL);
	}
	public boolean hasPermission(String permission,GroupTyp type){
		for(Permission p : new ArrayList<>(permissions))
			if((type == GroupTyp.ALL || p.getGroup() == type) && p.acceptPermission(permission))
				return true;
		for(Group group : groups)
			if(group.hasPermission(permission, type))
				return true;
		return false;
	}
}
