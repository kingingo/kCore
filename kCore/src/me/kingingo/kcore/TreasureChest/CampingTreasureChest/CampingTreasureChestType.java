package me.kingingo.kcore.TreasureChest.CampingTreasureChest;

import lombok.Getter;

import org.bukkit.Material;

public enum CampingTreasureChestType {
SKY(Material.CHEST,new Material[]{Material.GRASS,Material.STONE}),
SNOW(Material.CHEST,new Material[]{Material.SNOW_BLOCK,Material.ICE}),
NETHER(Material.CHEST,new Material[]{Material.NETHER_BRICK,Material.SOUL_SAND,Material.OBSIDIAN}),
END(Material.ENDER_CHEST,new Material[]{Material.OBSIDIAN,Material.ENDER_STONE});

@Getter
private Material item;
@Getter
private Material[] blockType;

CampingTreasureChestType(Material item,Material... blockType){
	this.item=item;
	this.blockType=blockType;
}
	
}
