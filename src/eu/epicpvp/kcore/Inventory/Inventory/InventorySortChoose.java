package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;

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
