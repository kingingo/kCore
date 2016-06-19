package eu.epicpvp.kcore.Listener.ClientListener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.ClientWrapper;
import eu.epicpvp.kcore.Events.ClientConnectedEvent;
import eu.epicpvp.kcore.Events.ClientDisconnectedEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.UpdateAsync.UpdateAsyncType;
import eu.epicpvp.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class ClientListener extends kListener{

	@Getter
	private ClientWrapper client;
	
	public ClientListener(JavaPlugin instance,ClientWrapper client) {
		super(instance, "ClientListener");
		this.client=client;
	}

	@EventHandler
	public void asnyc(UpdateAsyncEvent ev){
		if(ev.getType()==UpdateAsyncType.SEC_4){
			if(client != null && !client.getHandle().isConnected()){
				try {
					client.getHandle().connect("HelloWorld".getBytes());

					TranslationHandler.setInstance(client.getTranslationManager());
					TranslationHandler.getInstance().updateTranslations();
				} catch (Exception e) {
					System.out.println("[Client]: Cannot connect...");
				}
			}
		}
	}
	
	@EventHandler
	public void connect(ClientConnectedEvent ev){
		System.out.println("[Client]: Connected");
	}

	@EventHandler
	public void disconnect(ClientDisconnectedEvent ev){
		System.out.println("[Client]: Disconnected");
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void quit(PlayerQuitEvent ev){
		getClient().clearCacheForPlayer(getClient().getPlayerAndLoad(ev.getPlayer().getName()));
	}
	
	@EventHandler
	public void a(AsyncPlayerPreLoginEvent e){
		UtilServer.getClient().getPlayerAndLoad(e.getName());
		System.out.println("§aPlayer "+e.getName()+" loaded!");
	}
}
