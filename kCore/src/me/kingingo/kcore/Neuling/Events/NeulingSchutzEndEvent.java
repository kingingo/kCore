package me.kingingo.kcore.Neuling.Events;

import lombok.Getter;
import me.kingingo.kcore.Neuling.NeulingManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NeulingSchutzEndEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private Player player;
  @Getter
  private NeulingManager manager;

  public NeulingSchutzEndEvent(Player player,NeulingManager manager)
  {
	this.manager=manager;
    this.player=player;
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
