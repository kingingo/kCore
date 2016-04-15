package eu.epicpvp.kcore.Listener.AfkListener;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilServer;

public class AFKListener extends kListener{
	
	private HashMap<Player,Long> time;
	private HashMap<Player,Location> locations;
	
	public AFKListener(JavaPlugin instance) {
		super(instance, "AFKListener");
		this.time=new HashMap<Player,Long>();
		this.locations=new HashMap<Player,Location>();
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		this.time.put(ev.getPlayer(), System.currentTimeMillis());
		this.locations.put(ev.getPlayer(), ev.getPlayer().getLocation());
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		this.time.remove(ev.getPlayer());
		this.locations.remove(ev.getPlayer());
	}
	

	@EventHandler
	public void moveCheck(UpdateEvent ev){
		if(ev.getType()==UpdateType.MIN_10){
			for(Player player : UtilServer.getPlayers()){
				if(this.time.containsKey(player)){
					if(this.time.get(player) > System.currentTimeMillis()){
						
					}
				}
			}
		}
	}
	
	@EventHandler
	public void chat(AsyncPlayerChatEvent ev){
		
	}
}
