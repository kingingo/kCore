package me.kingingo.kcore.PacketAPI.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.PacketPlayInSettings;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;

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
