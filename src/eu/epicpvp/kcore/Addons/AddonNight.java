package eu.epicpvp.kcore.Addons;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import lombok.Getter;

public class AddonNight implements Listener {

	@Getter
	JavaPlugin instance;
	@Getter
	World world;

	public AddonNight(JavaPlugin plugin) {
		this(plugin, null);
	}

	public AddonNight(JavaPlugin plugin, World w) {
		this.world = w;
		this.instance = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void Night(UpdateEvent ev) {
		if (ev.getType() != UpdateType.FASTEST)
			return;

		if (world != null) {
			checkWorld(world);
		} else {
			for (World w : Bukkit.getWorlds())
				checkWorld(w);
		}
	}

	public void checkWorld(World world) {
		if (world.isThundering()) {
			world.setStorm(false);
		}
		if (world.getTime() >= 0 && world.getTime() <= 13000) {
			long time = world.getTime();
			time += 100;
			world.setStorm(false);
			world.setTime(time);
		}
	}

}
