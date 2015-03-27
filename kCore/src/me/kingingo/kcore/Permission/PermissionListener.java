package me.kingingo.kcore.Permission;

import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.PERMISSION_GROUP_RELOAD;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_RELOAD;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_REMOVE_ALL;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilList;
import me.kingingo.kcore.Util.UtilPlayer;

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
	    manager.loadPermission(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()));
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
			manager.loadGroup(packet.getGroup().toLowerCase());
		}else if(ev.getPacket() instanceof PERMISSION_USER_RELOAD){
			PERMISSION_USER_RELOAD packet = (PERMISSION_USER_RELOAD)ev.getPacket();
			manager.getPgroup().remove(packet.getUuid());
			manager.getPlist().remove(packet.getUuid());
			manager.loadPermission(packet.getUuid());
		}else if(ev.getPacket() instanceof PERMISSION_USER_REMOVE_ALL){
			PERMISSION_USER_REMOVE_ALL packet = (PERMISSION_USER_REMOVE_ALL)ev.getPacket();
			manager.getPgroup().remove(packet.getUuid());
			manager.getPlist().remove(packet.getUuid());
			Bukkit.getServer().getOperators().remove(Bukkit.getOfflinePlayer(packet.getUuid()));
			if(Bukkit.getPluginManager().getPlugin("PermissionsEx")!=null){
				ru.tehkode.permissions.PermissionManager pex = ((PermissionsEx)Bukkit.getPluginManager().getPlugin("PermissionEx")).getPermissionsManager();
				
				for(String world : pex.getUser(packet.getUuid()).getAllPermissions().keySet()){
					for(String permission : pex.getUser(packet.getUuid()).getAllPermissions().get(world)){
						pex.getUser(packet.getUuid()).removePermission(permission, world);
					}
				}
				for(String world : pex.getUser(packet.getUuid()).getAllGroups().keySet()){
					for(PermissionGroup group : pex.getUser(packet.getUuid()).getAllGroups().get(world)){
						pex.getUser(packet.getUuid()).removeGroup(group, world);
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