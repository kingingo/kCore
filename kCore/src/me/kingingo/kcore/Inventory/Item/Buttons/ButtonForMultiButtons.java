package me.kingingo.kcore.Inventory.Item.Buttons;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;

import org.bukkit.inventory.ItemStack;

public class ButtonForMultiButtons extends ButtonBase{

	public ButtonForMultiButtons(InventoryPageBase page,int slot,Click click, ItemStack item) {
		super(click, item);
		setSlot(slot);
		page.setItem(slot, item);
	}

}
