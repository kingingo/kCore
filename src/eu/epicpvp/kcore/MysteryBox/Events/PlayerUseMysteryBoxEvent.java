package eu.epicpvp.kcore.MysteryBox.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.MysteryBox.MysteryBox;
import lombok.Getter;

public class PlayerUseMysteryBoxEvent extends Event{
	  private static final HandlerList handlers = new HandlerList();
	  @Getter
	  private Player player;
	  @Getter
	  private MysteryBox chest;
	  
	  public PlayerUseMysteryBoxEvent(Player player,MysteryBox chest){
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
