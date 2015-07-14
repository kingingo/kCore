package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class TEAM_MESSAGE extends Packet{

	@Setter
	@Getter
	String Message;
	
	public TEAM_MESSAGE(){}
	
	public TEAM_MESSAGE(String[] packet){
		Set(packet);
	}
	
	public TEAM_MESSAGE(String Message){
		this.Message=Message;
	}
	
	public String getName(){
		return "TEAM_MESSAGE";
	}
	
	public TEAM_MESSAGE create(String[] packet){
		return new TEAM_MESSAGE(packet);
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
