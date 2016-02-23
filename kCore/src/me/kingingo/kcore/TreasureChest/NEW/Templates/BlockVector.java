package me.kingingo.kcore.TreasureChest.NEW.Templates;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.util.Vector;

import lombok.Getter;
import lombok.Setter;

public class BlockVector extends Vector{

	@Setter
	@Getter
	private int id;
	@Getter
	@Setter
	private byte data;
	
	public BlockVector(int x, int y, int z, int id, byte data){
		super(x,y,z);
		this.id=id;
		this.data=data;
	}
	
	public BlockState placeBlock(Location loc){
		Block block = loc.getWorld().getBlockAt(loc.getBlockX()+getBlockX(), loc.getBlockY()+getBlockY(), loc.getBlockZ()+getBlockZ());
		BlockState state = block.getState();
//		loc.getWorld().playEffect(loc, Effect.ITEM_BREAK, getId());
		block.setTypeId(getId());
		block.setData(getData());
		return state;
	}
}
