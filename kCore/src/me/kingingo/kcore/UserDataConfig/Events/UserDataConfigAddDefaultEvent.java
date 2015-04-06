package me.kingingo.kcore.UserDataConfig.Events;

import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserDataConfigAddDefaultEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private kConfig config;
  @Getter
  private UUID uuid;

  public UserDataConfigAddDefaultEvent(kConfig config,UUID uuid)
  {
	this.uuid=uuid;
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
