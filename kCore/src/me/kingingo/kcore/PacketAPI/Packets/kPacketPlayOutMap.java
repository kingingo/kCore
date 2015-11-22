package me.kingingo.kcore.PacketAPI.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.PacketAPI.kPacket;
import net.minecraft.server.v1_8_R3.PacketPlayOutMap;

public class kPacketPlayOutMap implements kPacket{
	@Getter
	@Setter
	private PacketPlayOutMap packet;
	private String ICHAT = "a";
	
	public kPacketPlayOutMap(){
		packet=new PacketPlayOutMap();
	}
	
}
