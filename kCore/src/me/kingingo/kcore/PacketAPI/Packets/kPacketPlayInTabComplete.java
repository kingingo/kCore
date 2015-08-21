package me.kingingo.kcore.PacketAPI.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
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
