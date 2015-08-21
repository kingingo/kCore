package me.kingingo.kcore.PacketAPI.Packets;

import lombok.Getter;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
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
