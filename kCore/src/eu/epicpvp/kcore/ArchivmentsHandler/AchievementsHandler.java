package eu.epicpvp.kcore.ArchivmentsHandler;

import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class AchievementsHandler extends kListener{

	@Getter
	private JavaPlugin instance;
	
	public AchievementsHandler(JavaPlugin instance) {
		super(instance, "AchievementsHandler");
		this.instance=instance;
		UtilServer.setAchievementsHandler(this);
	}

}
