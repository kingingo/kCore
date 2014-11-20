package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.TeamspeakGroup;
import me.kingingo.kcore.Packet.Packet;

public class TEAMSPEAK_ADD_CLIENT_GROUP extends Packet{

	@Setter
	@Getter
	int groupId;
	@Setter
	@Getter
	String identit�t;
	@Getter
	@Setter
	String inGameName;
	
	public TEAMSPEAK_ADD_CLIENT_GROUP(){}
	
	public TEAMSPEAK_ADD_CLIENT_GROUP(String packet){
		Set(packet);
	}
	
	public TEAMSPEAK_ADD_CLIENT_GROUP(String[] packet){
		Set(packet);
	}
	
	public TEAMSPEAK_ADD_CLIENT_GROUP create(String[] packet){
		return new TEAMSPEAK_ADD_CLIENT_GROUP(packet);
	}
	
	public TEAMSPEAK_ADD_CLIENT_GROUP(String identit�t,TeamspeakGroup group,String inGameName){
		this.identit�t=identit�t;
		this.groupId=group.getId();
		this.inGameName=inGameName;
	}
	
	public TEAMSPEAK_ADD_CLIENT_GROUP(String identit�t,int groupId,String inGameName){
		this.identit�t=identit�t;
		this.groupId=groupId;
		this.inGameName=inGameName;
	}
	
	public String getName(){
		return "TEAMSPEAK_ADD_CLIENT_GROUP";
	}
	
	public void Set(String[] split){
		identit�t=split[1];
		groupId=Integer.valueOf(split[2]);
		inGameName=split[3];
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		identit�t=split[1];
		groupId=Integer.valueOf(split[2]);
		inGameName=split[3];
	}
	
	//TEAMSPEAK_ADD_CLIENT_GROUP-/-IDENTIT�T-/-groupId-/-InGameName
	public String toString(){
		return String.format(getName()+"-/-%s-/-%d-/-%s", getIdentit�t(),getGroupId(),getInGameName());
	}
	
}
