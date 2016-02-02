package me.kingingo.kcore.Listener;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class kThreadListener extends Thread implements Listener{

	@Getter
	protected String moduleName;
	
	public kThreadListener(JavaPlugin plugin,String moduleName){
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
