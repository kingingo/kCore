package eu.epicpvp.kcore.MySQL;

import org.bukkit.event.EventHandler;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MySQL.Events.MySQLConnectEvent;
import eu.epicpvp.kcore.MySQL.Events.MySQLDisconnectEvent;
import eu.epicpvp.kcore.MySQL.Events.MySQLErrorEvent;
import lombok.Getter;

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
