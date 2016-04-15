package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;

public class ButtonUpDown extends ButtonMultiCopy{
	
	public ButtonUpDown(Click click,boolean wagerecht, InventoryPageBase page,ItemStack item,int slot,int min,int max) {
		super(new ButtonForMultiButtonsCopy[]{new ButtonForMultiButtonsCopy(page,(wagerecht ? slot+1:slot-InventorySize._9.getSize()),new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(player.getOpenInventory().getItem(slot).getAmount()<max&&player.getOpenInventory().getItem(slot).getAmount()>=min){
					player.getOpenInventory().getItem(slot).setAmount(player.getOpenInventory().getItem(slot).getAmount()+1);
					player.getOpenInventory().setItem(slot, player.getOpenInventory().getItem(slot));
					click.onClick(player, type, player.getOpenInventory().getItem(slot));
				}
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.STONE_BUTTON), "§6+"),null),
		
		
		new ButtonForMultiButtonsCopy(page,(wagerecht ? slot-1:slot+InventorySize._9.getSize()),new Click(){
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(player.getOpenInventory().getItem(slot).getAmount()<=max&&player.getOpenInventory().getItem(slot).getAmount()>min){
					player.getOpenInventory().getItem(slot).setAmount(player.getOpenInventory().getItem(slot).getAmount()-1);
					player.getOpenInventory().setItem(slot, player.getOpenInventory().getItem(slot));
					click.onClick(player, type, player.getOpenInventory().getItem(slot));
				}
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.WOOD_BUTTON), "§6-"),null),
		
		new ButtonForMultiButtonsCopy(page, slot, null, item, new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(object instanceof InventoryPageBase){
					item.setAmount(0);
					click.onClick(player, type, item);
					((InventoryPageBase)object).setItem(slot, item);
				}
			}
		}
		)});
	}
}
