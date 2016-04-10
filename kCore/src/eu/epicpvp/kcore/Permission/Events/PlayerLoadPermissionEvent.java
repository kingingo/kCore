package eu.epicpvp.kcore.Permission.Events;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.PermissionPlayer;
import lombok.Getter;

public class PlayerLoadPermissionEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private PermissionManager manager;
  @Getter
  private Player player;
  @Getter
  private PermissionPlayer permissionPlayer;

  public PlayerLoadPermissionEvent(PermissionManager manager,PermissionPlayer permissionPlayer){
	this.player=permissionPlayer.getPlayer();
	this.permissionPlayer=permissionPlayer;
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