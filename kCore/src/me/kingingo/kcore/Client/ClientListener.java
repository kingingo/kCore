package me.kingingo.kcore.Client;

import lombok.Getter;
import me.kingingo.kcore.Client.Events.ClientConnectEvent;
import me.kingingo.kcore.Client.Events.ClientDisconnectEvent;
import me.kingingo.kcore.Client.Events.ClientErrorConnectEvent;
import me.kingingo.kcore.Client.Events.ClientLostConnectionEvent;
import me.kingingo.kcore.Client.Events.ClientReceiveMessageEvent;
import me.kingingo.kcore.Client.Events.ClientSendMessageEvent;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

public class ClientListener extends kListener{

	@Getter
	private Client client;
	
	public ClientListener(JavaPlugin instance, Client client){
		super(instance,"Client");
		this.client=client;
	}
	
	@EventHandler
	public void client(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_005)return;
		if(getClient().isConnected())return;
		getClient().connect();
	}
	
	@EventHandler
	public void connected(ClientConnectEvent ev){
		Log("Der Client verbindet sich zum Daten-Server");
		getClient().setConnected(true);
	}
	
	@EventHandler
	public void message(ClientSendMessageEvent ev){
		Log("Der Client sendet die Nachricht '"+ev.getMessage()+"' zum Daten-Server.");
	}
	
	@EventHandler
	public void disconnect(ClientDisconnectEvent ev){
		Log("Der Client hat die verbindung zum Daten-Server getrennt.");
		getClient().setConnected(false);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void received(ClientReceiveMessageEvent ev){
		if(ev.getMessage().contains("whitelist=?off")){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist off");
		}else if(ev.getMessage().contains("whitelist=?on")){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist on");
		}else if(ev.getMessage().equalsIgnoreCase("ping")){
			Log("Der Client Empfaengt Nachricht '"+ev.getMessage()+"' vom Daten-Server.");
			getClient().sendMessageToServer("pong");
		}else if(ev.getMessage().equalsIgnoreCase("stop=?now")){
			Log("Der Client Empfaengt Nachricht '"+ev.getMessage()+"' vom Daten-Server.");
			Bukkit.getScheduler().runTask(getClient().getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
					
				}
			});
		}else if(ev.getMessage().equalsIgnoreCase("restart=?now")){
			Log("Der Client Empfaengt Nachricht '"+ev.getMessage()+"' vom Daten-Server.");
			Bukkit.getScheduler().runTask(getClient().getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
					
				}
			});
		}else if(ev.getMessage().equalsIgnoreCase("reload=?now")){
			Log("Der Client Empfaengt Nachricht '"+ev.getMessage()+"' vom Daten-Server.");
			Bukkit.getScheduler().runTask(getClient().getInstance(), new Runnable() {
				
				@Override
				public void run() {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "reload");
					
				}
			});
		}
	}
	
	@EventHandler
	public void error(ClientErrorConnectEvent ev){
		Log("Der Client konnte sich nicht verbinden mit dem Daten-Server.");
		getClient().setConnected(false);
	}
	
	@EventHandler
	public void lost(ClientLostConnectionEvent ev){
		Log("Der Client hat die Verbingung verloren ...");
		getClient().setConnected(false);
	}
	
}
