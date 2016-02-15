package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBack;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonShopBuy;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.Gems;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryShopBuy extends InventoryPageBase{
	
	public InventoryShopBuy(InventoryPageBase shop,Player player, ItemStack item,StatsManager statsManager, int money) {
		super("InventoryShopBuy",InventorySize._54.getSize(),"Buy");
		addButton(new ButtonShopBuy(shop,player,statsManager,this, item,money));
		addButton(0, new ButtonBack(shop, UtilItem.RenameItem(new ItemStack(Material.STAINED_CLAY,1,(byte)14), "§cAbbrechen")));
	}

}
