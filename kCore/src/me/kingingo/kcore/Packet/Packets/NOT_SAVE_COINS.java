package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class NOT_SAVE_COINS extends Packet{

	@Setter
	@Getter
	String spieler;
	
	public NOT_SAVE_COINS(){}
	
	public NOT_SAVE_COINS(String[] packet){
		Set(packet);
	}
	
	public NOT_SAVE_COINS(String spieler){
		this.spieler=spieler;
	}
	
	public NOT_SAVE_COINS create(String[] packet){
		return new NOT_SAVE_COINS(packet);
	}
	
	public String getName(){
		return "NOT_SAVE_COINS";
	}
	
	public void Set(String[] split){
		spieler=split[1];
	}
	
	public void Set(String packet){
		spieler=packet;
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s", spieler);
	}
	
}
