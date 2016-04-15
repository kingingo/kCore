package eu.epicpvp.kcore.Addons;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import lombok.Getter;

public class AddonDay implements Listener {

	@Getter
	JavaPlugin instance;
	@Getter
	World world;
	
	public AddonDay(JavaPlugin plugin,World w){
		this.world=w;
		this.instance=plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	long time;
	@EventHandler
	public void Night(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FASTEST)return;
		if(getWorld().isThundering()){
			getWorld().setStorm(false);
		}
		if(getWorld().getTime()<3000||getWorld().getTime()>9000){
			time=getWorld().getTime();
			time+=100;
			getWorld().setStorm(false);
			getWorld().setTime(time);
		}
	}
	
}

