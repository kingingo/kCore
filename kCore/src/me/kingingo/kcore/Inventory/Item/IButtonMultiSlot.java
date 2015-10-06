package me.kingingo.kcore.Inventory.Item;


public abstract interface IButtonMultiSlot extends IButton{
	  public abstract Integer[] getSlots();
	  public abstract void setSlots(Integer[] slot);
}
