package me.kingingo.kcore.Permission;

import javax.xml.crypto.dsig.keyinfo.PGPData;

import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilList;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PermissionListener implements Listener {
	
	private PermissionManager manager;
	
	public PermissionListener(PermissionManager manager){
		this.manager=manager;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Login(PlayerLoginEvent ev){
		Player p = ev.getPlayer();
	    manager.loadPermission(p);
	}
	
	boolean b = false;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()==UpdateType.MIN_64){
			UtilList.CleanList(manager.getPgroup());
		}else if(ev.getType()==UpdateType.MIN_32){
			UtilList.CleanList(manager.getPlist());
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Join(PlayerJoinEvent ev){
		manager.setTabList(ev.getPlayer());
	}
}