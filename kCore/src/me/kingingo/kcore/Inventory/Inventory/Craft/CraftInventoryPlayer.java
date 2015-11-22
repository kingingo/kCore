package me.kingingo.kcore.Inventory.Inventory.Craft;

import lombok.Getter;
import me.kingingo.kcore.Inventory.Inventory.InventoryPlayer;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventory;

public class CraftInventoryPlayer extends CraftInventory{
	@Getter
	public InventoryPlayer inventoryPlayer;
		
	public CraftInventoryPlayer(InventoryPlayer inventoryPlayer){
		super(inventoryPlayer);
		this.inventoryPlayer=inventoryPlayer;
	}
}