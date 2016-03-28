package eu.epicpvp.kcore.Inventory.Inventory.Craft;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;

import eu.epicpvp.kcore.Inventory.Inventory.InventoryPlayer;
import lombok.Getter;

public class CraftInventoryPlayer extends CraftInventory{
	@Getter
	public InventoryPlayer inventoryPlayer;
		
	public CraftInventoryPlayer(InventoryPlayer inventoryPlayer){
		super(inventoryPlayer);
		this.inventoryPlayer=inventoryPlayer;
	}
}