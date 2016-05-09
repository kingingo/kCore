package eu.epicpvp.kcore.MysteryChest;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryBuy;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesPackageBase;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;

public class MysteryChestShop extends InventoryPageBase{

	public MysteryChestShop(MysteryChestManager chestManager) {
		super(InventorySize._54.getSize(), "MysteryChestShop");
		
		int next = -1;
		int slot=0;
		Integer[] list = UtilInv.getSlotsBorder(InventorySize.invSize(getSize()), InventorySplit._18);
		for(int i = 0; i<list.length; i++){
			next++;
			slot=list[i];
			if(chestManager.getChests().size() <= next){
				setItem(slot, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), " "));
			}else{
				MysteryChest chest = (MysteryChest) chestManager.getChests().values().toArray()[next];
				
				addChest(slot, 1, chestManager, chest);
				slot=list[i+=1];
				addChest(slot, 3, chestManager, chest);
				slot=list[i+=1];
				addChest(slot, 6, chestManager, chest);
				slot=list[i+=1];
				addChest(slot, 12, chestManager, chest);
			}
		}
		
		fill(Material.STAINED_GLASS_PANE,(byte)7);
		UtilInv.getBase().addPage(this);
	}
	
	public void addChest(int slot,int amount,MysteryChestManager chestManager, MysteryChest chest){
		addButton(slot, new SalesPackageBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				InventoryBuy buy = new InventoryBuy(new Click(){

					@Override
					public void onClick(Player player, ActionType type, Object object) {
						chestManager.addAmount(player, amount, chest.getName());
						
					}
					
				}, "Kaufen", StatsManagerRepository.getStatsManager(GameType.Money), chest.getGems()*amount, 0);
				UtilInv.getBase().addAnother(buy);
				player.openInventory(buy);
			}
			
		}, UtilItem.Item(chest.getItem(), new String[]{"§bPreis: "+(amount*chest.getGems())}, "§7"+amount+"x §a"+chest.getName())));
	}

}
