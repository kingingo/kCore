package me.kingingo.kcore.Packet.Packets;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;
public class WORLD_CHANGE_DATA extends Packet{

	@Setter
	@Getter
	UUID old_uuid;
	@Getter
	@Setter
	UUID new_uuid;
	@Getter
	@Setter
	String worldName;
	
	public WORLD_CHANGE_DATA(){}
	
	public WORLD_CHANGE_DATA(String[] packet){
		Set(packet);
	}
	
	public WORLD_CHANGE_DATA(UUID old_uuid,UUID new_uuid,String worldName){
		this.old_uuid=old_uuid;
		this.new_uuid=new_uuid;
		this.worldName=worldName;
	}
	
	public WORLD_CHANGE_DATA create(String[] packet){
		return new WORLD_CHANGE_DATA(packet);
	}
	
	public String getName(){
		return "WORLD_CHANGE_DATA";
	}
	
	public void Set(String[] split){
		this.old_uuid=UUID.fromString(split[1]);
		this.new_uuid=UUID.fromString(split[2]);
		this.worldName=split[3];
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		this.old_uuid=UUID.fromString(split[1]);
		this.new_uuid=UUID.fromString(split[2]);
		this.worldName=split[3];
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s-/-%s", getOld_uuid(),getNew_uuid(),getWorldName());
	}
	
}
