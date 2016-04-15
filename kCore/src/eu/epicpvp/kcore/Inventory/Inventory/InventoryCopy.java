package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Inventory.InventoryBase;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.IButton;
import eu.epicpvp.kcore.Inventory.Item.IButtonMultiSlot;
import eu.epicpvp.kcore.Inventory.Item.IButtonOneSlot;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonForMultiButtons;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonForMultiButtonsCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonMultiCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesPackageBase;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import lombok.Getter;
import lombok.Setter;

public class InventoryCopy extends InventoryPageBase{

	@Getter
	@Setter
	private boolean create_new_inv=false;
	@Getter
	@Setter
	private boolean for_with_copy_page = true;
	
	public InventoryCopy(int size, String title) {
		super(size, title);
	}

	public void open(Player player,InventoryBase base){
		if(create_new_inv){
			InventoryPageBase page = clone();
			
			base.addAnother(page);
			player.openInventory(page);
			SalesPackageBase sale;
			ButtonCopy c;
			ButtonMultiCopy cc;
			for(IButton b : (for_with_copy_page ? page.getButtons() : getButtons())){
				if(b instanceof IButtonOneSlot){
					if(b instanceof SalesPackageBase){
						sale = (SalesPackageBase) b;
						if(sale.getItemStack()!=null&&sale.getPermission()!=null&&player.hasPermission(sale.getPermission().getPermissionToString())){
							sale.setItemStack(UtilItem.addEnchantmentGlow(sale.getItemStack()));
							sale.refreshItemStack();
						}
					}else if(b instanceof ButtonCopy){
						c = (ButtonCopy)b;
						c.getSet().onClick(player, ActionType.AIR, page);
					}
				}else if(b instanceof IButtonMultiSlot){
					if(b instanceof ButtonMultiCopy){
						cc = (ButtonMultiCopy)b;
						cc.setRemove(false);
						cc.setInventoryPageBase(page);
						ButtonForMultiButtonsCopy ccb;
						for (ButtonForMultiButtons cb : cc.getButtons()) {
							cb.setInventoryPageBase(page);
							if(cb instanceof ButtonForMultiButtonsCopy){
								ccb=(ButtonForMultiButtonsCopy)cb;
								if(ccb.getSet()!=null)ccb.getSet().onClick(player, ActionType.AIR, page);
							}
						}
					}
				}
			}
		}else{
			player.openInventory(this);
		}
	}
	
}
