package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Merchant.Merchant;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;

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
