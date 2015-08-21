package me.kingingo.kcore.Villager.Event;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Villager.VillagerShop;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VillagerShopEvent extends Event implements Cancellable{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private Player player;
  @Getter
  private VillagerShop shop;
  @Getter
  @Setter
  private boolean cancelled=false;

  public VillagerShopEvent(Player player,VillagerShop shop){
	  this.player=player;
	  this.shop=shop;
  }

  public HandlerList getHandlers(){
    return handlers;
  }

  public static HandlerList getHandlerList(){
    return handlers;
  }
}
