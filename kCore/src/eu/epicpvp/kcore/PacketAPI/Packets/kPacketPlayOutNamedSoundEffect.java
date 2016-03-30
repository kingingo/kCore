package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
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