package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.Material;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;

public class InventoryYesNo extends InventoryPageBase{

	public InventoryYesNo(String Title,Click Ja,Click Nein) {
		super("InventoryYesNo",9,Title);
		addButton(2,new ButtonBase(Ja,Material.EMERALD_BLOCK,"§aJA"));
		addButton(6,new ButtonBase(Nein,Material.REDSTONE_BLOCK,"§aNEIN"));
		this.fill(Material.STAINED_GLASS_PANE,7);
	}

}
