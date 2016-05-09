package eu.epicpvp.kcore.MysteryChest.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.MysteryChest.MysteryChest;
import lombok.Getter;

public class PlayerUseMysteryChestEvent extends Event{
	  private static final HandlerList handlers = new HandlerList();
	  @Getter
	  private Player player;
	  @Getter
	  private MysteryChest chest;
	  
	  public PlayerUseMysteryChestEvent(Player player,MysteryChest chest){
		  this.player=player;
		  this.chest=chest;
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
