package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayInSettings;

public class kPacketPlayInSettings implements kPacket{

	@Getter
	@Setter
	private PacketPlayInSettings packet;
	private String LOCALE = "a";
	
	public kPacketPlayInSettings(PacketPlayInSettings packet){
		this.packet = packet;
	}
	
	public kPacketPlayInSettings(Object packet){
		this.packet = (PacketPlayInSettings)packet;
	}
	
	public kPacketPlayInSettings(){
		packet = new PacketPlayInSettings();
	}
	
	public void setLocale(String locale){
		UtilReflection.setValue(LOCALE, packet, locale);
	}
	
	public String getLocale(){
		return (String)UtilReflection.getValue(LOCALE, packet);
	}
	
}
