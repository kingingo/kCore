package me.kingingo.kcore.Inventory.Item;

import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract interface IButton{
  public abstract ItemStack getItemStack();
  public abstract void Clicked(Player player,ActionType type);
  public abstract String getName();
  public abstract String[] getDescription();
}
