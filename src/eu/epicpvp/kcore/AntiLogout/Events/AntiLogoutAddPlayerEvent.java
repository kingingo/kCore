package eu.epicpvp.kcore.AntiLogout.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.AntiLogout.AntiLogoutManager;
import lombok.Getter;

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
