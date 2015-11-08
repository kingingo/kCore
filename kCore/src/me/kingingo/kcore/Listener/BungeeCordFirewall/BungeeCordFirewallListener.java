package me.kingingo.kcore.Listener.BungeeCordFirewall;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.Packet.Events.PacketReceiveEvent;
import me.kingingo.kcore.Packet.Packets.BG_IP_RELOAD;
import me.kingingo.kcore.Util.UtilException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class BungeeCordFirewallListener extends kListener{

	private ArrayList<String> bungeecord_ips;
	private MySQL mysql;
	private String server;
	
	public BungeeCordFirewallListener(MySQL mysql,String server) {
		super(mysql.getInstance(), "BungeeCordFirewallListener");
		this.mysql=mysql;
		this.server=server;
		this.bungeecord_ips=new ArrayList<>();
		
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
	public void packet(PacketReceiveEvent ev){
		if(ev.getPacket() instanceof BG_IP_RELOAD){
			loadBG();
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void preLogin(PlayerJoinEvent ev){
		if(!this.bungeecord_ips.contains(ev.getPlayer().spigot().getRawAddress().getAddress().getHostAddress())){
			UtilException.catchException(server, mysql.getInstance().getServer().getIp(), mysql, "IP:"+ev.getPlayer().spigot().getRawAddress().getAddress().getHostAddress()+" tried to connect without a EP BG! PlayerIP:"+ev.getPlayer().getAddress().getAddress().getHostAddress());
			ev.getPlayer().kickPlayer("§cPlease use the EpicPvP Proxy!");
		}
	}

}
