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
		this.Message=packet[1];
	}
	
	public BROADCAST(String Message){
		this.Message=Message;
	}
	
	public String getName(){
		return "BROADCAST";
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		Message=split[1];
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s", Message);
	}
	
}
