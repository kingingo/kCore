package eu.epicpvp.kcore.MysteryBox.Store;

import java.awt.Button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonCopy;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;

public class MysterySharpsButton extends ButtonCopy{

	public MysterySharpsButton(int slot) {
		super(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				int sharps = StatsManagerRepository.getStatsManager(GameType.Money).getInt(player, StatsKey.MYSTERY_SHARPS);
				((InventoryPageBase)object).setItem(slot, UtilItem.RenameItem(new ItemStack(Material.PRISMARINE_SHARD), "§b"+sharps+" MysterySharps"));
			}
			
		}, new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				
			}
			
		}, new ItemStack(Material.PRISMARINE_SHARD));
	}

}
