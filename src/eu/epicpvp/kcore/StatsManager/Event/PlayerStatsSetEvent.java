package eu.epicpvp.kcore.StatsManager.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.datenserver.definitions.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import lombok.Getter;

public class PlayerStatsSetEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private StatsKey stats;
  @Getter
  private Object value;
  @Getter
  private int playerId;
  @Getter
  private StatsManager manager;

  public PlayerStatsSetEvent(StatsManager manager,StatsKey s,Object value,int playerId){
	this.manager=manager;
    this.playerId=playerId;
    this.stats=s;
    this.value=value;
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
