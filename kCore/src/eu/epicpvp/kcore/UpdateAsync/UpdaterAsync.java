package eu.epicpvp.kcore.UpdateAsync;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import eu.epicpvp.kcore.Util.UtilServer;

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
