package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.wolveringer.dataclient.gamestats.StatsKey;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;

public class ButtonUpDownVersus extends ButtonMultiCopy{
	
	public ButtonUpDownVersus(InventoryPageBase page,ItemStack item,int slot,StatsManager statsManager,StatsKey stats,int min,int max) {
		super(new ButtonForMultiButtonsCopy[]{new ButtonForMultiButtonsCopy(page,slot-InventorySize._9.getSize(),new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if( (stats==StatsKey.TEAM_MAX && statsManager.getInt( StatsKey.TEAM_MIN, player) <= statsManager.getInt( StatsKey.TEAM_MAX , player)) ||
						(stats==StatsKey.TEAM_MIN && statsManager.getInt( StatsKey.TEAM_MIN, player) < statsManager.getInt( StatsKey.TEAM_MAX , player)) ){
					if(player.getOpenInventory().getItem(slot).getAmount()<max&&player.getOpenInventory().getItem(slot).getAmount()>=min){
						player.getOpenInventory().getItem(slot).setAmount(player.getOpenInventory().getItem(slot).getAmount()+1);
						player.getOpenInventory().setItem(slot, player.getOpenInventory().getItem(slot));
						statsManager.setInt(player, player.getOpenInventory().getItem(slot).getAmount(), stats);
					}
				}
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.STONE_BUTTON), "§6+"),null),
		
		
		new ButtonForMultiButtonsCopy(page,slot+InventorySize._9.getSize(),new Click(){
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if( (stats==StatsKey.TEAM_MAX && statsManager.getInt( StatsKey.TEAM_MIN, player) < statsManager.getInt( StatsKey.TEAM_MAX , player)) ||
						(stats==StatsKey.TEAM_MIN && statsManager.getInt( StatsKey.TEAM_MIN, player) <= statsManager.getInt( StatsKey.TEAM_MAX , player)) ){
					if(player.getOpenInventory().getItem(slot).getAmount()<=max&&player.getOpenInventory().getItem(slot).getAmount()>min){
						player.getOpenInventory().getItem(slot).setAmount(player.getOpenInventory().getItem(slot).getAmount()-1);
						player.getOpenInventory().setItem(slot, player.getOpenInventory().getItem(slot));
						statsManager.setInt(player, player.getOpenInventory().getItem(slot).getAmount(), stats);
					}
				}
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.WOOD_BUTTON), "§6-"),null),
		new ButtonForMultiButtonsCopy(page, slot, null, item, new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(object instanceof InventoryPageBase){
					((InventoryPageBase)object).setItem(slot, item);
					((InventoryPageBase)object).getItem(slot).setAmount(statsManager.getInt(stats, player));
				}
			}
		}
		)});
	}
}
