package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

public class WrapperPacketPlayOutEntityDestroy implements PacketWrapper{
	@Getter
	private PacketPlayOutEntityDestroy packet;
	private String IDS = "a";
	
	public WrapperPacketPlayOutEntityDestroy(){
		packet=new PacketPlayOutEntityDestroy();
	}
	
	public WrapperPacketPlayOutEntityDestroy(int id){
		packet=new PacketPlayOutEntityDestroy();
		setID(id);
	}
	
	public WrapperPacketPlayOutEntityDestroy(int ids[]){
		packet=new PacketPlayOutEntityDestroy();
		setIDs(ids);
	}
	
	public int getID(){
		return (int) ((int[]) UtilReflection.getValue(IDS, packet))[0];
	}
	
	public int[] getIDs(){
		return (int[]) UtilReflection.getValue(IDS, packet);
	}
	
	public void setID(int id){
		UtilReflection.setValue(IDS, packet, new int[]{id});
	}
	
	public void setIDs(int ids[]){
		UtilReflection.setValue(IDS, packet, ids);
	}
	
}
