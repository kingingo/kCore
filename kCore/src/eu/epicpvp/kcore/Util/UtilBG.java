package eu.epicpvp.kcore.Util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.Setter;

public class UtilBG {

	private static boolean isRegestiert=false;
	@Getter
	@Setter
	public static String hub = "hub1";
	
	public static void RegisterBungeeCord(JavaPlugin plugin){
		Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
		isRegestiert=true;
	}
	
	public static void SendToBungeeCord(String msg,Player p,JavaPlugin plugin){
		if(!isRegestiert)RegisterBungeeCord(plugin);
		
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		
		
		try {
		    out.writeUTF(msg);
		} catch (IOException e) {
		 
		}
		 p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
	}
	
	public static void sendToServer(Player p,JavaPlugin plugin) {
		sendToServer(p, getHub(), plugin);
	}
	
	public static void sendToServer(Player p, String server,JavaPlugin plugin) {
		if(!isRegestiert)RegisterBungeeCord(plugin);
		
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bs);
		try {
			out.writeUTF("Connect");
			out.writeUTF(server);
		}catch (IOException ex){
			System.err.println(ex);
		}
		p.sendPluginMessage(plugin, "BungeeCord", bs.toByteArray());
	}
	
}
