package me.kingingo.kcore.Hologram;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.Hologram.nametags.NameTagType;
import me.kingingo.kcore.Hologram.nametags.Events.HologramRemoveEvent;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Hologram extends kListener{
	
	@Getter
	private HashMap<NameTagMessage, Long> timer = new HashMap<NameTagMessage,Long>();
	@Getter
	private HashMap<Integer, NameTagMessage> creatures = new HashMap<Integer, NameTagMessage>();
	
	public Hologram(JavaPlugin plugin){
		super(plugin,"Hologram");
		UtilServer.createHologram(this);
	}
	
	HashMap<NameTagMessage,Long> clone;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		if(this.timer.isEmpty())return;
		this.clone=(HashMap<NameTagMessage,Long>)timer.clone();
		for(NameTagMessage message : clone.keySet()){
			if(this.clone.get(message)==-1)continue;
			if(this.clone.get(message)<System.currentTimeMillis()){
				Bukkit.getPluginManager().callEvent(new HologramRemoveEvent(message));
				this.timer.remove(message);
				message.clear();
				message.remove();
			}
		}
	}
	
	public void RemoveText(Player p){
		for(NameTagMessage message : this.timer.keySet()){
			message.clear(p);
		}
	}
	
	public void RemoveText(){
		for(NameTagMessage message : this.timer.keySet()){
			message.remove();
		}
		
		this.timer.clear();
		
		ArmorStand a;
		for(World w : Bukkit.getServer().getWorlds()){
			for(Entity e : w.getEntities()){
				if(e instanceof ArmorStand){
					a=(ArmorStand)e;
					if(!a.isVisible()&&a.isCustomNameVisible()&&a.isSmall()&&!a.hasArms()&&!a.hasBasePlate()){
						a.remove();
					}
				}
			}
		}
	}
	
	public NameTagMessage setCreatureName(final Entity entity,String msg) {
		return setCreatureName(null, entity, msg);
	}
	
	public NameTagMessage setCreatureName(Player player, final Entity entity,String... msg) {
		this.creatures.put(entity.getEntityId(), sendText(player, entity.getLocation().add(0, 2.1, 0), msg));
		return this.creatures.get(entity.getEntityId());
	}
	
	public NameTagMessage sendText(Location location,int time_in_sec, String msg) {
		return sendText(null, location, time_in_sec, msg);
	}
	
	public NameTagMessage sendText(Location location,int time_in_sec, String... msg) {
		return sendText(null, location, time_in_sec, msg);
	}
	
	public NameTagMessage sendText(Location location, String msg) {
		return sendText(null, location, 0, msg);
	}
	
	public NameTagMessage sendText(Location location, String... msg) {
		return sendText(null, location, 0, msg);
	}
	
	public NameTagMessage sendText(final Player player, Location location, String msg) {
		return sendText(player, location, 0, msg);
	}
	
	public NameTagMessage sendText(final Player player, Location location,int time_in_sec, String msg) {
		return sendText(player, location, time_in_sec, new String[]{msg});
	}
	
	public NameTagMessage sendText(final Player player, Location location, String... msg) {
		return sendText(player, location, 0, msg);
	}
	
	public NameTagMessage sendText(final Player player, Location location,int time_in_sec, String... msg) {
		NameTagMessage m=null;
		if(player==null){
			m = new NameTagMessage(NameTagType.PACKET, location, msg);
			m.send();
			
			if(time_in_sec<0)this.timer.put(m, System.currentTimeMillis() + (TimeSpan.SECOND*time_in_sec));
		}else{
			m = new NameTagMessage(NameTagType.PACKET, location, msg);
			m.sendToPlayer(player);
			
			if(time_in_sec<0)this.timer.put(m, System.currentTimeMillis() + (TimeSpan.SECOND*time_in_sec));
		}
		
		return m;
	}
	

}
