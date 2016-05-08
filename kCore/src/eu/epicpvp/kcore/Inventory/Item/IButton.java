package eu.epicpvp.kcore.Inventory.Item;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;

public interface IButton extends Cloneable{
  public void remove();
  public InventoryPageBase getInventoryPageBase();
  public void setInventoryPageBase(InventoryPageBase inv);
  public boolean isSlot(int slot);
  public void setCancelled(boolean b);
  public boolean isCancelled();
  public IButton clone();
}
