package me.kingingo.kcore.PacketAPI.Packets;

import lombok.Getter;
import me.kingingo.kcore.PacketAPI.kPacket;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;

public class kPacketPlayOutRelEntityMoveLook implements kPacket{
	@Getter
	private PacketPlayOutRelEntityMoveLook packet;

	public kPacketPlayOutRelEntityMoveLook(){
		packet=new PacketPlayOutRelEntityMoveLook();
	}
	
	public kPacketPlayOutRelEntityMoveLook(int entityID, byte x,byte y,byte z,byte yaw,byte pitch,boolean b){
		packet=new PacketPlayOutRelEntityMoveLook(entityID,x,y,z,yaw,pitch,b);
	}
	
}
