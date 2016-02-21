package me.kingingo.kcore.TreasureChest.NEW;

import lombok.Getter;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class BlockVector extends Vector{

	@Getter
	private int id;
	@Getter
	private byte data;
	
	public BlockVector(int x, int y, int z, int id, byte data){
		super(x,y,z);
		this.id=id;
		this.data=data;
	}
	
	public void placeBlock(Location loc){
		Block block = loc.getWorld().getBlockAt(loc.getBlockX()+getBlockX(), loc.getBlockY()+getBlockY(), loc.getBlockZ()+getBlockZ());
		block.setTypeId(getId());
		block.setData(getData());
	}
}
