package me.kingingo.kcore.StatsManager.Event;

import lombok.Getter;
import me.kingingo.kcore.StatsManager.StatsManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerStatsCreateEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private StatsManager manager;
  @Getter
  private Player player;

  public PlayerStatsCreateEvent(StatsManager manager,Player p)
  {
    this.player=p;
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
