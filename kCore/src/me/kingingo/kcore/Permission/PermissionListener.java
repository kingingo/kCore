package me.kingingo.kcore.Permission;

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
	
//	@EventHandler
//	public void Quit(PlayerQuitEvent ev){
//		manager.getPlayerAttachment().remove(ev.getPlayer());
//	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Join(PlayerJoinEvent ev){
		manager.setTabList(ev.getPlayer());
//		if(!manager.getPlayerAttachment().containsKey(ev.getPlayer())){
//			manager.getPlayerAttachment().put(ev.getPlayer(),ev.getPlayer().addAttachment(manager.getInstance()));
//			for(Permission perm : manager.getPermissionList(ev.getPlayer())){
//				manager.getPlayerAttachment().get(ev.getPlayer()).setPermission(perm.getPermissionToString(), true);
//			}
//		}
	}
	
}
