package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class InventorySortChoose extends InventoryPageBase{
	
	public InventorySortChoose(Click click,String Title,int size,ItemStack[] item) {
		super("InventorySortChoose",size,Title);
		for(ItemStack i : item){
			if(i==null||i.getType()==Material.AIR)continue;
			addButton(getSlotSort(), new ButtonBase(click,i));
		}
		fill(Material.STAINED_GLASS_PANE,7);
	}
}
