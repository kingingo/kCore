package eu.epicpvp.kcore.PacketAPI.Packets;

import java.util.List;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.DataWatcher.WatchableObject;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;

public class kPacketPlayOutEntityMetadata implements kPacket{
	@Getter
	@Setter
	private PacketPlayOutEntityMetadata packet;
	private String ID = "a";
	private String WATCHABLE = "b";
	
	public kPacketPlayOutEntityMetadata(PacketPlayOutEntityMetadata packet){
		this.packet=packet;
	}
	
	public kPacketPlayOutEntityMetadata(){
		this(new PacketPlayOutEntityMetadata());
	}
	
	public kPacketPlayOutEntityMetadata(int entityID,DataWatcher watcher){
		packet=new PacketPlayOutEntityMetadata();
		setEntityID(entityID);
		setList(watcher.c());
	}
	
	public kPacketPlayOutEntityMetadata(int entityID,kDataWatcher watcher){
		packet=new PacketPlayOutEntityMetadata();
		setEntityID(entityID);
		setList(watcher);
	}
	
	public List<WatchableObject> getList(){
		return (List<WatchableObject>)UtilReflection.getValue(WATCHABLE,packet);
	}
	
	public void setList(List<WatchableObject> watcher){
		UtilReflection.setValue(WATCHABLE, packet, watcher);
	}
	
	public void setList(kDataWatcher watcher){
		setList(watcher.c());
	}
	
	public int getEntityID(){
		return (int)UtilReflection.getValue(ID, packet);
	}
	
	public void setEntityID(int id){
		UtilReflection.setValue(ID, packet, id);
	}
	
}
