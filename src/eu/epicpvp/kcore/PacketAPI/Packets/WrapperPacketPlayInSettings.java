package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayInSettings;

public class WrapperPacketPlayInSettings implements PacketWrapper{

	@Getter
	@Setter
	private PacketPlayInSettings packet;
	private String LOCALE = "a";
	
	public WrapperPacketPlayInSettings(PacketPlayInSettings packet){
		this.packet = packet;
	}
	
	public WrapperPacketPlayInSettings(Object packet){
		this.packet = (PacketPlayInSettings)packet;
	}
	
	public WrapperPacketPlayInSettings(){
		packet = new PacketPlayInSettings();
	}
	
	public void setLocale(String locale){
		UtilReflection.setValue(LOCALE, packet, locale);
	}
	
	public String getLocale(){
		return (String)UtilReflection.getValue(LOCALE, packet);
	}
	
}
