package eu.epicpvp.kcore.PacketAPI.Packets;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

public class kPacketPlayOutEntityDestroy implements kPacket{
	@Getter
	private PacketPlayOutEntityDestroy packet;
	private String IDS = "a";
	
	public kPacketPlayOutEntityDestroy(){
		packet=new PacketPlayOutEntityDestroy();
	}
	
	public kPacketPlayOutEntityDestroy(int id){
		packet=new PacketPlayOutEntityDestroy();
		setID(id);
	}
	
	public kPacketPlayOutEntityDestroy(int ids[]){
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
