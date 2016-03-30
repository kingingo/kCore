package eu.epicpvp.kcore.PacketAPI.Packets;

import org.bukkit.Location;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction;

public class kPacketPlayOutBlockAction implements kPacket{
	@Getter
	private PacketPlayOutBlockAction packet;
	private String ID = "b";
	private String ID1 = "c";
	private String BLOCK_POSIION = "a";
	private String BLOCK = "d";
	
	public kPacketPlayOutBlockAction(){
		packet=new PacketPlayOutBlockAction();
	}
	
	public kPacketPlayOutBlockAction(Block b,Location loc,int action){
		packet=new PacketPlayOutBlockAction();
		setPosition(loc);
		setBlock(b);
		setBlockAction(action);
	}
	
	public kPacketPlayOutBlockAction(int id,Location loc,int action){
		packet=new PacketPlayOutBlockAction();
		setPosition(loc);
		setBlock(id);
		setBlockAction(action);
	}
	
	public int getBlockAction(){
		return (int)UtilReflection.getValue(ID1, packet);
	}
	
	public void setBlockAction(int i){
		UtilReflection.setValue(ID1, packet, i);
		UtilReflection.setValue(ID, packet, i);
	}
	
	public Block getBlock(){
		return (Block)UtilReflection.getValue(BLOCK, packet);
	}
	
	public void setBlock(int id){
		UtilReflection.setValue(BLOCK, packet, Block.getById(id));
	}
	
	public void setBlock(Block block){
		UtilReflection.setValue(BLOCK, packet, block);
	}
	
	public BlockPosition getPosition(){
		return (BlockPosition)UtilReflection.getValue(BLOCK_POSIION, packet);
	}
	
	public void setPosition(Location location){
		UtilReflection.setValue(BLOCK_POSIION, packet, new BlockPosition(location.getX(), location.getY(), location.getZ()));
	}

}
