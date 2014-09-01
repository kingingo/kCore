package me.kingingo.kcore.AntiLogout.Events;

import lombok.Getter;
import me.kingingo.kcore.AntiLogout.AntiLogoutManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AntiLogoutAddPlayerEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private Player player;
  @Getter
  private AntiLogoutManager manager;

  public AntiLogoutAddPlayerEvent(Player player,AntiLogoutManager manager)
  {
	this.manager=manager;
    this.player=player;
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
