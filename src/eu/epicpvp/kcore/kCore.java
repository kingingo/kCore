package eu.epicpvp.kcore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import eu.epicpvp.kcore.Listener.Skin.SkinUpdateListener;
import eu.epicpvp.kcore.Listener.nick.NickChangeListener;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class kCore extends JavaPlugin {
	@Getter
	private static kCore instance;
	
	public void onEnable() {
		instance = this;
		NickChangeListener nickListener = new NickChangeListener(this);
		UtilServer.createPacketListener(this).addPacketHandler(nickListener);
		Bukkit.getPluginManager().registerEvents(nickListener, this);
		new SkinUpdateListener(this).registerListener(); //Needed everywhere
		
		Bukkit.getScoreboardManager().getMainScoreboard().getTeams().stream().forEach(Team::unregister);
	}
	public void onDisable() {
		
	}
}