package eu.epicpvp.kcore.TeleportManager.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.TeleportManager.Teleporter;
import lombok.Getter;

public class PlayerTeleportedEvent extends Event
{
	  private static final HandlerList handlers = new HandlerList();
	  @Getter
	  private Teleporter teleporter;

	  public PlayerTeleportedEvent(Teleporter teleporter)
	  {
	    this.teleporter=teleporter;
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