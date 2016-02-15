package me.kingingo.kcore.Inventory.Inventory;

import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBestOfSwitch;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonUpDown;
import me.kingingo.kcore.Util.InventorySplit;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryNextPage extends InventoryPageBase{
	
	@Getter
	private Player player;
	
	public InventoryNextPage(Player player,InventorySize size,String title){
		super(size,title);
		this.player=player;
	}
}
