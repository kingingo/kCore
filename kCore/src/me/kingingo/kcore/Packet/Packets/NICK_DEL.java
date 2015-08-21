package me.kingingo.kcore.Packet.Packets;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class NICK_DEL extends Packet{

	@Setter
	@Getter
	UUID uuid;
	
	public NICK_DEL(){}
	
	public NICK_DEL(String[] packet){
		Set(packet);
	}
	
	public NICK_DEL(UUID uuid){
		this.uuid=uuid;
	}
	
	public NICK_DEL create(String[] packet){
		return new NICK_DEL(packet);
	}
	
	public String getName(){
		return "NICK_DEL";
	}
	
	public void Set(String[] split){
		uuid=UUID.fromString(split[1]);
	}
	
	public void Set(String packet){
		Set(packet.split("-/-"));
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s", uuid.toString());
	}
	
}
