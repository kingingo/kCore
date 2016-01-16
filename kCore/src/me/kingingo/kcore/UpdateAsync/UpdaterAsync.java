package me.kingingo.kcore.UpdateAsync;

import me.kingingo.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class UpdaterAsync
  implements Runnable
{
  private JavaPlugin _plugin;
  private int TaskID = 0;

  public UpdaterAsync(JavaPlugin plugin) {
    this._plugin = plugin;
    this.TaskID = this._plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, this,1L,1L);
    UtilServer.setUpdaterAsync(this);
  }

  public void stop() {
    if (this.TaskID != 0)
      try {
        Bukkit.getScheduler().cancelTask(TaskID);
      } finally {
        this.TaskID = 0;
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
