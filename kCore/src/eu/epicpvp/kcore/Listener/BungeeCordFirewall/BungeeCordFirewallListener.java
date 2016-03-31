package eu.epicpvp.kcore.Listener.BungeeCordFirewall;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Command.Admin.CommandFirewall;
import eu.epicpvp.kcore.Events.ServerMessageEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.MySQL.MySQLErr;
import eu.epicpvp.kcore.MySQL.Events.MySQLErrorEvent;
import eu.epicpvp.kcore.Util.UtilException;
import lombok.Getter;
import lombok.Setter;

public class BungeeCordFirewallListener extends kListener{

	private ArrayList<String> bungeecord_ips;
	@Getter
	private MySQL mysql;
	private String server;
	@Getter
	@Setter
	private boolean firewall=true;
	
	public BungeeCordFirewallListener(MySQL mysql,CommandHandler cmd,String server) {
		super(mysql.getInstance(), "BungeeCordFirewallListener");
		this.mysql=mysql;
		this.server=server;
		this.bungeecord_ips=new ArrayList<>();
		cmd.register(CommandFirewall.class, new CommandFirewall(this));
		
		loadBG();
	}
	
	public void loadBG(){
		bungeecord_ips.clear();
		try{
			ResultSet rs = mysql.Query("SELECT ip FROM BG_Online");
		      while (rs.next()){
		    	  bungeecord_ips.add(rs.getString(1));
		      }
		      rs.close();
		}catch (SQLException e){
			Bukkit.getServer().getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,mysql));
		}
	}
	
	@EventHandler
	public void message(ServerMessageEvent ev){
		if(ev.getChannel().equalsIgnoreCase("BungeeFirewall")){
			loadBG();
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void preLogin(PlayerJoinEvent ev){
		if(!firewall)return;
		if(!this.bungeecord_ips.contains(ev.getPlayer().spigot().getRawAddress().getAddress().getHostAddress())){
			UtilException.catchException(server, mysql.getInstance().getServer().getIp(), mysql, "IP:"+ev.getPlayer().spigot().getRawAddress().getAddress().getHostAddress()+" tried to connect without a EP BG! PlayerIP:"+ev.getPlayer().getAddress().getAddress().getHostAddress());
			
			ev.getPlayer().kickPlayer("§cPlease use the EpicPvP Proxy!");
		}
	}

}
