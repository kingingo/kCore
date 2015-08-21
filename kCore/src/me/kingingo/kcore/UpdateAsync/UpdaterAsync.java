package me.kingingo.kcore.UpdateAsync;

import me.kingingo.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class UpdaterAsync
  implements Runnable
{
  private JavaPlugin _plugin;
  private int TaskID = -1;

  public UpdaterAsync(JavaPlugin plugin) {
    this._plugin = plugin;
    this.TaskID = this._plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, this, 1L, 1L);
  }

  public void stop() {
    if (this.TaskID != -1)
      try {
        Bukkit.getScheduler().cancelTask(this.TaskID);
      } finally {
        this.TaskID = -1;
      }
  }

  public void run()
  {
    for (UpdateAsyncType updateType : UpdateAsyncType.values())
    {
      if (updateType.Elapsed())
      {
        this._plugin.getServer().getPluginManager().callEvent(new UpdateAsyncEvent(updateType));
      }
    }
  }
}
