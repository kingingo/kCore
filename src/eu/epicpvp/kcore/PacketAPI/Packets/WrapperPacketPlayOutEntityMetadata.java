package eu.epicpvp.kcore.PacketAPI.Packets;

import java.util.List;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.DataWatcher.WatchableObject;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;

public class WrapperPacketPlayOutEntityMetadata implements PacketWrapper{
	@Getter
	@Setter
	private PacketPlayOutEntityMetadata packet;
	private String ID = "a";
	private String WATCHABLE = "b";
	
	public WrapperPacketPlayOutEntityMetadata(PacketPlayOutEntityMetadata packet){
		this.packet=packet;
	}
	
	public WrapperPacketPlayOutEntityMetadata(){
		this(new PacketPlayOutEntityMetadata());
	}
	
	public WrapperPacketPlayOutEntityMetadata(int entityID,DataWatcher watcher){
		packet=new PacketPlayOutEntityMetadata();
		setEntityID(entityID);
		setList(watcher.c());
	}
	
	public WrapperPacketPlayOutEntityMetadata(int entityID,WrapperDataWatcher watcher){
		packet=new PacketPlayOutEntityMetadata();
		setEntityID(entityID);
		setList(watcher);
	}
	
	public List<WatchableObject> getList(){
		return (List<WatchableObject>) UtilReflection.getValue(WATCHABLE,packet);
	}
	
	public void setList(List<WatchableObject> watcher){
		UtilReflection.setValue(WATCHABLE, packet, watcher);
	}
	
	public void setList(WrapperDataWatcher watcher){
		setList(watcher.c());
	}
	
	public int getEntityID(){
		return (int)UtilReflection.getValue(ID, packet);
	}
	
	public void setEntityID(int id){
		UtilReflection.setValue(ID, packet, id);
	}
	
}
