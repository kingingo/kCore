package eu.epicpvp.kcore.Permission.Listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class PermissionListener extends kListener{

	private PermissionManager manager;
	
	public PermissionListener(PermissionManager manager) {
		super(manager.getInstance(), "PermissionListener");
		this.manager=manager;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGHEST)
	public void load(PlayerLoginEvent ev){
		Bukkit.getScheduler().runTaskAsynchronously(manager.getInstance(), new BukkitRunnable() {
			
			@Override
			public void run() {
				manager.loadPlayer(ev.getPlayer(), UtilPlayer.getRealUUID(ev.getPlayer()));
			}
		});
	}
	
}
