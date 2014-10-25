package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class BROADCAST extends Packet{

	@Setter
	@Getter
	String Message;
	
	public BROADCAST(){}
	
	public BROADCAST(String[] packet){
		Set(packet);
	}
	
	public BROADCAST(String Message){
		this.Message=Message;
	}
	
	public BROADCAST create(String[] packet){
		return new BROADCAST(packet);
	}
	
	public String getName(){
		return "BROADCAST";
	}
	
	public void Set(String[] split){
		Message=split[1];
	}
	
	public void Set(String packet){
		Message=packet;
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s", Message);
	}
	
}
