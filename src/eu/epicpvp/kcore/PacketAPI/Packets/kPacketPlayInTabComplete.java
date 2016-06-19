package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;

public class kPacketPlayInTabComplete implements kPacket{

	@Getter
	@Setter
	private PacketPlayInTabComplete packet;
	private String CMD = "a";
	private String BLOCK_POSTION = "b";
	
	public kPacketPlayInTabComplete(PacketPlayInTabComplete packet){
		this.packet = packet;
	}
	
	public kPacketPlayInTabComplete(Object packet){
		this.packet = (PacketPlayInTabComplete)packet;
	}
	
	public kPacketPlayInTabComplete(){
		packet = new PacketPlayInTabComplete();
	}
	
	public String getBlockPosition(){
		return (String)UtilReflection.getValue(BLOCK_POSTION, packet);
	}
	
	public void setBlockPosition(BlockPosition po){
		UtilReflection.setValue(BLOCK_POSTION, packet, po);
	}

	public String getChatMessage(){
		return (String)UtilReflection.getValue(CMD, packet);
	}
	
	public void setChatMessage(String cmd){
		UtilReflection.setValue(CMD, packet, cmd);
	}
	
}
