package me.kingingo.kcore.Inventory.Item.Buttons;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonUpDownVersus extends ButtonMultiCopy{
	
	public ButtonUpDownVersus(InventoryPageBase page,ItemStack item,int slot,StatsManager statsManager,Stats stats,int min,int max) {
		super(new ButtonForMultiButtonsCopy[]{new ButtonForMultiButtonsCopy(page,slot-InventorySize._9.getSize(),new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if( (stats==Stats.TEAM_MAX && statsManager.getInt( Stats.TEAM_MIN, player) <= statsManager.getInt( Stats.TEAM_MAX , player)) ||
						(stats==Stats.TEAM_MIN && statsManager.getInt( Stats.TEAM_MIN, player) < statsManager.getInt( Stats.TEAM_MAX , player)) ){
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
				if( (stats==Stats.TEAM_MAX && statsManager.getInt( Stats.TEAM_MIN, player) < statsManager.getInt( Stats.TEAM_MAX , player)) ||
						(stats==Stats.TEAM_MIN && statsManager.getInt( Stats.TEAM_MIN, player) <= statsManager.getInt( Stats.TEAM_MAX , player)) ){
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
