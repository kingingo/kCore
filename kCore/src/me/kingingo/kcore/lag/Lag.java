package me.kingingo.kcore.lag;

import me.kingingo.kcore.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class Lag extends kListener
{
public static int TICK_COUNT= 0;
public static long[] TICKS= new long[600];
public static long LAST_TICK= 0L;
public static int target;
public static long elapsed;
public static long time;

public Lag(JavaPlugin instance){
	super(instance,"LagTPS");
}

public static double getTPS()
{
  return getTPS(100);
}

public static double getTPS(int ticks)
{
  if (TICK_COUNT< ticks) {
    return 20.0D;
  }
  target = (TICK_COUNT- 1 - ticks) % TICKS.length;
  elapsed = System.currentTimeMillis() - TICKS[target];

  return ticks / (elapsed / 1000.0D);
}

public static long getElapsed(int tickID)
{
  if (TICK_COUNT- tickID >= TICKS.length)
  {
  }

  time = TICKS[(tickID % TICKS.length)];
  return System.currentTimeMillis() - time;
}

@EventHandler
public void Update(UpdateEvent ev){
  if(ev.getType()!=UpdateType.TICK)return;
  TICKS[(TICK_COUNT% TICKS.length)] = System.currentTimeMillis();
  TICK_COUNT+= 1;
}

}