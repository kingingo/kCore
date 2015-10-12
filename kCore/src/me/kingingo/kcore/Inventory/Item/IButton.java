package me.kingingo.kcore.Inventory.Item;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract interface IButton{
  public abstract void remove();
  public abstract InventoryPageBase getInventoryPageBase();
  public abstract void setInventoryPageBase(InventoryPageBase inv);
  public abstract void Clicked(Player player,ActionType type,Object object);
  public abstract Click getClick();
  public abstract boolean isSlot(int slot);
  public abstract void setCancelled(boolean b);
  public abstract boolean isCancelled();
}
