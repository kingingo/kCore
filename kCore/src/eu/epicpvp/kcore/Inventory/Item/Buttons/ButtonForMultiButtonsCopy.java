package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;

public class ButtonForMultiButtonsCopy extends ButtonForMultiButtons{

	@Getter
	public Click set;
	
	public ButtonForMultiButtonsCopy(InventoryPageBase page, int slot,Click click, ItemStack item,Click set) {
		super(page, slot, click, item);
		this.set=set;
	}


}
