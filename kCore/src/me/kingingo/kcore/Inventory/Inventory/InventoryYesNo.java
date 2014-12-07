package me.kingingo.kcore.Inventory.Inventory;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;

import org.bukkit.Material;

public class InventoryYesNo extends InventoryPageBase{

	public InventoryYesNo(String Title,Click Ja,Click Nein) {
		super(9,Title);
		addButton(2,new ButtonBase(Ja,Material.EMERALD_BLOCK,"§aJA"));
		addButton(6,new ButtonBase(Nein,Material.REDSTONE_BLOCK,"§aNEIN"));
		this.fill(Material.STAINED_GLASS_PANE,7);
	}

}
