package me.kingingo.kcore.StatsManager.Event;

import lombok.Getter;
import me.kingingo.kcore.StatsManager.Stats;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerStatsChangeEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private Stats stats;
  @Getter
  private Player player;

  public PlayerStatsChangeEvent(Stats s,Player p)
  {
    this.player=p;
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
