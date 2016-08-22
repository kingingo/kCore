package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;

public class WrapperPacketPlayOutNamedSoundEffect implements PacketWrapper{

	@Getter
	@Setter
	private PacketPlayOutNamedSoundEffect packet;
	private String NAME = "a";
	
	public WrapperPacketPlayOutNamedSoundEffect(PacketPlayOutNamedSoundEffect packet){
		this.packet = packet;
	}
	
	public WrapperPacketPlayOutNamedSoundEffect(Object packet){
		this.packet = (PacketPlayOutNamedSoundEffect)packet;
	}
	
	public WrapperPacketPlayOutNamedSoundEffect(){
		packet = new PacketPlayOutNamedSoundEffect();
	}
	public String getSoundName(){
		return (String)UtilReflection.getValue(NAME, packet);
	}
	
	public void setSoundName(String soundName){
		UtilReflection.setValue(NAME, packet, soundName);
	}
}