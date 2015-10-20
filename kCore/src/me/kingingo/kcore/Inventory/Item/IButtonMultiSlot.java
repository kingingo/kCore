package me.kingingo.kcore.Inventory.Item;

import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBase;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public abstract interface IButtonMultiSlot extends IButton{
	  public abstract ButtonBase[] getButtons();
	  public abstract boolean Clicked(int slot,Player player,ActionType type,Object object);
}
