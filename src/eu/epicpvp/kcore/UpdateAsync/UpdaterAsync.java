package eu.epicpvp.kcore.UpdateAsync;

import eu.epicpvp.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import eu.epicpvp.kcore.Util.UtilServer;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdaterAsync implements Runnable {
	private JavaPlugin plugin;
	private int taskId = -1;

	public UpdaterAsync(JavaPlugin plugin) {
		this.plugin = plugin;
		this.taskId = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 1L, 1L).getTaskId();
		UtilServer.setUpdaterAsync(this);
	}

	public void stop() {
		if (this.taskId != -1) {
			try {
				this.plugin.getServer().getScheduler().cancelTask(taskId);
			} finally {
				this.taskId = -1;
			}
		}
	}

	@Override
	public void run() {
		for (UpdateAsyncType updateType : UpdateAsyncType.values()) {
			if (updateType.Elapsed()) {
				this.plugin.getServer().getPluginManager().callEvent(new UpdateAsyncEvent(updateType));
			}
		}
	}
}
