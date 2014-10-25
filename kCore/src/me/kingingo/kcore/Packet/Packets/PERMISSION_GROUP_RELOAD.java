package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class PERMISSION_GROUP_RELOAD extends Packet{

	@Setter
	@Getter
	String group;
	
	public PERMISSION_GROUP_RELOAD(){}
	
	public PERMISSION_GROUP_RELOAD(String[] packet){
		Set(packet);
	}
	
	public PERMISSION_GROUP_RELOAD(String group){
		this.group=group;
	}
	
	public PERMISSION_GROUP_RELOAD create(String[] packet){
		return new PERMISSION_GROUP_RELOAD(packet);
	}
	
	public String getName(){
		return "PERMISSION_GROUP_RELOAD";
	}
	
	public void Set(String[] split){
		group=split[1];
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		group=split[1];
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s", getGroup());
	}
	
}
