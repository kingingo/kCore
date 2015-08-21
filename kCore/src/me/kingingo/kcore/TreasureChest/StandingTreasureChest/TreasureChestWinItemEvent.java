package me.kingingo.kcore.TreasureChest.StandingTreasureChest;

import lombok.Getter;
import me.kingingo.kcore.StatsManager.Stats;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

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
