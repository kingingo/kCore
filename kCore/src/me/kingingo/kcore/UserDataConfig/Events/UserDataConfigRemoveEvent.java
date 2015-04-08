package me.kingingo.kcore.UserDataConfig.Events;

import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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
