package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InventoryChoose extends InventoryPageBase{
	
	public InventoryChoose(Click click,String Title,int size,ItemStack[] item) {
		super(size,Title);
		for(ItemStack i : item){
			addButton(getSlotSort(), new ButtonBase(click,i.getType(),i.getDurability(),i.getItemMeta().getDisplayName()));
		}
		fill(Material.STAINED_GLASS_PANE,7);
	}

}
