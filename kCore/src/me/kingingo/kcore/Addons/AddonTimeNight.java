package me.kingingo.kcore.Addons;

import lombok.Getter;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AddonTimeNight implements Listener {

	@Getter
	JavaPlugin instance;
	@Getter
	World world;
	@Getter
	boolean drehen=false;
	
	public AddonTimeNight(JavaPlugin plugin,World w){
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
		if(getWorld().getTime()>=0&&getWorld().getTime()<13000){
			time=getWorld().getTime();
			time+=80;
			getWorld().setStorm(false);
			getWorld().setTime(time);
		}
	}
	
}
