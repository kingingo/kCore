package me.kingingo.kcore.Packet.Packets;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class PERMISSION_USER_RELOAD extends Packet{

	@Setter
	@Getter
	UUID uuid;
	
	public PERMISSION_USER_RELOAD(){}
	
	public PERMISSION_USER_RELOAD(String[] packet){
		Set(packet);
	}
	
	public PERMISSION_USER_RELOAD(UUID uuid){
		this.uuid=uuid;
	}
	
	public PERMISSION_USER_RELOAD create(String[] packet){
		return new PERMISSION_USER_RELOAD(packet);
	}
	
	public String getName(){
		return "PERMISSION_GROUP_RELOAD";
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
