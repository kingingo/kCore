package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class PERMISSION_USER_REMOVE_ALL extends Packet{

	@Setter
	@Getter
	String user;
	
	public PERMISSION_USER_REMOVE_ALL(){}
	
	public PERMISSION_USER_REMOVE_ALL(String[] packet){
		Set(packet);
	}
	
	public PERMISSION_USER_REMOVE_ALL(String user){
		this.user=user;
	}
	
	public PERMISSION_USER_REMOVE_ALL create(String[] packet){
		return new PERMISSION_USER_REMOVE_ALL(packet);
	}
	
	public String getName(){
		return "PERMISSION_USER_REMOVE_ALL";
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
