package eu.epicpvp.kcore.Update;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilServer;

public class Updater implements Runnable {
	private Plugin plugin;
	private int TaskID = -1;

	public Updater(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void start(){
		TaskID = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, this, 0L, 1L);
	}

	public void stop() {
		if (TaskID != -1) {
			try {
				this.plugin.getServer().getScheduler().cancelTask(TaskID);
			} finally {
				TaskID = -1;
			}
		}
	}

	public void run() {
		for (UpdateType updateType : UpdateType.values()) {
			if (updateType.Elapsed()) {
				this.plugin.getServer().getPluginManager().callEvent(new UpdateEvent(updateType));
			}
		}
	}
}
