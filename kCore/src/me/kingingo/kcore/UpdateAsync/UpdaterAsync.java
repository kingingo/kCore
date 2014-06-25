package me.kingingo.kcore.UpdateAsync;

import me.kingingo.kcore.UpdateAsync.Event.UpdateAsyncEvent;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class UpdaterAsync
implements Runnable
{
private JavaPlugin _plugin;
private BukkitTask TaskID=null;

public UpdaterAsync(JavaPlugin plugin){
  this._plugin = plugin;
  TaskID=this._plugin.getServer().getScheduler().runTaskAsynchronously(this._plugin, this);
}

public void stop(){
	if(TaskID!=null){
		try{
		this.TaskID.cancel();
		}finally{
			TaskID=null;
		}
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
