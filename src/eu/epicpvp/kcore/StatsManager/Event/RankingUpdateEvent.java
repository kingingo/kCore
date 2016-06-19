package eu.epicpvp.kcore.StatsManager.Event;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.StatsManager.Ranking;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import lombok.Getter;

public class RankingUpdateEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private Ranking ranking;
  @Getter
  private StatsManager manager;

  public RankingUpdateEvent(StatsManager manager,Ranking ranking)
  {
	this.manager=manager;
    this.ranking=ranking;
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