package me.kingingo.kcore.Inventory.Item.Buttons;

import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;

import org.bukkit.inventory.ItemStack;

public class ButtonForMultiButtonsCopy extends ButtonForMultiButtons{

	@Getter
	public Click set;
	
	public ButtonForMultiButtonsCopy(InventoryPageBase page, int slot,Click click, ItemStack item,Click set) {
		super(page, slot, click, item);
		this.set=set;
	}


}
