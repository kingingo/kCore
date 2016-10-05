package eu.epicpvp.kcore.PacketAPI.Packets;

import org.bukkit.Location;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.PacketAPI.UtilPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;

public class WrapperPacketPlayOutBed implements PacketWrapper{
	@Getter
	private PacketPlayOutBed packet;
	private String ID = "a";
	private String BLOCK_POSIION = "b";
	
	public WrapperPacketPlayOutBed(){
		packet=new PacketPlayOutBed();
	}
	
	public WrapperPacketPlayOutBed(EntityHuman human,BlockPosition block){
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
