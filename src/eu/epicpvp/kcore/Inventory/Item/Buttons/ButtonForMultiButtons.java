package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;

public class ButtonForMultiButtons extends ButtonBase{

	public ButtonForMultiButtons(InventoryPageBase page,int slot,Click click, ItemStack item) {
		super(click, item);
		setSlot(slot);
		page.setItem(slot, item);
	}
	
	public void setInventoryPageBase(InventoryPageBase page){
		page.setItem(getSlot(), getItemStack());
	}

}
