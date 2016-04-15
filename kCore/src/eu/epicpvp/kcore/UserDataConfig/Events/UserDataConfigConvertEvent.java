package eu.epicpvp.kcore.UserDataConfig.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class UserDataConfigConvertEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private kConfig config;
  @Getter
  private int playerId;

  public UserDataConfigConvertEvent(kConfig config,int playerId)
  {
	this.playerId=playerId;
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
