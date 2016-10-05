package eu.epicpvp.kcore.UpdateAsync;

import org.bukkit.plugin.Plugin;

import eu.epicpvp.kcore.UpdateAsync.Event.UpdateAsyncEvent;

public class UpdaterAsync implements Runnable {
	private Plugin plugin;
	private int taskId = -1;

	public UpdaterAsync(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void start(){
		this.taskId = this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 1L, 1L).getTaskId();
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
