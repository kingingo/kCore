package me.kingingo.kcore.Inventory.Item;

import me.kingingo.kcore.Inventory.InventoryPageBase;

public abstract interface IButton{
  public abstract void remove();
  public abstract InventoryPageBase getInventoryPageBase();
  public abstract void setInventoryPageBase(InventoryPageBase inv);
  public abstract boolean isSlot(int slot);
  public abstract void setCancelled(boolean b);
  public abstract boolean isCancelled();
}
