package me.kingingo.kcore.PacketAPI.Packets;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.DataWatcher.WatchableObject;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;

public class kPacketPlayOutEntityMetadata implements kPacket{
	@Getter
	@Setter
	private PacketPlayOutEntityMetadata packet;
	private String ID = "a";
	private String WATCHABLE = "b";
	
	public kPacketPlayOutEntityMetadata(){
		packet=new PacketPlayOutEntityMetadata();
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
