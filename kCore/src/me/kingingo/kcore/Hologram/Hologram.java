package me.kingingo.kcore.Hologram;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.Hologram.nametags.Events.HologramRemoveEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Hologram implements Listener{
	
	@Getter
	private HashMap<NameTagMessage, Integer> list = new HashMap<NameTagMessage,Integer>();
	@Getter
	private HashMap<Integer, NameTagMessage> creatures = new HashMap<Integer, NameTagMessage>();
	
	public Hologram(JavaPlugin plugin){
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void Join(PlayerJoinEvent ev){
		if(creatures.isEmpty())return;
		for(NameTagMessage m : creatures.values()){
			m.sendToPlayer(ev.getPlayer());
		}
	}
	
//	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
//    public void onChunkLoad(ChunkLoadEvent event) {
//		for(NameTagMessage ntm : list.keySet()){
//			if(ntm.getLocation().getChunk().equals(event.getChunk())){
//				for(Entity e : ntm.getLocation().getWorld().getEntities()){
//					if(e instanceof Player){
//						ntm.clear((Player)e);
//						ntm.sendToPlayer((Player)e);
//					}
//				}
//			}
//		}
//    }
	
	HashMap<NameTagMessage,Integer> clone;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		if(this.list.isEmpty())return;
		clone=(HashMap<NameTagMessage,Integer>)list.clone();
		for(NameTagMessage message : clone.keySet()){
			if(this.clone.get(message)==-1)continue;
			if(this.clone.get(message)>0){
				this.list.put(message, this.clone.get(message)-1);
			}else{
				Bukkit.getPluginManager().callEvent(new HologramRemoveEvent(message.getSpawner()));
				this.list.remove(message);
				for(Player p : UtilServer.getPlayers())message.clear(p);
			}
		}
	}
	
	public void RemoveText(Player p){
		for(NameTagMessage message : list.keySet()){
			message.clear(p);
		}
	}
	
	public void RemoveAllText(){
		for(Player p : UtilServer.getPlayers()){
			for(NameTagMessage message : list.keySet()){
				message.clear(p);
			}
		}
		list.clear();
	}
	
	public void setName(final Entity c,String name) {
		creatures.remove(c.getEntityId());
		try {
			final NameTagMessage message = new NameTagMessage(name);
			message.setLocation( (c.getType()==EntityType.ENDERMAN ? c.getLocation().add(0, 3.1, 0) : c.getLocation().add(0, 2.1, 0)) );
			creatures.put(c.getEntityId(),message);
			for(Player player : UtilServer.getPlayers())message.sendToPlayer(player);
		} catch (Exception error) {

			error.printStackTrace();
		}
	}
	
	public NameTagMessage createNameTagMessage(Location loc, String... msg) {
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			message.setLocation(loc);
			return message;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}
	
	//--
	public NameTagMessage sendText(final Player p, Location loc,int time, String... msg) {
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			message.sendToPlayer(p, loc);
			list.put(message, time);
			return message;
		} catch (Exception error) {

			error.printStackTrace();
		}
		return null;
	}
	
	public NameTagMessage sendText(final Player p, Location loc, String... msg) {
		
		
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			message.sendToPlayer(p, loc);
			return message;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}
	
	public NameTagMessage sendText(final Player p, Location loc,int time, String msg) {
		
		
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			message.sendToPlayer(p, loc);
			list.put(message, time);
			return message;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}
	
	public NameTagMessage sendText(final Player p, Location loc, String msg) {
		
		
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			message.sendToPlayer(p, loc);
			return message;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}
	
	//--

	public NameTagMessage sendTextAll(Location loc, String... msg) {
		
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			for (Player ps : UtilServer.getPlayers()){
				
				message.sendToPlayer(ps, loc);
			}
			
			return message;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}
	
	public NameTagMessage sendTextAll(Location loc, String msg) {
		
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			for (Player ps : UtilServer.getPlayers()){
				
				message.sendToPlayer(ps, loc);
			}
			
			return message;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}
	
	public NameTagMessage sendTextAll(Location loc,int time, String... msg) {
		
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			list.put(message, time);
			for (Player ps : UtilServer.getPlayers()){
				
				message.sendToPlayer(ps, loc);
			}
			
			return message;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}
	
	public NameTagMessage sendTextAll(Location loc,int time, String msg) {
		
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			list.put(message, time);
			for (Player ps : UtilServer.getPlayers()){
				
				message.sendToPlayer(ps, loc);
			}
			
			return message;
		} catch (Exception error) {
			error.printStackTrace();
		}
		return null;
	}
	
}
