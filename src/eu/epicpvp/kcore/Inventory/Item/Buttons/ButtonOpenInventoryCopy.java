package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import lombok.Getter;

public class ButtonOpenInventoryCopy extends ButtonBase{

	@Getter
	private InventoryCopy inventorySale;
	
	public ButtonOpenInventoryCopy(final InventoryCopy inv,InventoryBase base,final ItemStack item) {
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
