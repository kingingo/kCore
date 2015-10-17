package me.kingingo.kcore.Inventory.Inventory;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.IButton;
import me.kingingo.kcore.Inventory.Item.Buttons.SalesPackageBase;
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
			for(IButton b : page.getButtons()){
				if(b instanceof SalesPackageBase){
					SalesPackageBase sale = (SalesPackageBase) b;
					if(sale.getItemStack()!=null&&sale.getPermission()!=null&&player.hasPermission(sale.getPermission().getPermissionToString())){
						sale.setItemStack(UtilItem.addEnchantmentGlow(sale.getItemStack()));
						sale.refreshItemStack();
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
