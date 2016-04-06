package eu.epicpvp.kcore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.skinupdater.SkinUpdateListener;

public class kCore extends JavaPlugin {
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new SkinUpdateListener(), this); //Needed everywhere
	}

	public void onDisable() {

	}
}