package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.DeliveryPet.DeliveryPet;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.IButton;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class DeliveryInventoryPage extends InventoryPageBase{

	public DeliveryPet delivery;
	
	public DeliveryInventoryPage(int size, String title,DeliveryPet delivery) {
		super(size, title);
		this.delivery=delivery;
	}
	
	public void useButton(Player player,ActionType type,ItemStack item,int slot){
		for(IButton button : this.getButtons()){
			if(slot==button.getSlot() ){
				delivery.deliverlyUSE(player, (!item.hasItemMeta()?"ITEMMETA NULL":(item.getItemMeta().hasDisplayName()?item.getItemMeta().getDisplayName():"DISPLAYNAME NULL")));
				break;
			}
		}
	}
	
}