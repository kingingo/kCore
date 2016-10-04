package eu.epicpvp.kcore.Inventory.Item.Buttons;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.datenserver.definitions.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.ItemShop.Events.PlayerBuyItemEvent;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;

public class ButtonShopBuy extends ButtonMultiCopy{

	public ButtonShopBuy(InventoryPageBase shop,Player player,StatsManager statsManager,InventoryPageBase page, ItemStack item,int money) {
		super(new ButtonForMultiButtonsCopy[]{
		new ButtonForMultiButtonsCopy(page, 8, new Click() {
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				int amount = getAmount(page);
				if(statsManager.getDouble(StatsKey.MONEY, player)>=(amount*money)){
					statsManager.addDouble(player, -(amount*money), StatsKey.MONEY);
					ItemStack bitem;
					for(int i = 0; i<(amount/64); i++){
						bitem = page.getItem(4).clone();
						bitem.setAmount(64);
						player.getInventory().addItem(bitem);
					}

					if((amount%64)!=0){
						bitem = page.getItem(4).clone();
						bitem.setAmount((amount%64));
						player.getInventory().addItem(bitem);
					}

					if(item.getData().getData()==0){
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "SIGN_SHOP_GET",new String[]{String.valueOf(amount),String.valueOf(item.getTypeId()),String.valueOf( (amount*money) )}));
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "SIGN_SHOP_GET_",new String[]{String.valueOf(amount),String.valueOf(item.getTypeId()),String.valueOf(item.getData().getData()),String.valueOf( (amount*money) )}));
					}
					Bukkit.getPluginManager().callEvent(new PlayerBuyItemEvent(player, item, amount*money));
				}else{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NOT_ENOUGH_MONEY"));
				}

				page.clear();
				player.openInventory(shop);
			}
		}, UtilItem.RenameItem(new ItemStack(Material.STAINED_CLAY,1,(byte)5), "§aKaufen"), null),
		new ButtonForMultiButtonsCopy(page, 1, new Click() {

			@Override
			public void onClick(Player player, ActionType type, Object object) {

				changePrice(page,(money*(setAmount(page, getAmount(page)-64, money))));
			}
		}, UtilItem.RenameItem(new ItemStack(Material.STONE_PLATE), "§c-64"), null),
		new ButtonForMultiButtonsCopy(page, 2, new Click() {

			@Override
			public void onClick(Player player, ActionType type, Object object) {

				changePrice(page,(money*(setAmount(page, getAmount(page)-10, money))));
			}
		}, UtilItem.RenameItem(new ItemStack(Material.STONE_BUTTON), "§c-10"), null),
		new ButtonForMultiButtonsCopy(page, 3, new Click() {

			@Override
			public void onClick(Player player, ActionType type, Object object) {

				changePrice(page,(money*(setAmount(page, getAmount(page)-1, money))));
			}
		}, UtilItem.RenameItem(new ItemStack(Material.WOOD_BUTTON), "§c-1"), null),

		new ButtonForMultiButtonsCopy(page, 5, new Click() {

			@Override
			public void onClick(Player player, ActionType type, Object object) {

				changePrice(page,(money*(setAmount(page, getAmount(page)+1, money))));
			}
		},  UtilItem.RenameItem(new ItemStack(Material.WOOD_BUTTON), "§a+1"), null),
		new ButtonForMultiButtonsCopy(page, 6, new Click() {

			@Override
			public void onClick(Player player, ActionType type, Object object) {

				changePrice(page,(money*(setAmount(page, getAmount(page)+10, money))));
			}
		}, UtilItem.RenameItem(new ItemStack(Material.STONE_BUTTON), "§a+10"), null),
		new ButtonForMultiButtonsCopy(page, 7, new Click() {

			@Override
			public void onClick(Player player, ActionType type, Object object) {

				changePrice(page,(money*(setAmount(page, getAmount(page)+64, money))));
			}
		}, UtilItem.RenameItem(new ItemStack(Material.WOOD_PLATE), "§a+64"), null)});
		page.setItem(4, UtilItem.setLore(item, new String[]{"§eAnzahl "+1,"§e1 § §a"+money+" Epics","§e10 § §a"+(money*10)+" Epics","§e64 § §a"+(money*64)+" Epics"}));

		for(int i=InventorySplit._18.getMin(); i<=InventorySplit._18.getMax(); i++)page.setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)15)," "));

		int fslots = UtilInv.FreeInventory(player);
		ItemStack blocked = UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14 ), "§cBelegt");
		for(int i = (fslots+18); i<54; i++){
			page.setItem(i, blocked);
		}
		setAmount(page, 0, 0);
	}

	public static int changeItems(InventoryPageBase page,int amount){
		int slot = InventorySplit._27.getMin();
		int a=0;

		for(int s = InventorySplit._27.getMin(); s<54; s++){
			if(page.getItem(s)!=null){
				if(page.getItem(s).hasItemMeta()&&page.getItem(s).getItemMeta().hasDisplayName()&&page.getItem(s).getItemMeta().getDisplayName().equalsIgnoreCase("§cBelegt"))break;
				page.setItem(s, null);
			}
		}

		ItemStack item;
		for(int i = 0; i<(amount/64); i++){
			if(slot>53)return a;
			if(page.getItem(slot)!=null&&page.getItem(slot).hasItemMeta()&&page.getItem(slot).getItemMeta().hasDisplayName()&&page.getItem(slot).getItemMeta().getDisplayName().equalsIgnoreCase("§cBelegt"))return a;
			item = page.getItem(4).clone();
			item.setAmount(64);
			page.setItem(slot, item);
			a+=64;
			slot++;
		}

		if((amount%64)!=0){
			if(slot>53)return a;
			if(page.getItem(slot)!=null&&page.getItem(slot).hasItemMeta()&&page.getItem(slot).getItemMeta().hasDisplayName()&&page.getItem(slot).getItemMeta().getDisplayName().equalsIgnoreCase("§cBelegt"))return a;
			item = page.getItem(4).clone();
			item.setAmount(amount%64);
			page.setItem(slot, item);
			a+=(amount%64);
		}
		return a;
	}

	public static int setAmount(InventoryPageBase page,int amount ,int money){
		if(amount<=0){
			amount = changeItems(page,1);
			page.setItem(4, UtilItem.setLore(page.getItem(4), new String[]{"§eAnzahl "+1,"§e1 § §a"+money+" Epics","§e10 § §a"+(money*10)+" Epics","§e64 § §a"+(money*64)+" Epics"}));
			return 1;
		}else{
			amount = changeItems(page,amount);
			page.setItem(4, UtilItem.setLore(page.getItem(4), new String[]{"§eAnzahl "+amount,"§e1 § §a"+money+" Epics","§e10 § §a"+(money*10)+" Epics","§e64 § §a"+(money*64)+" Epics"}));
		}
		return amount;
	}

	public static int getAmount(InventoryPageBase page){
		return Integer.valueOf(page.getItem(4).getItemMeta().getLore().get(0).split(" ")[1]);
	}

	public static void changePrice(InventoryPageBase page ,int money){
		page.setItem(8, UtilItem.setLore(page.getItem(8), new String[]{"§e"+money+" Epics"}));
	}
}
