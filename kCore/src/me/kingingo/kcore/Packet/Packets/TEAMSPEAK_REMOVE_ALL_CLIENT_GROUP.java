package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP extends Packet{

	@Setter
	@Getter
	String identit�t;
	
	public TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(){}
	
	public TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(String[] packet){
		Set(packet);
	}
	
	public TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP create(String[] packet){
		return new TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(packet);
	}
	
	public TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(String identit�t){
		this.identit�t=identit�t;
	}
	
	public String getName(){
		return "TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP";
	}
	
	public void Set(String[] split){
		identit�t=split[1];
	}
	
	//TEAMSPEAK_ADD_CLIENT_GROUP-/-IDENTIT�T
	public String toString(){
		return String.format(getName()+"-/-%s", getIdentit�t());
	}

	@Override
	public void Set(String split) {
		this.identit�t=split;
	}
	
}
