package eu.epicpvp.kcore.WalkEffect;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.WalkEffect.Events.WalkEffectEvent;
import lombok.Getter;

public class WalkEffectManager extends kListener {

	@Getter
	private JavaPlugin instance;
	private HashMap<Entity,WalkEffectEventHandler> list = new HashMap<>();
	
	public interface WalkEffectEventHandler {
        public void onWalkEffect(WalkEffectEvent event);
    }
	
	public WalkEffectManager(JavaPlugin instance){
		super(instance,"[WalkEffectManager]");
		this.instance=instance;
	}
	
	public void add(Entity player,WalkEffectEventHandler handler){
		if(list.containsKey(player))del(player);
		list.put(player,handler);
	}
	
	public void del(Entity player){
		list.remove(player);
	}
	
	WalkEffectEvent w;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		for(Entity p : list.keySet()){
			if(w==null){
				w = new WalkEffectEvent(p);
			}else{
				w.setPlayer(p);
			}
			
			list.get(p).onWalkEffect(w);
			Bukkit.getPluginManager().callEvent(w);
		}
	}
	
}
