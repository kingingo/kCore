package eu.epicpvp.kcore.Achievements.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import lombok.Getter;

public class PlayerLoadAchievementsEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private Player player;
  @Getter
  private Achievement achievement;

  public PlayerLoadAchievementsEvent(Player player,Achievement achievement)
  {
	this.achievement=achievement;
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
