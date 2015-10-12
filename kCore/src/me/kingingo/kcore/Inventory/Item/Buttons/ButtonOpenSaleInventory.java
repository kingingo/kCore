package me.kingingo.kcore.Inventory.Item.Buttons;

import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventorySale;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonOpenSaleInventory extends ButtonBase{

	@Getter
	private InventorySale inventorySale;
	
	public ButtonOpenSaleInventory(final InventorySale inv,InventoryBase base,final ItemStack item) {
		super(new Click(){
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(object instanceof ItemStack){
					if( UtilItem.ItemNameEquals(((ItemStack)object), item) ){
						if(item.getType() == ((ItemStack)object).getType()){
							if(item.getDurability()==((ItemStack)object).getDurability()){
								inv.open(player, base);
							}
						}
					}
				}
			}
			
		}, item);
		this.inventorySale=inv;
	}

}
