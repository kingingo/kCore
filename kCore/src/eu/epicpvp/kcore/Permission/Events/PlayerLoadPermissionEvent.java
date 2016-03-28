package eu.epicpvp.kcore.Permission.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Permission.PermissionManager;
import lombok.Getter;

public class PlayerLoadPermissionEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private PermissionManager manager;
  @Getter
  private Player player;

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