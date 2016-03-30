package eu.epicpvp.kcore.Addons;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import lombok.Getter;

public class AddonSun extends kListener {

	@Getter
	private JavaPlugin instance;
	@Getter
	private World world;
	
	public AddonSun(JavaPlugin plugin,World w){
		super(plugin,"AddonSun");
		this.world=w;
		this.instance=plugin;
	}
	
	@EventHandler
	public void weather(WeatherChangeEvent ev){
		if(ev.getWorld().getUID()==getWorld().getUID()){
			if(ev.toWeatherState())ev.getWorld().setStorm(false);
		}
	}
	
}

