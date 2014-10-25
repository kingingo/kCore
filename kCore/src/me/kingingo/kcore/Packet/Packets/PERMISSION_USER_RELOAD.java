package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class PERMISSION_USER_RELOAD extends Packet{

	@Setter
	@Getter
	String user;
	
	public PERMISSION_USER_RELOAD(){}
	
	public PERMISSION_USER_RELOAD(String[] packet){
		Set(packet);
	}
	
	public PERMISSION_USER_RELOAD(String user){
		this.user=user;
	}
	
	public PERMISSION_USER_RELOAD create(String[] packet){
		return new PERMISSION_USER_RELOAD(packet);
	}
	
	public String getName(){
		return "PERMISSION_GROUP_RELOAD";
	}
	
	public void Set(String[] split){
		user=split[1];
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		user=split[1];
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s", getUser());
	}
	
}
