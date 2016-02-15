package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBase;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.Gems;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryBuy extends InventoryPageBase{
	
	public InventoryBuy(final Click buyed,String Title,final Coins coins,final int c) {
		this(null,buyed, Title, coins, c, null, 0);
	}
	
	public InventoryBuy(final Click buyed,String Title,final Gems gems,final int g) {
		this(null,buyed, Title,null,0 ,gems, g);
	}
	
	public InventoryBuy(ItemStack i,final Click buyed,String Title,final Coins coins,final int c) {
		this(i,buyed, Title, coins, c, null, 0);
	}
	
	public InventoryBuy(ItemStack i,final Click buyed,String Title,final Gems gems,final int g) {
		this(i,buyed, Title,null,0 ,gems, g);
	}
	
	public InventoryBuy(final Click buyed,String Title,final Coins coins,final int c,final Gems gems,final int g) {
		this(null,buyed, Title,coins,c ,gems, g);
	}
	
	public InventoryBuy(ItemStack i,final Click buyed,String Title,final Coins coins,final int c,final Gems gems,final int g) {
		super("InventoryBuy",9,Title);
		int c_slot=2;
		int g_slot=6;
		
		if(i!=null){
			setItem(1, i);
			c_slot=3;
			g_slot=7;
		}
		
		if(coins!=null){
			addButton( (gems==null?4:c_slot) ,new ButtonBase(new Click(){

				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(!coins.delCoinsWithScoreboardUpdate(player, true, c)){
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NOT_ENOUGH_COINS"));
					}else{
						buyed.onClick(player, type,null);
					}
					player.closeInventory();
				}
					
			},Material.GOLD_INGOT,"§e"+c+" Coins"));
		}
			
		if(gems!=null){
			addButton((coins==null?4:g_slot),new ButtonBase(new Click(){

				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(!gems.delGemsWithScoreboardUpdate(player, true, g)){
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NOT_ENOUGH_GEMS"));
					}else{
						buyed.onClick(player, type,null);
					}
					player.closeInventory();
				}
					
			},Material.EMERALD,"§a"+g+" Gems"));
		}
		
		fill(Material.STAINED_GLASS_PANE,7);
	}

}
