package me.kingingo.kcore.Update;

import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.plugin.java.JavaPlugin;

public class Updater
implements Runnable
{
private JavaPlugin _plugin;
private int TaskID=-1;

public Updater(JavaPlugin plugin)
{
  this._plugin = plugin;
  TaskID=this._plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this._plugin, this, 0L, 1L);
}

public void stop(){
	if(TaskID!=-1){
		try{
		this._plugin.getServer().getScheduler().cancelTask(TaskID);
		}finally{
			TaskID=-1;
		}
	}
}

public void run()
{
  for (UpdateType updateType : UpdateType.values())
  {
    if (updateType.Elapsed())
    {
      this._plugin.getServer().getPluginManager().callEvent(new UpdateEvent(updateType));
    }
  }
}
}
