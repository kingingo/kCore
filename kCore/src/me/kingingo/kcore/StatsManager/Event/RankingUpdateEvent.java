package me.kingingo.kcore.StatsManager.Event;

import lombok.Getter;
import me.kingingo.kcore.StatsManager.Ranking;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RankingUpdateEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private Ranking ranking;

  public RankingUpdateEvent(Ranking ranking)
  {
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
