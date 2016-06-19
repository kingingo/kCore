package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTabComplete;

public class kPacketPlayOutTabComplete implements kPacket{

	@Getter
	private PacketPlayOutTabComplete packet;
	private String LIST = "a";
	
	public kPacketPlayOutTabComplete(PacketPlayOutTabComplete packet){
		this.packet = packet;
	}
	
	public kPacketPlayOutTabComplete(Object packet){
		this.packet = (PacketPlayOutTabComplete)packet;
	}
	
	public kPacketPlayOutTabComplete(){
		packet = new PacketPlayOutTabComplete();
	}

	public String[] getList(){
		return (String[])UtilReflection.getValue(LIST, packet);
	}
	
	public void setList(String[] list){
		UtilReflection.setValue(LIST, packet, list);
	}
	
}
