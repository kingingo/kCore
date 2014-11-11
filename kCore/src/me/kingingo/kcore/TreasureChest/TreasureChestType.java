package me.kingingo.kcore.TreasureChest;

import lombok.Getter;

import org.bukkit.Material;

public enum TreasureChestType {
GRASS(Material.CHEST,Material.GRASS),
SNOW(Material.CHEST,Material.SNOW_BLOCK),
NETHER(Material.CHEST,Material.NETHER_BRICK),
END(Material.ENDER_CHEST,Material.ENDER_STONE);

@Getter
private Material item;
@Getter
private Material blockType;

TreasureChestType(Material item,Material blockType){
	this.item=item;
	this.blockType=blockType;
}
	
}
