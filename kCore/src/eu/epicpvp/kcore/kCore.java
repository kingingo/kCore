package eu.epicpvp.kcore;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.Skin.SkinUpdateListener;
import eu.epicpvp.kcore.UpdateAsync.Event.UpdateAsyncEvent;

public class kCore extends JavaPlugin {
	public void onEnable() {
		new SkinUpdateListener(this); //Needed everywhere
	}
	public void onDisable() {
		System.out.println("");
	}
}