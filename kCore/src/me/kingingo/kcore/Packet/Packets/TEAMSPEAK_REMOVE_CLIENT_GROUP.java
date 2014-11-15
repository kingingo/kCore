package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.TeamspeakGroup;
import me.kingingo.kcore.Packet.Packet;

public class TEAMSPEAK_REMOVE_CLIENT_GROUP extends Packet{

	@Setter
	@Getter
	int groupId;
	@Setter
	@Getter
	String identität;
	
	public TEAMSPEAK_REMOVE_CLIENT_GROUP(){}
	
	public TEAMSPEAK_REMOVE_CLIENT_GROUP(String packet){
		Set(packet);
	}
	
	public TEAMSPEAK_REMOVE_CLIENT_GROUP(String[] packet){
		Set(packet);
	}
	
	public TEAMSPEAK_REMOVE_CLIENT_GROUP create(String[] packet){
		return new TEAMSPEAK_REMOVE_CLIENT_GROUP(packet);
	}
	
	public TEAMSPEAK_REMOVE_CLIENT_GROUP(String identität,int groupId){
		this.identität=identität;
		this.groupId=groupId;
	}
	
	public TEAMSPEAK_REMOVE_CLIENT_GROUP(String identität,TeamspeakGroup group){
		this.identität=identität;
		this.groupId=group.getId();
	}
	
	public String getName(){
		return "TEAMSPEAK_REMOVE_CLIENT_GROUP";
	}
	
	public void Set(String[] split){
		identität=split[1];
		groupId=Integer.valueOf(split[2]);
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		identität=split[1];
		groupId=Integer.valueOf(split[2]);
	}
	
	//TEAMSPEAK_ADD_CLIENT_GROUP-/-IDENTITÄT-/-groupId
	public String toString(){
		return String.format(getName()+"-/-%s-/-%d", getIdentität(),getGroupId());
	}
	
}
