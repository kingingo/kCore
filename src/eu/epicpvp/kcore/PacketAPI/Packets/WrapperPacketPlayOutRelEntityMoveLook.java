package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;

public class WrapperPacketPlayOutRelEntityMoveLook implements PacketWrapper{
	@Getter
	private PacketPlayOutRelEntityMoveLook packet;

	public WrapperPacketPlayOutRelEntityMoveLook(){
		packet=new PacketPlayOutRelEntityMoveLook();
	}
	
	public WrapperPacketPlayOutRelEntityMoveLook(int entityID, byte x,byte y,byte z,byte yaw,byte pitch,boolean b){
		packet=new PacketPlayOutRelEntityMoveLook(entityID,x,y,z,yaw,pitch,b);
	}
	
}
