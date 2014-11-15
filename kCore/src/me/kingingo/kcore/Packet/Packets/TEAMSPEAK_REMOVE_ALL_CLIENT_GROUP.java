package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP extends Packet{

	@Setter
	@Getter
	String identität;
	
	public TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(){}
	
	public TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(String[] packet){
		Set(packet);
	}
	
	public TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP create(String[] packet){
		return new TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(packet);
	}
	
	public TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(String identität){
		this.identität=identität;
	}
	
	public String getName(){
		return "TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP";
	}
	
	public void Set(String[] split){
		identität=split[1];
	}
	
	//TEAMSPEAK_ADD_CLIENT_GROUP-/-IDENTITÄT
	public String toString(){
		return String.format(getName()+"-/-%s", getIdentität());
	}

	@Override
	public void Set(String split) {
		this.identität=split;
	}
	
}
