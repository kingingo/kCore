package me.kingingo.kcore.Permission.Event;

import lombok.Getter;
import me.kingingo.kcore.Permission.PermissionManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLoadPermissionEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private Player player;
  @Getter
  private PermissionManager manager;

  public PlayerLoadPermissionEvent(PermissionManager manager,Player player){
	this.player=player;
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
