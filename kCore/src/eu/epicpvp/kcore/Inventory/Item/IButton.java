package eu.epicpvp.kcore.Inventory.Item;

import org.apache.commons.lang3.SerializationUtils;

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
