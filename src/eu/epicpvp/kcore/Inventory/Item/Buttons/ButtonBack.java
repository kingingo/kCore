package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;

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
