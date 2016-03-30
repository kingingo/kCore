package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import lombok.Getter;
import lombok.Setter;
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
