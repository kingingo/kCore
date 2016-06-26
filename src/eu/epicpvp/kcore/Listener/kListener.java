package eu.epicpvp.kcore.Listener;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

public class kListener implements Listener {

	@Getter
	protected String moduleName;
	@Getter
	protected Plugin plugin;
	@Getter
	protected boolean registered = false;
	
	public kListener(JavaPlugin plugin, String moduleName) {
		this(plugin, moduleName, true);
	}
	
	public kListener(JavaPlugin plugin, String moduleName,boolean autoRegister) {
		this.moduleName = moduleName;
		this.plugin = plugin;
		if(autoRegister)
			registerListener();
	}

	public void registerListener() {
		if(!registered)
			Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		registered = true;
	}

	public void unregisterListener() {
		if(registered)
			HandlerList.unregisterAll(this);
		registered = false;
	}

	public void logMessage(String message) {
		System.out.println("[" + this.moduleName + "]: " + message);
	}

}
