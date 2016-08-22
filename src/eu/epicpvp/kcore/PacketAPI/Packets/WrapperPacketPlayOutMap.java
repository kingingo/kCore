package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayOutMap;

public class WrapperPacketPlayOutMap implements PacketWrapper{
	@Getter
	@Setter
	private PacketPlayOutMap packet;
	private String ICHAT = "a";
	
	public WrapperPacketPlayOutMap(){
		packet=new PacketPlayOutMap();
	}
	
}
