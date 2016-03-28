package eu.epicpvp.kcore.Scheduler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;

public class kScheduler extends kListener{

	private UpdateType type;
	private kSchedulerHandler handler;
	
	public interface kSchedulerHandler {
        public void onRun(kScheduler scheduler);
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
		if(handler!=null)handler.onRun(this);
	}
	
}
