package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketStatusOutServerInfo;
import net.minecraft.server.v1_8_R3.ServerPing;

public class WrapperPacketStatusOutServerInfo implements PacketWrapper{

	@Getter
	private PacketStatusOutServerInfo packet;
	private String SERVERPING = "b";
	
	public WrapperPacketStatusOutServerInfo(PacketStatusOutServerInfo packet){
		this.packet = packet;
	}
	
	public WrapperPacketStatusOutServerInfo(Object packet){
		this.packet = (PacketStatusOutServerInfo)packet;
	}
	
	public WrapperPacketStatusOutServerInfo(){
		packet = new PacketStatusOutServerInfo();
	}

	public ServerPing getServerPing(){
		return (ServerPing)UtilReflection.getValue(SERVERPING, packet);
	}
	
	public void setList(ServerPing serverping){
		UtilReflection.setValue(SERVERPING, packet, serverping);
	}
	
}
