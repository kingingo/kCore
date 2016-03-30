package eu.epicpvp.kcore.Neuling.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Neuling.NeulingManager;
import lombok.Getter;

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
