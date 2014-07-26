package me.kingingo.kcore.Permission;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PermissionListener implements Listener {
	
	private PermissionManager manager;
	
	public PermissionListener(PermissionManager manager){
		this.manager=manager;
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Login(PlayerLoginEvent ev){
		Player p = ev.getPlayer();
	    manager.loadPermission(p);
	    manager.setTabList(p);
	}
	
}
