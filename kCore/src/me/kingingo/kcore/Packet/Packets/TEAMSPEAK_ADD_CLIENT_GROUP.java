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
	String identität;
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
	
	public TEAMSPEAK_ADD_CLIENT_GROUP(String identität,TeamspeakGroup group,String inGameName){
		this.identität=identität;
		this.groupId=group.getId();
		this.inGameName=inGameName;
	}
	
	public TEAMSPEAK_ADD_CLIENT_GROUP(String identität,int groupId,String inGameName){
		this.identität=identität;
		this.groupId=groupId;
		this.inGameName=inGameName;
	}
	
	public String getName(){
		return "TEAMSPEAK_ADD_CLIENT_GROUP";
	}
	
	public void Set(String[] split){
		identität=split[1];
		groupId=Integer.valueOf(split[2]);
		inGameName=split[3];
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		identität=split[1];
		groupId=Integer.valueOf(split[2]);
		inGameName=split[3];
	}
	
	//TEAMSPEAK_ADD_CLIENT_GROUP-/-IDENTITÄT-/-groupId-/-InGameName
	public String toString(){
		return String.format(getName()+"-/-%s-/-%d-/-%s", getIdentität(),getGroupId(),getInGameName());
	}
	
}
