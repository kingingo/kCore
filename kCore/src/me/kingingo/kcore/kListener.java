package me.kingingo.kcore;

import me.kingingo.kcore.Util.F;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class kListener implements Listener{

	protected JavaPlugin plugin;
	protected String moduleName;
	
	public kListener(JavaPlugin plugin,String moduleName){
		this.plugin=plugin;
		this.moduleName=moduleName;
		RegisterEvents(this);
	}
	
	public void RegisterEvents(Listener listener){
	    this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
	}
	
	protected void Log(String message){
	  System.out.println(F.main(this.moduleName, message));
	}
	
}
