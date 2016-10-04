package eu.epicpvp.kcore.Listener.ClientListener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.datenclient.client.ClientWrapper;
import eu.epicpvp.kcore.Events.ClientConnectedEvent;
import eu.epicpvp.kcore.Events.ClientDisconnectedEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.UpdateAsync.UpdateAsyncType;
import eu.epicpvp.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import lombok.Setter;

public class ClientListener extends kListener{

	@Getter
	private ClientWrapper client;
	@Getter
	@Setter
	private String password;

	public ClientListener(JavaPlugin instance,ClientWrapper client,String password) {
		super(instance, "ClientListener");
		this.client=client;
		this.password=password;
	}

	@EventHandler
	public void asnyc(UpdateAsyncEvent ev){
		if(ev.getType()==UpdateAsyncType.SEC_4){
			if(client != null && !client.getHandle().isConnected()){
				try {
					client.getHandle().connect(getPassword().getBytes());

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

	@EventHandler(priority=EventPriority.HIGHEST)
	public void a(AsyncPlayerPreLoginEvent e){
		if (client == null || !client.getHandle().isConnected() || !client.getHandle().isHandshakeCompleted() ) {
			return;
		}
		UtilServer.getClient().getPlayerAndLoad(e.getName());
		System.out.println("§aPlayer "+e.getName()+" loaded!");
	}
}
