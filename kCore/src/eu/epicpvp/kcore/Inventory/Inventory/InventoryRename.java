package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Util.AnvilGUI;
import eu.epicpvp.kcore.Util.AnvilGUI.AnvilClickEventHandler;
import eu.epicpvp.kcore.Util.UtilItem;

public class InventoryRename{

	private AnvilGUI gui;
	
	public InventoryRename(Player player,AnvilClickEventHandler anvil,JavaPlugin instance,String Title) {
		gui = new AnvilGUI(player,instance, anvil);
		ItemStack renamed = UtilItem.RenameItem(new ItemStack(Material.NAME_TAG), Title);
		gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, renamed);
		gui.setSlot(AnvilGUI.AnvilSlot.OUTPUT, UtilItem.RenameItem(new ItemStack(Material.NAME_TAG), "§aFertig"));
		gui.open();
	}

}
