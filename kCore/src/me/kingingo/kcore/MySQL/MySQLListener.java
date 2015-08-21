package me.kingingo.kcore.MySQL;

import me.kingingo.kcore.MySQL.Events.MySQLConnectEvent;
import me.kingingo.kcore.MySQL.Events.MySQLDisconnectEvent;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MySQLListener implements Listener{
	
	private MySQL mysql;
	
	public MySQLListener(MySQL mysql){
		this.mysql=mysql;
	}
	
	@EventHandler
	public void Error(MySQLErrorEvent ev){
		switch(ev.getError()){
		case CONNECT:
			break;
		case UPDATE:
			mysql.close();
			mysql.connect();
			break;
		case QUERY:
			mysql.close();
			mysql.connect();
			break;
		case CLOSE:
			break;
		default:
			break;
		}
		
		System.err.println("[kCore] Error:");
		ev.getException().printStackTrace();
	}
	
	@EventHandler
	public void Connect(MySQLConnectEvent ev){
		System.out.println("[kCore] Die MySQL Verbindung wurde hergestellt.");
	}
	
	@EventHandler
	public void Close(MySQLDisconnectEvent ev){
		System.out.println("[kCore] Die MySQL Verbindung wurde geschlossen.");
	}
	
}
