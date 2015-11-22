package me.kingingo.kcore.PacketAPI.Packets;

import lombok.Getter;
import me.kingingo.kcore.PacketAPI.UtilPacket;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;

import org.bukkit.Location;

public class kPacketPlayOutBed implements kPacket{
	@Getter
	private PacketPlayOutBed packet;
	private String ID = "a";
	private String BLOCK_POSIION = "b";
	
	public kPacketPlayOutBed(){
		packet=new PacketPlayOutBed();
	}
	
	public kPacketPlayOutBed(EntityHuman human,BlockPosition block){
		packet=new PacketPlayOutBed(human,block);
	}
	
	public int getEntityID(){
		return (int)UtilReflection.getValue(ID, packet);
	}
	
	public void setEntityID(int id){
		UtilReflection.setValue(ID, packet, id);
	}
	
	public BlockPosition getPosition(){
		return (BlockPosition)UtilReflection.getValue(BLOCK_POSIION, packet);
	}
	
	public void setPosition(Location location){
		UtilReflection.setValue(BLOCK_POSIION, packet, new BlockPosition(UtilPacket.toFixedPoint(location.getX()), UtilPacket.toFixedPoint(location.getY()), UtilPacket.toFixedPoint(location.getZ())));
	}
	
}
