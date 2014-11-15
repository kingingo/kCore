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
	
	public TEAMSPEAK_ADD_CLIENT_GROUP(String identit�t,TeamspeakGroup group){
		this.identit�t=identit�t;
		this.groupId=group.getId();
	}
	
	public TEAMSPEAK_ADD_CLIENT_GROUP(String identit�t,int groupId){
		this.identit�t=identit�t;
		this.groupId=groupId;
	}
	
	public String getName(){
		return "TEAMSPEAK_ADD_CLIENT_GROUP";
	}
	
	public void Set(String[] split){
		identit�t=split[1];
		groupId=Integer.valueOf(split[2]);
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		identit�t=split[1];
		groupId=Integer.valueOf(split[2]);
	}
	
	//TEAMSPEAK_ADD_CLIENT_GROUP-/-IDENTIT�T-/-groupId
	public String toString(){
		return String.format(getName()+"-/-%s-/-%d", getIdentit�t(),getGroupId());
	}
	
}
