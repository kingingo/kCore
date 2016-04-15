package eu.epicpvp.kcore.StatsManager.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import lombok.Getter;

public class PlayerStatsChangeEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private StatsKey stats;
  @Getter
  private String playername;
  @Getter
  private StatsManager manager;

  public PlayerStatsChangeEvent(StatsManager manager,StatsKey s,String playername)
  {
	this.manager=manager;
    this.playername=playername;
    this.stats=s;
  }
  public HandlerList getHandlers()
  {
    return handlers;
  }

  public static HandlerList getHandlerList()
  {
    return handlers;
  }
}