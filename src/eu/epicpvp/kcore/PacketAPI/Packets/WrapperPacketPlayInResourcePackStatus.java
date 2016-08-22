package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInResourcePackStatus;
import net.minecraft.server.v1_8_R3.PacketPlayInResourcePackStatus.EnumResourcePackStatus;

public class WrapperPacketPlayInResourcePackStatus implements PacketWrapper{

	@Getter
	private PacketPlayInResourcePackStatus packet;
	private String HASH = "a";
	private String EnumResourcePackStatus = "b";

	public WrapperPacketPlayInResourcePackStatus(){
		this.packet=new PacketPlayInResourcePackStatus();
	}
	
	public WrapperPacketPlayInResourcePackStatus(PacketPlayInResourcePackStatus packet){
		this.packet=packet;
	}
	
	public EnumResourcePackStatus getStatus(){
		return (EnumResourcePackStatus)UtilReflection.getValue(EnumResourcePackStatus, packet);
	}
	
	public void setStatus(EnumResourcePackStatus s){
		UtilReflection.setValue(EnumResourcePackStatus, packet, s);
	}
	
	public String getHash(){
		return (String)UtilReflection.getValue(HASH, packet);
	}
	
	public void setHash(String hash){
		UtilReflection.setValue(HASH, packet, hash);
	}
}
