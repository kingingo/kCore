package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.wolveringer.dataclient.gamestats.StatsKey;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;

public class InventoryBuy extends InventoryPageBase{
	
	public InventoryBuy(final Click buyed,String Title,final StatsManager stats,final int g,final int c) {
		this(null,buyed, Title,stats, g, c);
	}
	
	public InventoryBuy(ItemStack i,final Click buyed,String Title,final StatsManager stats,final int g,final int c) {
		super("InventoryBuy",9,Title);
		int c_slot=2;
		int g_slot=6;
		
		if(i!=null){
			setItem(1, i);
			c_slot=3;
			g_slot=7;
		}
		
		if(c!=0){
			addButton( (g==0?4:c_slot) ,new ButtonBase(new Click(){

				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(stats.getInt(player, StatsKey.COINS) < c){
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NOT_ENOUGH_COINS"));
					}else{
						stats.add(player, StatsKey.COINS, -c);
						buyed.onClick(player, type,null);
					}
					player.closeInventory();
				}
					
			},Material.GOLD_INGOT,"\u00A75e"+c+" Coins"));
		}
			
		if(g!=0){
			addButton((c==0?4:g_slot),new ButtonBase(new Click(){

				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(stats.getInt(player, StatsKey.GEMS) < g){
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NOT_ENOUGH_GEMS"));
					}else{
						stats.add(player, StatsKey.GEMS, -g);
						buyed.onClick(player, type,null);
					}
					player.closeInventory();
				}
					
			},Material.EMERALD,"\u00A75a"+g+" Gems"));
		}
		
		fill(Material.STAINED_GLASS_PANE,7);
	}

}
