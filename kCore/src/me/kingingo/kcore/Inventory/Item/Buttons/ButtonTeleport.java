package me.kingingo.kcore.Inventory.Item.Buttons;

import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

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
