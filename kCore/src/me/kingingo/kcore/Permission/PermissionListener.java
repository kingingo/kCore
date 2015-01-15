package me.kingingo.kcore.Permission;

import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.PERMISSION_GROUP_RELOAD;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_RELOAD;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_REMOVE_ALL;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilList;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionListener implements Listener {
	
	private PermissionManager manager;
	
	public PermissionListener(PermissionManager manager){
		this.manager=manager;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Login(AsyncPlayerPreLoginEvent ev){
	    manager.loadPermission(ev.getName());
	}
	
	boolean b = false;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()==UpdateType.MIN_64){
			UtilList.CleanList(manager.getPgroup());
			UtilList.CleanList(manager.getPlist());
		}
	}
	
	@EventHandler
	public void Packet(PacketReceiveEvent ev){
		if(ev.getPacket() instanceof PERMISSION_GROUP_RELOAD){
			PERMISSION_GROUP_RELOAD packet = (PERMISSION_GROUP_RELOAD)ev.getPacket();
			manager.getGroups().remove(packet.getGroup().toLowerCase());
			manager.getGprefix().remove(packet.getGroup().toLowerCase());
			manager.loadGroup(packet.getGroup().toLowerCase());
		}else if(ev.getPacket() instanceof PERMISSION_USER_RELOAD){
			PERMISSION_USER_RELOAD packet = (PERMISSION_USER_RELOAD)ev.getPacket();
			manager.getPgroup().remove(packet.getUser());
			manager.getPlist().remove(packet.getUser());
			manager.loadPermission(packet.getUser());
		}else if(ev.getPacket() instanceof PERMISSION_USER_REMOVE_ALL){
			PERMISSION_USER_REMOVE_ALL packet = (PERMISSION_USER_REMOVE_ALL)ev.getPacket();
			manager.getPgroup().remove(packet.getUser());
			manager.getPlist().remove(packet.getUser());
			Bukkit.getServer().getOperators().remove(Bukkit.getOfflinePlayer(packet.getUser()));
			if(Bukkit.getPluginManager().getPlugin("PermissionsEx")!=null){
				PermissionsEx pex = (PermissionsEx)Bukkit.getPluginManager().getPlugin("PermissionEx");
				for(String world : pex.getUser(packet.getUser()).getAllPermissions().keySet()){
					for(String permission : pex.getUser(packet.getUser()).getAllPermissions().get(world)){
						pex.getUser(packet.getUser()).removePermission(permission, world);
					}
				}
				for(String world : pex.getUser(packet.getUser()).getAllGroups().keySet()){
					for(PermissionGroup group : pex.getUser(packet.getUser()).getAllGroups().get(world)){
						pex.getUser(packet.getUser()).removeGroup(group, world);
					}
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Join(PlayerJoinEvent ev){
		manager.setTabList(ev.getPlayer());
	}
}