package eu.epicpvp.kcore.MysteryBox.Store;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonOpenInventory;
import eu.epicpvp.kcore.MysteryBox.MysteryBox;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;

public class MysteryStore extends InventoryCopy{

	public MysteryStore(MysteryBox box) {
		super(InventorySize._45.getSize(), "MysteryBox Store");
		addButton(4,new MysteryShardButton(4));
		
		InventoryPageBase craft = new InventoryPageBase(InventorySize._27, "Mystery Box Craft");
		craft.setItem(InventorySplit._9.getMiddle(), box.getItem());
		craft.addButton(11, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				StatsManager stats = StatsManagerRepository.getStatsManager(GameType.Money);
				if(stats.getInt(player, StatsKey.MYSTERY_SHARPS) >= box.getShards()){
					stats.add(player, StatsKey.MYSTERY_SHARPS, -box.getShards());
					box.getManager().addAmount(player, 1, box.getName());
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "MYSTERYBOX_BUY"));
				}else{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NOT_ENOUGH_SHARPS"));
				}
				player.closeInventory();
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.PRISMARINE_SHARD), "§b§l"+box.getShards()+" Mystery Shards")));
		craft.addButton(15, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.closeInventory();
				player.sendMessage(TranslationHandler.getPrefixAndText(player, "SHOP_LINK"));
			}
			
		},UtilItem.Item(new ItemStack(Material.EXP_BOTTLE),new String[]{"§7Shop.ClashMC.eu"}, "§aOnlineStore")));
		addButton(InventorySplit._45.getMiddle(), new ButtonOpenInventory(craft, UtilItem.RenameItem(new ItemStack(Material.ANVIL), "§eCraft MysteryBoxes")));
		addButton(InventorySplit._27.getMiddle(), new ButtonCopy(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				int amount = box.getManager().getAmount(player, box.getName());
				if(amount>0){
					((InventoryPageBase)object).setItem(InventorySplit._27.getMiddle(), UtilItem.Item(new ItemStack(Material.ENDER_CHEST), new String[]{"§7Anzahl: "+amount}, "§aOpen Mystery Box"));
				}else{
					((InventoryPageBase)object).setItem(InventorySplit._27.getMiddle(), UtilItem.Item(new ItemStack(Material.STAINED_CLAY,1,(byte)6), new String[]{}, "§cDu Besitzt keine Boxen."));
				}
			}
			
		}, new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(((ItemStack)object).getType() == Material.ENDER_CHEST 
						&& !box.getManager().isBlocked(player.getLocation())
						&& !box.getSessions().containsKey(player)){
					int amount = box.getManager().getAmount(player, box.getName());
					amount--;
					box.getManager().setAmount(player, amount, box.getName());
					box.start(player);
				}
				player.closeInventory();
			}
			
		},new ItemStack(Material.ENDER_CHEST)));
		
		fill(Material.STAINED_GLASS_PANE,(byte)7);
		craft.fill(Material.STAINED_GLASS_PANE,(byte)7);
		setCreate_new_inv(true);
		UtilInv.getBase().addPage(this);
		UtilInv.getBase().addPage(craft);
	}
}
