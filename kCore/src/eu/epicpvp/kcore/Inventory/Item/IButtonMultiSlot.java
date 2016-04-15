package eu.epicpvp.kcore.Inventory.Item;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;


public abstract interface IButtonMultiSlot extends IButton{
	  public abstract ButtonBase[] getButtons();
	  public abstract boolean Clicked(int slot,Player player,ActionType type,Object object);
}
