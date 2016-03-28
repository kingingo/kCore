package eu.epicpvp.kcore.Permission.Events;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Permission.PermissionManager;
import lombok.Getter;

public class GroupLoadedEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private PermissionManager manager;
  @Getter
  private String group;

  public GroupLoadedEvent(PermissionManager manager,String group){
	this.group=group;
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