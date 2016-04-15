package eu.epicpvp.kcore.TreasureChest.StandingTreasureChest;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;

public class TreasureChestWinItemEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  @Getter
  private Player player;
  @Getter
  private ItemStack item;

  public TreasureChestWinItemEvent(Player player,ItemStack item){
    this.player=player;
    this.item=item;
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
