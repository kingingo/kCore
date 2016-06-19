package eu.epicpvp.kcore;

import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.Skin.SkinUpdateListener;

public class kCore extends JavaPlugin {
	public void onEnable() {
		new SkinUpdateListener(this); //Needed everywhere
	}
	public void onDisable() {
		
	}
}