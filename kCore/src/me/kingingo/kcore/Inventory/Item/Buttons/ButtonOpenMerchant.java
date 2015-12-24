package me.kingingo.kcore.Inventory.Item.Buttons;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Merchant.Merchant;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonOpenMerchant extends ButtonBase{

	public ButtonOpenMerchant(Merchant merchant,final ItemStack item) {
		super(new Click(){
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(object instanceof ItemStack){
					if( UtilItem.ItemNameEquals(((ItemStack)object), item) ){
						if(item.getType() == ((ItemStack)object).getType()){
							if(item.getDurability()==((ItemStack)object).getDurability()){
								Merchant m = merchant.clone();
								m.setCustomer(player);
								m.setTitle(merchant.getTitle());
								m.openTrading(player);
								m=null;
							}
						}
					}
				}
			}
			
		}, item);
	}
	
}
