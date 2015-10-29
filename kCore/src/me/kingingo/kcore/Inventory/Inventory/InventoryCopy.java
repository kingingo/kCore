package me.kingingo.kcore.Inventory.Inventory;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.IButton;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonCopy;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonForMultiButtons;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonForMultiButtonsCopy;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonMultiCopy;
import me.kingingo.kcore.Inventory.Item.Buttons.SalesPackageBase;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.entity.Player;

public class InventoryCopy extends InventoryPageBase{

	@Getter
	@Setter
	private boolean create_new_inv=false;
	
	public InventoryCopy(int size, String title) {
		super(size, title);
	}

	public void open(Player player,InventoryBase base){
		if(create_new_inv){
			InventoryPageBase page = clone();
			SalesPackageBase sale;
			ButtonCopy c;
			ButtonMultiCopy cc;
			for(IButton b : page.getButtons()){
				if(b instanceof SalesPackageBase){
					sale = (SalesPackageBase) b;
					if(sale.getItemStack()!=null&&sale.getPermission()!=null&&player.hasPermission(sale.getPermission().getPermissionToString())){
						sale.setItemStack(UtilItem.addEnchantmentGlow(sale.getItemStack()));
						sale.refreshItemStack();
					}
				}else if(b instanceof ButtonCopy){
					c = (ButtonCopy)b;
					c.getSet().onClick(player, ActionType.AIR, page);
				}else if(b instanceof ButtonMultiCopy){
					cc = (ButtonMultiCopy)b;
					ButtonForMultiButtonsCopy ccb;
					for (ButtonForMultiButtons cb : cc.getButtons()) {
						if(cb instanceof ButtonForMultiButtonsCopy){
							ccb=(ButtonForMultiButtonsCopy)cb;
							ccb.getSet().onClick(player, ActionType.AIR, page);
						}
					}
				}
			}
			
			player.openInventory(page);
			base.addAnother(page);
		}else{
			player.openInventory(this);
		}
	}
	
}
