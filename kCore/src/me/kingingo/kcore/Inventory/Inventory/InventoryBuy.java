package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.Tokens;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryBuy extends InventoryPageBase{
	
	public InventoryBuy(final Click buyed,String Title,final Coins coins,final int c,final Tokens tokens,final int t) {
		super(9,Title);
		addButton(2,new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				if(!coins.delCoins(player, true, c)){
					player.sendMessage(Text.PREFIX.getText()+"§cDu hast nicht genug Coins!");
				}else{
					buyed.onClick(player, type,null);
				}
				player.closeInventory();
			}
			
		},Material.GOLD_NUGGET,"§aCoins"));
		
		addButton(6,new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				if(!tokens.delTokens(player, true, t)){
					player.sendMessage(Text.PREFIX.getText()+"§cDu hast nicht genug Tokens!");
				}else{
					buyed.onClick(player, type,null);
				}
				player.closeInventory();
			}
			
		},Material.GOLD_INGOT,"§aTokens"));
		fill(Material.STAINED_GLASS_PANE,7);
	}
	
	public InventoryBuy(final Click buyed,String Title,final Tokens tokens,final int t) {
		super(9,Title);
		
		addButton(4,new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				if(!tokens.delTokens(player, true, t)){
					player.sendMessage(Text.PREFIX.getText()+"§cDu hast nicht genug Tokens!");
				}else{
					buyed.onClick(player, type,null);
				}
				player.closeInventory();
			}
			
		},Material.GOLD_INGOT,"§aTokens"));
		fill(Material.STAINED_GLASS_PANE,7);
	}
	
	public InventoryBuy(final Click buyed,String Title,final Coins coins,final int c) {
		super(9,Title);
		addButton(4,new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				if(!coins.delCoins(player, true, c)){
					player.sendMessage(Text.PREFIX.getText()+"§cDu hast nicht genug Coins!");
				}else{
					buyed.onClick(player, type,null);
				}
				player.closeInventory();
			}
			
		},Material.GOLD_NUGGET,"§aCoins"));
		fill(Material.STAINED_GLASS_PANE,7);
	}

}
