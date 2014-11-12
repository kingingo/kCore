package me.kingingo.kcore.TreasureChest;

import lombok.Getter;

import org.bukkit.Material;

public enum TreasureChestType {
SKY(Material.CHEST,new Material[]{Material.GRASS,Material.STONE}),
SNOW(Material.CHEST,new Material[]{Material.SNOW_BLOCK,Material.ICE}),
NETHER(Material.CHEST,new Material[]{Material.NETHER_BRICK,Material.SOUL_SAND,Material.OBSIDIAN}),
END(Material.ENDER_CHEST,new Material[]{Material.OBSIDIAN,Material.ENDER_STONE});

@Getter
private Material item;
@Getter
private Material[] blockType;

TreasureChestType(Material item,Material... blockType){
	this.item=item;
	this.blockType=blockType;
}
	
}
