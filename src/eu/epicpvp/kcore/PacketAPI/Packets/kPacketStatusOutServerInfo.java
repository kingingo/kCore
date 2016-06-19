package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketStatusOutServerInfo;
import net.minecraft.server.v1_8_R3.ServerPing;

public class kPacketStatusOutServerInfo implements kPacket{

	@Getter
	private PacketStatusOutServerInfo packet;
	private String SERVERPING = "b";
	
	public kPacketStatusOutServerInfo(PacketStatusOutServerInfo packet){
		this.packet = packet;
	}
	
	public kPacketStatusOutServerInfo(Object packet){
		this.packet = (PacketStatusOutServerInfo)packet;
	}
	
	public kPacketStatusOutServerInfo(){
		packet = new PacketStatusOutServerInfo();
	}

	public ServerPing getServerPing(){
		return (ServerPing)UtilReflection.getValue(SERVERPING, packet);
	}
	
	public void setList(ServerPing serverping){
		UtilReflection.setValue(SERVERPING, packet, serverping);
	}
	
}
