package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;

public class ButtonTeleport extends ButtonBase{

	public ButtonTeleport(final ItemStack item,Location loc) {
		super(new Click(){
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(object instanceof ItemStack){
					if( UtilItem.ItemNameEquals(((ItemStack)object), item) ){
						if(item.getType() == ((ItemStack)object).getType()){
							if(item.getDurability()==((ItemStack)object).getDurability()){
								player.closeInventory();
								player.teleport(loc);
							}
						}
					}
				}
			}
			
		}, item);
	}

}
