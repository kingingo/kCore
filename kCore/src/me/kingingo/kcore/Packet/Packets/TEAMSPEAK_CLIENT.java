package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.TeamspeakGroup;
import me.kingingo.kcore.Packet.Packet;

public class TEAMSPEAK_CLIENT extends Packet{

	@Setter
	@Getter
	int groupId;
	@Setter
	@Getter
	String identität;
	@Getter
	@Setter
	Teamspeak ts;
	@Getter
	@Setter
	String inGameName;
	
	public TEAMSPEAK_CLIENT(){}
	
	public TEAMSPEAK_CLIENT(String packet){
		Set(packet);
	}
	
	public TEAMSPEAK_CLIENT(String[] packet){
		Set(packet);
	}
	
	public TEAMSPEAK_CLIENT create(String[] packet){
		return new TEAMSPEAK_CLIENT(packet);
	}
	
	public TEAMSPEAK_CLIENT(String identität,TeamspeakGroup group,Teamspeak ts,String inGameName){
		this.identität=identität;
		this.ts=ts;
		this.groupId=group.getId();
		this.inGameName=inGameName;
	}
	
	public TEAMSPEAK_CLIENT(String identität,int groupId,Teamspeak ts,String inGameName){
		this.identität=identität;
		this.groupId=groupId;
		this.ts=ts;
		this.inGameName=inGameName;
	}
	
	public String getName(){
		return "TEAMSPEAK_CLIENT";
	}
	
	public void Set(String[] split){
		identität=split[1];
		groupId=Integer.valueOf(split[2]);
		ts=Teamspeak.valueOf(split[3]);
		inGameName=split[4];
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		identität=split[1];
		groupId=Integer.valueOf(split[2]);
		ts=Teamspeak.valueOf(split[3]);
		inGameName=split[4];
	}
	
	//TEAMSPEAK_CLIENT-/-IDENTITÄT-/-groupId-/-Teamspeak.GROUP_ADDED.toString()-/-InGameName
	public String toString(){
		return String.format(getName()+"-/-%s-/-%d-/-%s-/-%s", getIdentität(),getGroupId(),ts.toString(),getInGameName());
	}
	
	public enum Teamspeak{
		GROUP_ADDED,
		USER_NOT_FOUND,
		GROUP_REMOVED;		
	}
	
}
