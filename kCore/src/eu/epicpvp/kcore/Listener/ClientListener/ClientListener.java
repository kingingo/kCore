package eu.epicpvp.kcore.Listener.ClientListener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.ClientWrapper;
import eu.epicpvp.kcore.Listener.kListener;
import lombok.Getter;

public class ClientListener extends kListener{

	@Getter
	private ClientWrapper client;
	
	public ClientListener(JavaPlugin instance,ClientWrapper client) {
		super(instance, "ClientListener");
		this.client=client;
	}

//	@EventHandler(priority=EventPriority.HIGHEST)
//	public void join(PlayerJoinEvent ev){
//		getClient().clearCacheForPlayer(getClient().getPlayer(ev.getPlayer().getName()));
//	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void quit(PlayerQuitEvent ev){
		getClient().clearCacheForPlayer(getClient().getPlayer(ev.getPlayer().getName()));
	}
}
