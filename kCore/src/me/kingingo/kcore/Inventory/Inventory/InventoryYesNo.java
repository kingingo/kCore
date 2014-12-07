package me.kingingo.kcore.Inventory.Inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

public class InventoryYesNo extends InventoryPageBase{

	public InventoryYesNo(String Title) {
		super(9,Title);
		
		addButton(2,new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type) {
				
			}
			
		},Material.EMERALD_BLOCK,"§aJA"));
		
		addButton(6,new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type) {
				
			}
			
		},Material.REDSTONE_BLOCK,"§aNEIN"));
	}

}
