package eu.epicpvp.kcore.Inventory.Inventory;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.DeliveryPet.DeliveryPet;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.IButton;
import eu.epicpvp.kcore.Inventory.Item.IButtonOneSlot;
import eu.epicpvp.kcore.Util.UtilDebug;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;


public class DeliveryInventoryPage extends InventoryPageBase{

	public DeliveryPet delivery;
	private HashMap<Player,Long> click_time = new HashMap<Player,Long>();
	
	public DeliveryInventoryPage(int size, String title,DeliveryPet delivery) {
		super("DeliveryInventoryPage",size, title);
		this.delivery=delivery;
	}
	
	public boolean useButton(Player player,ActionType type,ItemStack item,int slot){
		if(click_time.containsKey(player)){
    		
    		if(click_time.get(player) <= System.currentTimeMillis()){
    			click_time.remove(player);
    		}else{
    			return true;
    		}
    		
    	}else{
    		click_time.put(player, System.currentTimeMillis() + 2000);
    	}
    		
		
		for(IButton button : this.getButtons()){
			if(button.isSlot(slot)){
				if(button instanceof IButtonOneSlot 
						&& (delivery.getObjects().containsKey(item.getItemMeta().getDisplayName())
								&&((IButtonOneSlot)button).getItemStack().getType() != delivery.getObjects().get(item.getItemMeta().getDisplayName()).getDelay_material())){
					if(((IButtonOneSlot)button).getItemStack().getType()==Material.JUKEBOX){	
						((IButtonOneSlot)button).Clicked(player, ActionType.R, ((IButtonOneSlot)button).getItemStack());
						return button.isCancelled();
					}else{
						if(UtilDebug.isDebug())UtilDebug.debug("DeliveryInv: UseButton", new String[]{"PLAYER: "+player.getName()});
						delivery.deliveryUSE(player, item.getItemMeta().getDisplayName(),false);
						break;
					}
				}
			}
		}
		
		return true;
	}
	
}