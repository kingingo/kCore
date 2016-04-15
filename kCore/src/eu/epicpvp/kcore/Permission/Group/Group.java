package eu.epicpvp.kcore.Permission.Group;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.protocoll.DataBuffer;
import eu.epicpvp.kcore.Permission.Permission;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.Events.GroupLoadedEvent;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class Group {

	@Getter
	private String prefix = "undefined";
	@Getter
	private String name = "undefined";
	private ArrayList<Permission> perms = new ArrayList<>();
	private PermissionManager manager;
	
	public Group(PermissionManager manager,String name) {
		this.name = name;
		this.manager = manager;
		init();
	}

	private void init(){
		perms.clear();
		DataBuffer response =  manager.getHandler().sendMessage(createPlayer(), new DataBuffer().writeByte(1).writeString(name)).getSync(); //Action=1 Getgroup informatin
		if(response == null){
			System.out.println("Cant load group "+name+" (Response == null)");
		}
		int length = response.readInt();
		if(length == -1){
			System.out.println("Group loading error: "+response.readString());
			return;
		}
		for (int i = 0; i < length; i++) {
			perms.add(new Permission(response.readString(), GroupTyp.values()[response.readByte()]));
		}
		prefix = response.readString();
		
		Bukkit.getPluginManager().callEvent(new GroupLoadedEvent(manager, this));
	}
	
	private Player createPlayer(){
		return UtilServer.getPlayers().isEmpty() ? null : UtilServer.getPlayers().iterator().next();
	}
	
	public boolean hasPermission(String permission){
		return hasPermission(permission, GroupTyp.ALL);
	}
	public boolean hasPermission(String permission,GroupTyp type){
		for(Permission p : new ArrayList<>(perms))
			if((type == GroupTyp.ALL || p.getGroup() == type) && p.acceptPermission(permission))
				return true;
		return false;
	}
	
	public void reload(){
		init();
	}

	public ArrayList<Permission> getPermissions() {
		return perms;
	}
}
