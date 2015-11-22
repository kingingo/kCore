package me.kingingo.kcore.Permission.Event;

import lombok.Getter;
import me.kingingo.kcore.Permission.PermissionManager;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GroupLoadedEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private String group;
  @Getter
  private PermissionManager manager;

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
