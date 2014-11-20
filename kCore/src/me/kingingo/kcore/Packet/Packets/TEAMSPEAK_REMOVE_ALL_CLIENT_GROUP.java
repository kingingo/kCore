package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP extends Packet{

	@Setter
	@Getter
	String identit�t;	
	@Getter
	@Setter
	String inGameName;
	
	public TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(){}
	
	public TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(String[] packet){
		Set(packet);
	}
	
	public TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP create(String[] packet){
		return new TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(packet);
	}
	
	public TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP(String identit�t,String inGameName){
		this.identit�t=identit�t;
		this.inGameName=inGameName;
	}
	
	public String getName(){
		return "TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP";
	}
	
	public void Set(String[] split){
		identit�t=split[1];
		inGameName=split[2];
	}
	
	//TEAMSPEAK_REMOVE_ALL_CLIENT_GROUP-/-IDENTIT�T-/-InGame
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s", getIdentit�t(),getInGameName());
	}

	@Override
	public void Set(String split) {
		String[] sp = split.split("-/-");
		this.identit�t=sp[1];
		inGameName=sp[2];
	}
	
}
