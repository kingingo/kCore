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
	
	public AddonSun(JavaPlugin instance,World world){
		super(instance,"AddonSun");
		this.world=world;
		this.instance=instance;
	}
	
	@EventHandler
	public void weather(WeatherChangeEvent ev){
		if(getWorld()==null || ev.getWorld().getUID()==getWorld().getUID()){
			if(ev.toWeatherState())ev.getWorld().setStorm(false);
		}
	}
	
}

