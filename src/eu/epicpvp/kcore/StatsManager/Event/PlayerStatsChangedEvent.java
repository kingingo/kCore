package eu.epicpvp.kcore.StatsManager.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.datenserver.definitions.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import lombok.Getter;

public class PlayerStatsChangedEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private StatsKey stats;
  @Getter
  private int playerId;
  @Getter
  private StatsManager manager;


  public PlayerStatsChangedEvent(StatsManager manager,StatsKey s,int playerId)
  {
	this.manager=manager;
    this.playerId=playerId;
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
