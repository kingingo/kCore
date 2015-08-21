package me.kingingo.kcore.Listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class kListener implements Listener{

	protected String moduleName;
	
	public kListener(JavaPlugin plugin,String moduleName){
		this.moduleName=moduleName;
		RegisterEvents(this,plugin);
	}
	
	public void RegisterEvents(Listener listener,JavaPlugin instance){
	    Bukkit.getServer().getPluginManager().registerEvents(listener, instance);
	}
	
	protected void Log(String message){
	  System.out.println("["+this.moduleName+"]: "+message);
	}
	
}
