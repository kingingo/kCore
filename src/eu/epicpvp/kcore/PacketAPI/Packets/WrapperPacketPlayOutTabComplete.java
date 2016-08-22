package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTabComplete;

public class WrapperPacketPlayOutTabComplete implements PacketWrapper{

	@Getter
	private PacketPlayOutTabComplete packet;
	private String LIST = "a";
	
	public WrapperPacketPlayOutTabComplete(PacketPlayOutTabComplete packet){
		this.packet = packet;
	}
	
	public WrapperPacketPlayOutTabComplete(Object packet){
		this.packet = (PacketPlayOutTabComplete)packet;
	}
	
	public WrapperPacketPlayOutTabComplete(){
		packet = new PacketPlayOutTabComplete();
	}

	public String[] getList(){
		return (String[])UtilReflection.getValue(LIST, packet);
	}
	
	public void setList(String[] list){
		UtilReflection.setValue(LIST, packet, list);
	}
	
}
