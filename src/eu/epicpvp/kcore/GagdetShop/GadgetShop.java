package eu.epicpvp.kcore.GagdetShop;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;

public class GadgetShop extends InventoryCopy{
	
	public GadgetShop(GadgetHandler handler){
		super(InventorySize._36.getSize(),"Gadget Shop");
		setCreate_new_inv(true);
		
		int next = 0;
		for(int slot : UtilInv.getSlotsBorder(InventorySize.invSize(getSize()), InventorySplit._18)){
			if(handler.getGadgets().size() <= next){
				setItem(slot, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), " "));
			}else{
				addButton(slot, new GadgetButton(slot, handler.getGadgets().get(next)));
			}
			next++;
		}
		
		fill(Material.STAINED_GLASS_PANE, 7);
		UtilInv.getBase().addPage(this);
	}
	
}
