package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import lombok.Getter;
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
