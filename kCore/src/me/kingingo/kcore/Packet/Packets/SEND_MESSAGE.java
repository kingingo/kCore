package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class SEND_MESSAGE extends Packet{

	@Setter
	@Getter
	String Message;
	@Setter
	@Getter
	String Player;
	
	public SEND_MESSAGE(){}
	
	public SEND_MESSAGE(String[] packet){
		Set(packet);
	}
	
	public SEND_MESSAGE(String Message,String Player){
		this.Message=Message;
		this.Player=Player;
	}
	
	public String getName(){
		return "SEND_MESSAGE";
	}
	
	public void Set(String[] packet){
		Message=packet[1];
		Player=packet[2];;
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s", Player,Message);
	}
	
}
