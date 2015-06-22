package me.kingingo.kcore.Inventory.Item;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonOpenInventory extends ButtonBase{

	public ButtonOpenInventory(final InventoryPageBase inv,final ItemStack item) {
		super(new Click(){
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(object instanceof ItemStack){
					if( UtilItem.ItemNameEquals(((ItemStack)object), item) ){
						if(item.getType() == ((ItemStack)object).getType()){
							if(item.getDurability()==((ItemStack)object).getDurability()){
								player.openInventory(inv);
							}
						}
					}
				}
			}
			
		}, item);
	}

}
