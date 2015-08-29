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
import net.minecraft.server.v1_8_R3.PacketPlayOutMap;

public class kPacketPlayOutMap implements kPacket{
	@Getter
	@Setter
	private PacketPlayOutMap packet;
	private String ICHAT = "a";
	
	public kPacketPlayOutMap(){
		packet=new PacketPlayOutMap();
	}
	
}
