package me.kingingo.kcore.Scheduler;

import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class kScheduler extends kListener{

	private UpdateType type;
	private kSchedulerHandler handler;
	
	public interface kSchedulerHandler {
        public void onRun();
    }
	
	public kScheduler(JavaPlugin instance,kSchedulerHandler handler,UpdateType type){
		super(instance,"kScheduler");
		this.handler=handler;
		this.type=type;
	}
	
	public void close(){
		this.handler=null;
		HandlerList.unregisterAll(this);
	}
	
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=this.type)return;
		if(handler!=null)handler.onRun();
	}
	
}
