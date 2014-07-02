package me.kingingo.kcore.MySQL;

import me.kingingo.kcore.MySQL.Events.MySQLConnectEvent;
import me.kingingo.kcore.MySQL.Events.MySQLDisconnectEvent;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MySQLListener implements Listener{
	
	boolean connect=true;
	private MySQL mysql;
	
	public MySQLListener(MySQL mysql){
		this.mysql=mysql;
	}
	
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_04)return;
		if(connect)return;
		mysql.connect();
	}
	
	@EventHandler
	public void Error(MySQLErrorEvent ev){
		switch(ev.getError()){
		case  CONNECT:
			connect=false;
			break;
		case UPDATE:
			mysql.close();
			connect=false;
			break;
		case QUERY:
			mysql.close();
			connect=false;
			break;
		}
		System.err.println("[kCORE] Error: "+ev.getException());
	}
	
	@EventHandler
	public void Connect(MySQLConnectEvent ev){
		connect=true;
		System.out.println("[kCORE] Die MySQL Verbindung wurde hergestellt.");
	}
	
	@EventHandler
	public void Close(MySQLDisconnectEvent ev){
		connect=true;
		System.out.println("[kCORE] Die MySQL Verbindung wurde geschlossen.");
	}
	
}
