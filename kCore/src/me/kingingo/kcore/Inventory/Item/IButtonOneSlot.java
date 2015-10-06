package me.kingingo.kcore.Inventory.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract interface IButtonOneSlot extends IButton{
	  public abstract String getName();
	  public abstract void setName(String name);
	  public abstract void setDescription(String[] desc);
	  public abstract String[] getDescription();
	  public abstract void refreshItemStack();
	  public abstract void setMaterial(Material material);
	  public abstract void setMaterial(Material material,byte data);
	  public abstract int getSlot();
	  public abstract void setSlot(int slot);
	  public abstract ItemStack getItemStack();
	  public abstract void setItemStack(ItemStack item);
	}
