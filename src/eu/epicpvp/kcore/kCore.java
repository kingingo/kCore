package eu.epicpvp.kcore;

import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.MoneyListener.MoneyListener;
import eu.epicpvp.kcore.Listener.Skin.SkinUpdateListener;
import lombok.Getter;

public class kCore extends JavaPlugin {
	@Getter
	private static kCore instance;
	
	public void onEnable() {
		instance = this;
		new SkinUpdateListener(this).registerListener(); //Needed everywhere
	}
	public void onDisable() {
		
	}
}