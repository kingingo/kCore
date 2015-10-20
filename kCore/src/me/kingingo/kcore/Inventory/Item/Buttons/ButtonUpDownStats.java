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

public class ButtonUpDownStats extends ButtonMultiSlotBase{
	
	public ButtonUpDownStats(InventoryPageBase page,ItemStack item,int slot,StatsManager statsManager,Stats stats) {
		super(new ButtonForMultiButtons[]{new ButtonForMultiButtons(page,slot-InventorySplit._9.getMax(),new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				page.getItem(slot).setAmount(page.getItem(slot).getAmount()+1);
				page.setItem(slot, page.getItem(slot));
				statsManager.addInt(player, 1, stats);
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.STONE_BUTTON), "§a§l+")),new ButtonForMultiButtons(page,slot+InventorySplit._9.getMax(),new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				page.getItem(slot).setAmount(page.getItem(slot).getAmount()-1);
				page.setItem(slot, page.getItem(slot));
				statsManager.delInt(player, 1, stats);
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.WOOD_BUTTON), "§c§l-"))},page);
		
		page.setItem(slot, item);
	}
}
