package me.kingingo.kcore.NeulingManager.Events;

import lombok.Getter;
import me.kingingo.kcore.NeulingManager.NeulingManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NeulingEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private Player player;
  @Getter
  private NeulingManager manager;

  public NeulingEvent(Player player,NeulingManager manager)
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
