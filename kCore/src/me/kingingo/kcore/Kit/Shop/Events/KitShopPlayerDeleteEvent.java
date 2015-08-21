package me.kingingo.kcore.Kit.Shop.Events;

import lombok.Getter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class KitShopPlayerDeleteEvent extends Event{
	  private static final HandlerList handlers = new HandlerList();
	  @Getter
	  private Player player;
	  
	  public KitShopPlayerDeleteEvent(Player player){
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
