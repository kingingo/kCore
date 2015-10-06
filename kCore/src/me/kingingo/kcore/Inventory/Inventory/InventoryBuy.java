package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBase;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryBuy extends InventoryPageBase{
	
	public InventoryBuy(final Click buyed,String Title,final Coins coins,final int c) {
		super("InventoryBuy",9,Title);
		addButton(4,new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type,Object object) {
				if(!coins.delCoins(player, true, c)){
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NOT_ENOUGH_COINS"));
				}else{
					buyed.onClick(player, type,null);
				}
				player.closeInventory();
			}
			
		},Material.GOLD_NUGGET,"§aCoins"));
		fill(Material.STAINED_GLASS_PANE,7);
	}

}
