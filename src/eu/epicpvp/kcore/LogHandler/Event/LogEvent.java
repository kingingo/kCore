package eu.epicpvp.kcore.LogHandler.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;

public class LogEvent extends Event{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private String message;
  
  public LogEvent(String message){
	  this.message=message;
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
