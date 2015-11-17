package me.kingingo.kcore.Inventory.Inventory;

import java.util.HashMap;

import me.kingingo.kcore.DeliveryPet.DeliveryPet;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.IButton;
import me.kingingo.kcore.Inventory.Item.IButtonOneSlot;
import me.kingingo.kcore.Util.UtilDebug;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


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
				if(button instanceof IButtonOneSlot && ((IButtonOneSlot)button).getItemStack().getType() != Material.REDSTONE_BLOCK){
					if(((IButtonOneSlot)button).getItemStack().getType()==Material.JUKEBOX){	
						((IButtonOneSlot)button).Clicked(player, ActionType.R, ((IButtonOneSlot)button).getItemStack());
						return button.isCancelled();
					}else{
						if(UtilDebug.isDebug())UtilDebug.debug("DeliveryInv: UseButton", new String[]{"PLAYER: "+player.getName()});
						delivery.deliveryUSE(player, item.getType(),false);
						break;
					}
				}
			}
		}
		
		return true;
	}
	
}