package me.kingingo.kcore.WalkEffect;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.WalkEffect.Events.WalkEffectEvent;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

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
	
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		for(Entity p : list.keySet()){
			WalkEffectEvent w = new WalkEffectEvent(p);
			list.get(p).onWalkEffect(w);
		}
	}
	
}
