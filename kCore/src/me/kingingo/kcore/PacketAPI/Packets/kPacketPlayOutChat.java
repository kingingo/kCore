package me.kingingo.kcore.PacketAPI.Packets;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.PacketAPI.UtilPacket;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class kPacketPlayOutChat implements kPacket{
	@Getter
	@Setter
	private PacketPlayOutChat packet;
	private String ICHAT = "a";
	
	public kPacketPlayOutChat(){
		packet=new PacketPlayOutChat();
	}
	
	public IChatBaseComponent getIChatBaseComponent(){
		return (IChatBaseComponent)UtilReflection.getValue(ICHAT, packet);
	}
	
	public void setIChatBaseComponent(IChatBaseComponent base){
		UtilReflection.setValue(ICHAT, packet, base);
	}
}
