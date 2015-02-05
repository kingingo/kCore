package me.kingingo.kcore.Packet.Packets;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class PERMISSION_USER_REMOVE_ALL extends Packet{

	@Setter
	@Getter
	UUID uuid;
	
	public PERMISSION_USER_REMOVE_ALL(){}
	
	public PERMISSION_USER_REMOVE_ALL(String[] packet){
		Set(packet);
	}
	
	public PERMISSION_USER_REMOVE_ALL(UUID uuid){
		this.uuid=uuid;
	}
	
	public PERMISSION_USER_REMOVE_ALL create(String[] packet){
		return new PERMISSION_USER_REMOVE_ALL(packet);
	}
	
	public String getName(){
		return "PERMISSION_USER_REMOVE_ALL";
	}
	
	public void Set(String[] split){
		uuid=UUID.fromString(split[1]);
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		uuid=UUID.fromString(split[1]);
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s", getUuid());
	}
	
}
