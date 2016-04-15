package eu.epicpvp.kcore.UpdateAsync.Event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.UpdateAsync.UpdateAsyncType;

public class UpdateAsyncEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  private UpdateAsyncType _type;

  public UpdateAsyncEvent(UpdateAsyncType example)
  {
    this._type = example;
  }

  public UpdateAsyncType getType()
  {
    return this._type;
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
