package eu.epicpvp.kcore.Inventory.Inventory;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Util.InventorySize;
import lombok.Getter;

public class InventoryNextPage extends InventoryPageBase{
	
	@Getter
	private Player player;
	
	public InventoryNextPage(Player player,InventorySize size,String title){
		super(size,title);
		this.player=player;
	}
}
