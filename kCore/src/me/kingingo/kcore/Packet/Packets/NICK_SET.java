package me.kingingo.kcore.Packet.Packets;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class NICK_SET extends Packet{

	@Setter
	@Getter
	UUID uuid;
	@Getter
	@Setter
	String nick;
	
	public NICK_SET(){}
	
	public NICK_SET(String[] packet){
		Set(packet);
	}
	
	public NICK_SET(UUID uuid){
		this.uuid=uuid;
	}
	
	public NICK_SET create(String[] packet){
		return new NICK_SET(packet);
	}
	
	public String getName(){
		return "NICK_SET";
	}
	
	public void Set(String[] split){
		uuid=UUID.fromString(split[1]);
		nick=split[2];
	}
	
	public void Set(String packet){
		Set(packet.split("-/-"));
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s", uuid.toString(),nick);
	}
	
}
