package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.Util.AnvilGUI;
import me.kingingo.kcore.Util.AnvilGUI.AnvilClickEventHandler;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

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
