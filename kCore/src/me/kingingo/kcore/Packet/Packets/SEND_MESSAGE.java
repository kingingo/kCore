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
	
	public SEND_MESSAGE(String packet){
		Set(packet);
	}
	
	public SEND_MESSAGE(String[] packet){
		Set(packet);
	}
	
	public SEND_MESSAGE create(String[] packet){
		return new SEND_MESSAGE(packet);
	}
	
	public SEND_MESSAGE(String Message,String Player){
		this.Message=Message;
		this.Player=Player;
	}
	
	public String getName(){
		return "SEND_MESSAGE";
	}
	
	public void Set(String[] split){
		Message=split[1];
		Player=split[2];;
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		Message=split[1];
		Player=split[2];;
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s", Player,Message);
	}
	
}
