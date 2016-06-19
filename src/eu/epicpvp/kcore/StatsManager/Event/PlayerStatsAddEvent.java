package eu.epicpvp.kcore.StatsManager.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import lombok.Getter;
import lombok.Setter;

public class PlayerStatsAddEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private StatsKey stats;
  @Getter
  private int playerId;
  @Getter
  @Setter
  private Object value;
  @Getter
  private StatsManager manager;

  public PlayerStatsAddEvent(StatsManager manager,StatsKey stats,Object value,int playerId){
	this.manager=manager;
    this.playerId=playerId;
    this.stats=stats;
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
