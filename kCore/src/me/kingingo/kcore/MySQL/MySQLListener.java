package me.kingingo.kcore.MySQL;

import lombok.Getter;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.MySQL.Events.MySQLConnectEvent;
import me.kingingo.kcore.MySQL.Events.MySQLDisconnectEvent;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;

import org.bukkit.event.EventHandler;

public class MySQLListener extends kListener{
	
	@Getter
	private MySQL mysql;
	
	public MySQLListener(MySQL mysql){
		super(mysql.getInstance(),"MySQLListener");
		this.mysql=mysql;
	}
	
	@EventHandler
	public void Error(MySQLErrorEvent ev){
		switch(ev.getError()){
			case CONNECT:
				break;
			case UPDATE:
				this.mysql.close();
				this.mysql.connect();
				break;
			case QUERY:
				this.mysql.close();
				this.mysql.connect();
				break;
			case CLOSE:
				break;
		}
		
		Log("Error:");
		ev.getException().printStackTrace();
	}
	
	@EventHandler
	public void Connect(MySQLConnectEvent ev){
		Log("Die MySQL Verbindung wurde hergestellt.");
	}
	
	@EventHandler
	public void Close(MySQLDisconnectEvent ev){
		Log("Die MySQL Verbindung wurde geschlossen.");
	}
	
}
