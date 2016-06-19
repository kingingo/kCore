package eu.epicpvp.kcore.StatsManager.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.StatsManager.StatsManager;
import lombok.Getter;

public class PlayerStatsLoadedEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private StatsManager manager;
  @Getter
  private int playerId;

  public PlayerStatsLoadedEvent(StatsManager manager,int playerId)
  {
    this.playerId=playerId;
    this.manager=manager;
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
