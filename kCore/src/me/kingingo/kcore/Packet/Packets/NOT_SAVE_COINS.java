package me.kingingo.kcore.Packet.Packets;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class NOT_SAVE_COINS extends Packet{

	@Setter
	@Getter
	UUID uuid;
	
	public NOT_SAVE_COINS(){}
	
	public NOT_SAVE_COINS(String[] packet){
		Set(packet);
	}
	
	public NOT_SAVE_COINS(UUID uuid){
		this.uuid=uuid;
	}
	
	public NOT_SAVE_COINS create(String[] packet){
		return new NOT_SAVE_COINS(packet);
	}
	
	public String getName(){
		return "NOT_SAVE_COINS";
	}
	
	public void Set(String[] split){
		uuid=UUID.fromString(split[1]);
	}
	
	public void Set(String packet){
		String[] split=packet.split("-/-");
		uuid=UUID.fromString(split[1]);
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s", getUuid());
	}
	
}
