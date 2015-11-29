package me.kingingo.kcore.TeleportManager.Events;

import lombok.Getter;
import me.kingingo.kcore.TeleportManager.Teleporter;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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