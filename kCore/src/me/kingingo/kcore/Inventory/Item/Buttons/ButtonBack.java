package me.kingingo.kcore.Inventory.Item.Buttons;

import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonBack extends ButtonBase{

	public ButtonBack(final InventoryPageBase page, ItemStack item) {
		super(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(page);
			}
			
		}, item);
	}

}
