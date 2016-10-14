package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;

public class WrapperPacketPlayOutAttachEntity implements PacketWrapper{
	@Getter
	private PacketPlayOutAttachEntity packet;
	private String ENTITY_ID = "b";
	private String VEHICLE_ID = "c";
	private String LEACHED = "a";
	
	public WrapperPacketPlayOutAttachEntity(){
		packet=new PacketPlayOutAttachEntity();
	}
	
	public void setLeached(boolean leached){
		UtilReflection.setValue(LEACHED, packet, (leached?1:0) );
	}
	
	public boolean isLeached(){
		return (((int)UtilReflection.getValue(LEACHED, packet))==1?true:false);
	}
	
	public int getVehicleID(){
		return (int)UtilReflection.getValue(VEHICLE_ID, packet);
	}
	
	public void setVehicleID(int id){
		UtilReflection.setValue(VEHICLE_ID, packet, id);
	}
	
	public int getEntityID(){
		return (int)UtilReflection.getValue(ENTITY_ID, packet);
	}
	
	public void setEntityID(int id){
		UtilReflection.setValue(ENTITY_ID, packet, id);
	}
}