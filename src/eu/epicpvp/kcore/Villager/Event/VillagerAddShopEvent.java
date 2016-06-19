package eu.epicpvp.kcore.Villager.Event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Merchant.Merchant;
import eu.epicpvp.kcore.Villager.VillagerShop;
import lombok.Getter;
import lombok.Setter;

public class VillagerAddShopEvent extends Event implements Cancellable{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private Merchant merchant;
  @Getter
  private ItemStack itemStack;
  @Getter
  private int slot;
  @Getter
  private VillagerShop shop;
  @Getter
  @Setter
  private boolean cancelled=false;

  public VillagerAddShopEvent(VillagerShop shop,Merchant merchant,ItemStack itemStack,int slot){
	  this.shop=shop;
	  this.merchant=merchant;
	  this.itemStack=itemStack;
	  this.slot=slot;
  }

  public HandlerList getHandlers(){
    return handlers;
  }

  public static HandlerList getHandlerList(){
    return handlers;
  }
}
