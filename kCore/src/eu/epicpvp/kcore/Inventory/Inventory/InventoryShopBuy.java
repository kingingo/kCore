package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBack;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonShopBuy;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilItem;

public class InventoryShopBuy extends InventoryPageBase{
	
	public InventoryShopBuy(InventoryPageBase shop,Player player, ItemStack item,StatsManager statsManager, int money) {
		super("InventoryShopBuy",InventorySize._54.getSize(),"Buy");
		addButton(new ButtonShopBuy(shop,player,statsManager,this, item,money));
		addButton(0, new ButtonBack(shop, UtilItem.RenameItem(new ItemStack(Material.STAINED_CLAY,1,(byte)14), "§cAbbrechen")));
	}

}
