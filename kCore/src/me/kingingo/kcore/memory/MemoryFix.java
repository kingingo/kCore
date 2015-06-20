package me.kingingo.kcore.memory;

import java.util.Iterator;

import net.minecraft.server.v1_8_R3.IInventory;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class MemoryFix
{
  private JavaPlugin _plugin;
  private HumanEntity entity;
  private Iterator entityIterator;

  public MemoryFix(JavaPlugin plugin)
  {
    this._plugin = plugin;

    this._plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this._plugin, new Runnable()
    {
      public void run()
      {
    	  
    	  for(World w : Bukkit.getWorlds()){
    		  for(Entity e : w.getEntities()){
    			  if ((e instanceof IInventory)){
    	            entityIterator = ((IInventory)e).getViewers().iterator();

    	            while (entityIterator.hasNext()){
    	              entity = (HumanEntity)entityIterator.next();

    	              if (((entity instanceof CraftPlayer)) && (!((CraftPlayer)entity).isOnline())){
    	                entityIterator.remove();
    	              }
    	            }
    	          }
    		  }
    	  }
      }
    }
    , 100L, 100L);
  }
}