package eu.epicpvp.kcore.Update;

import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilServer;

public class Updater implements Runnable {
	private JavaPlugin _plugin;
	private int TaskID = -1;

	public Updater(JavaPlugin plugin) {
		this._plugin = plugin;
		TaskID = this._plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this._plugin, this, 0L, 1L);
		UtilServer.setUpdater(this);
	}

	public void stop() {
		if (TaskID != -1) {
			try {
				this._plugin.getServer().getScheduler().cancelTask(TaskID);
			} finally {
				TaskID = -1;
			}
		}
	}

	public void run() {
		for (UpdateType updateType : UpdateType.values()) {
			if (updateType.Elapsed()) {
				this._plugin.getServer().getPluginManager().callEvent(new UpdateEvent(updateType));
			}
		}
	}
}
