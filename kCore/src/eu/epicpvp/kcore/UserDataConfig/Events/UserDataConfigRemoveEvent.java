package eu.epicpvp.kcore.UserDataConfig.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class UserDataConfigRemoveEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private kConfig config;
  @Getter
  private Player player;

  public UserDataConfigRemoveEvent(kConfig config,Player player)
  {
	this.player=player;
    this.config=config;
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
