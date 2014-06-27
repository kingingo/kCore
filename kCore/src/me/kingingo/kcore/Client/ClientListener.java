package me.kingingo.kcore.Client;

import me.kingingo.kcore.Client.Events.ClientConnectEvent;
import me.kingingo.kcore.Client.Events.ClientDisconnectEvent;
import me.kingingo.kcore.Client.Events.ClientErrorConnectEvent;
import me.kingingo.kcore.Client.Events.ClientLostConnectionEvent;
import me.kingingo.kcore.Client.Events.ClientReceiveMessageEvent;
import me.kingingo.kcore.Client.Events.ClientSendMessageEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Updater;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClientListener implements Listener{
	
	private Client c;
	
	public ClientListener(Client c){
		this.c=c;
	}
	
	boolean connected=false;
	
	@EventHandler
	public void Client(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_04)return;
		if(connected)return;
		c.connect();
	}
	
	@EventHandler
	public void Connected(ClientConnectEvent ev){
		System.out.println("[Client] Der Client verbindet sich zum Daten-Server");
		connected=true;
	}
	
	@EventHandler
	public void Message(ClientSendMessageEvent ev){
		System.out.println("[Client] Der Client sendet die Nachricht '"+ev.getMessage()+"' zum Daten-Server.");
	}
	
	@EventHandler
	public void Disconnect(ClientDisconnectEvent ev){
		System.out.println("[Client] Der Client hat die verbindung zum Daten-Server getrennt.");
		connected=false;
	}
	
	@EventHandler
	public void Received(ClientReceiveMessageEvent ev){
		//System.out.println("[Client] Der Client Empfaengt Nachricht '"+ev.getMessage()+"' vom Daten-Server.");
		if(ev.getMessage().equalsIgnoreCase("ping")){
			c.sendMessageToServer("pong");
		}
	}
	
	@EventHandler
	public void Error(ClientErrorConnectEvent ev){
		System.out.println("[Client] Der Client konnte sich nicht verbinden mit dem Daten-Server.");
		connected=false;
	}
	
	@EventHandler
	public void Lost(ClientLostConnectionEvent ev){
		System.out.println("[Client] Der Client hat die Verbingung verloren ...");
		connected=false;
	}
	
}
