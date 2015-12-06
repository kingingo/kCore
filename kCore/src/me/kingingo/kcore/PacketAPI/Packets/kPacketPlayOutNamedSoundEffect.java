package me.kingingo.kcore.PacketAPI.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;

public class kPacketPlayOutNamedSoundEffect implements kPacket{

	@Getter
	@Setter
	private PacketPlayOutNamedSoundEffect packet;
	private String NAME = "a";
	
	public kPacketPlayOutNamedSoundEffect(PacketPlayOutNamedSoundEffect packet){
		this.packet = packet;
	}
	
	public kPacketPlayOutNamedSoundEffect(Object packet){
		this.packet = (PacketPlayOutNamedSoundEffect)packet;
	}
	
	public kPacketPlayOutNamedSoundEffect(){
		packet = new PacketPlayOutNamedSoundEffect();
	}
	public String getSoundName(){
		return (String)UtilReflection.getValue(NAME, packet);
	}
	
	public void setSoundName(String soundName){
		UtilReflection.setValue(NAME, packet, soundName);
	}
}