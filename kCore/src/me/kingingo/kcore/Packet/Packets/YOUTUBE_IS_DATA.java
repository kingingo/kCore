package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.PacketState;
import me.kingingo.kcore.Enum.TeamspeakGroup;
import me.kingingo.kcore.Packet.Packet;

public class YOUTUBE_IS_DATA extends Packet{

	@Getter
	@Setter
	String Channel;
	@Getter
	@Setter
	PacketState packetState;
	@Getter
	@Setter
	String inGameName;
	@Getter
	@Setter
	YouTubeIsData datatype;
	
	public YOUTUBE_IS_DATA(){}
	
	public YOUTUBE_IS_DATA(String packet){
		Set(packet);
	}
	
	public YOUTUBE_IS_DATA(String[] packet){
		Set(packet);
	}
	
	public YOUTUBE_IS_DATA create(String[] packet){
		return new YOUTUBE_IS_DATA(packet);
	}
	
	public YOUTUBE_IS_DATA(String inGameName,String Channel,PacketState packetState){
		this.Channel=Channel;
		this.inGameName=inGameName;
		this.packetState=packetState;
	}
	
	public String getName(){
		return "YOUTUBE_IS_DATA";
	}
	
	public void Set(String[] split){
		setChannel(split[1]);
		setInGameName(split[2]);
		setPacketState(PacketState.valueOf(split[3]));
		setDatatype(YouTubeIsData.valueOf(split[4]));
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		setChannel(split[1]);
		setInGameName(split[2]);
		setPacketState(PacketState.valueOf(split[3]));
		setDatatype(YouTubeIsData.valueOf(split[4]));
	}
	
	//YOUTUBE_IS_DATA-/-Channel-/-InGame-/-PacketState-/-YouTubeIsData
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s-/-%s", getChannel(),getInGameName(),getPacketState().toString(),getDatatype().toString());
	}
	
	public enum YouTubeIsData{
		IS_FOLLOWING,
		IS_THE_YT_USER;
	}
	
}
