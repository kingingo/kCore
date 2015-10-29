package me.kingingo.kcore.Inventory.Item.Buttons;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Util.InventorySplit;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonUpDownStats extends ButtonMultiCopy{
	
	public ButtonUpDownStats(InventoryPageBase page,ItemStack item,int slot,StatsManager statsManager,Stats stats) {
		super(new ButtonForMultiButtonsCopy[]{new ButtonForMultiButtonsCopy(page,slot-InventorySplit._9.getMax(),new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.getOpenInventory().getItem(slot).setAmount(player.getOpenInventory().getItem(slot).getAmount()+1);
				player.getOpenInventory().setItem(slot, player.getOpenInventory().getItem(slot));
				statsManager.addInt(player, 1, stats);
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.STONE_BUTTON), "§a§l+"),null),
		
		
		new ButtonForMultiButtonsCopy(page,slot+InventorySplit._9.getMax(),new Click(){
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.getOpenInventory().getItem(slot).setAmount(player.getOpenInventory().getItem(slot).getAmount()-1);
				player.getOpenInventory().setItem(slot, player.getOpenInventory().getItem(slot));
				statsManager.delInt(player, 1, stats);
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.WOOD_BUTTON), "§c§l-"),null),
		new ButtonForMultiButtonsCopy(page, slot, null, item, new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.getOpenInventory().setItem(slot, item);
				player.getOpenInventory().getItem(slot).setAmount(statsManager.getInt(stats, player));
			}
		}
		)});
	}
}
