package me.kingingo.kcore.Inventory.Item.Buttons;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonSwitch extends ButtonMultiCopy{
	
	public ButtonSwitch(Click click,boolean wagerecht, InventoryPageBase page,ItemStack[] items,int slot) {
		super(new ButtonForMultiButtonsCopy[]{new ButtonForMultiButtonsCopy(page,(wagerecht ? slot+1:slot-InventorySize._9.getSize()),new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(player.getOpenInventory().getItem(slot).getAmount()<items.length){
					int a = player.getOpenInventory().getItem(slot).getAmount()+1;
					player.getOpenInventory().setItem(slot, items[a]);
					player.getOpenInventory().getItem(slot).setAmount(a);
					click.onClick(player, type, player.getOpenInventory().getItem(slot));
				}
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.STONE_BUTTON), "§6+"),null),
		
		
		new ButtonForMultiButtonsCopy(page,(wagerecht ? slot-1:slot+InventorySize._9.getSize()),new Click(){
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(player.getOpenInventory().getItem(slot).getAmount()<=items.length){
					int a = player.getOpenInventory().getItem(slot).getAmount()-1;
					player.getOpenInventory().setItem(slot, items[a]);
					player.getOpenInventory().getItem(slot).setAmount(a);
					click.onClick(player, type, player.getOpenInventory().getItem(slot));
				}
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.WOOD_BUTTON), "§6-"),null),
		
		new ButtonForMultiButtonsCopy(page, slot, null, items[0], new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(object instanceof InventoryPageBase){
					click.onClick(player, type, null);
				}
			}
		}
		)});
	}
}
