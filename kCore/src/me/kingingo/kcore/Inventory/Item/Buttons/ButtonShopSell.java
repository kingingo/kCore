package me.kingingo.kcore.Inventory.Item.Buttons;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.InventorySplit;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.command.defaults.SetIdleTimeoutCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ButtonShopSell extends ButtonMultiCopy{
	
	public ButtonShopSell(InventoryPageBase shop,Player player,StatsManager statsManager,InventoryPageBase page, ItemStack item,int money) {
		super(new ButtonForMultiButtonsCopy[]{
		new ButtonForMultiButtonsCopy(page, 8, new Click() {
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				int amount = getAmount(page);
				statsManager.addDouble(player, +(amount*money), Stats.MONEY);
				UtilInv.remove(player, page.getItem(4).getType(), page.getItem(4).getData().getData(), amount);
				page.clear();
				player.openInventory(shop);
			}
		}, UtilItem.RenameItem(new ItemStack(Material.STAINED_CLAY,1,(byte)5), "§aVerkaufen"), null),	
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
		}, UtilItem.RenameItem(new ItemStack(Material.WOOD_PLATE), "§a+64"), null),
		new ButtonForMultiButtonsCopy(page, 13, new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				changePrice(page,(money*(setAmount(page, getAll(page), money))));
			}
		}, UtilItem.Item(new ItemStack(Material.TRIPWIRE_HOOK), new String[]{"§eInsgesamt "+UtilInv.AnzahlInInventory(player, item.getType(), item.getData().getData())},"§6Alles auswählen"), null)});
		page.setItem(4, UtilItem.SetDescriptions(item, new String[]{"§eAnzahl "+1,"§e1 » §a"+money+" Epics","§e10 » §a"+(money*10)+" Epics","§e64 » §a"+(money*64)+" Epics"}));
		
		for(int i=InventorySplit._18.getMin(); i<=InventorySplit._18.getMax(); i++)if(page.getItem(i)==null)page.setItem(i, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)15)," "));
	}
	
	public static int getAll(InventoryPageBase page){
		return Integer.valueOf(page.getItem(13).getItemMeta().getLore().get(0).split(" ")[1]);
	}
	
	public static int changeItems(InventoryPageBase page,int amount){
		int slot = InventorySplit._27.getMin();
		int a=0;
		
		if(amount>getAll(page))amount=getAll(page);
		
		for(int s = InventorySplit._27.getMin(); s<54; s++){
			if(page.getItem(s)!=null){
				page.setItem(s, null);
			}
		}
		
		for(int i = 0; i<(amount/64); i++){
			if(slot>53)return a;
			page.setItem(slot, new ItemStack(page.getItem(4).getType(),64,page.getItem(4).getData().getData()));
			a+=64;
			slot++;
		}
		
		if((amount%64)!=0){
			if(slot>53)return a;
			page.setItem(slot, new ItemStack(page.getItem(4).getType(),(amount%64),page.getItem(4).getData().getData()));
			a+=(amount%64);
		}
		return a;
	}
	
	public static int setAmount(InventoryPageBase page,int amount ,int money){
		if(amount<=0){
			amount = changeItems(page,1);
			page.setItem(4, UtilItem.SetDescriptions(page.getItem(4), new String[]{"§eAnzahl "+1,"§e1 » §a"+money+" Epics","§e10 » §a"+(money*10)+" Epics","§e64 » §a"+(money*64)+" Epics"}));
			return 1;
		}else{
			amount = changeItems(page,amount);
			page.setItem(4, UtilItem.SetDescriptions(page.getItem(4), new String[]{"§eAnzahl "+amount,"§e1 » §a"+money+" Epics","§e10 » §a"+(money*10)+" Epics","§e64 » §a"+(money*64)+" Epics"}));
		}
		return amount;
	}
	
	public static int getAmount(InventoryPageBase page){
		return Integer.valueOf(page.getItem(4).getItemMeta().getLore().get(0).split(" ")[1]);
	}
	
	public static void changePrice(InventoryPageBase page ,int money){
		page.setItem(8, UtilItem.SetDescriptions(page.getItem(8), new String[]{"§e"+money+" Epics"}));
	}
}

