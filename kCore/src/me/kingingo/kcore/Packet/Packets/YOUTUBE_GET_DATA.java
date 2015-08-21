package me.kingingo.kcore.Packet.Packets;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.PacketState;
import me.kingingo.kcore.Enum.TeamspeakGroup;
import me.kingingo.kcore.Packet.Packet;

public class YOUTUBE_GET_DATA extends Packet{

	@Getter
	@Setter
	String Channel;
	@Getter
	@Setter
	PacketState packetState;
	@Getter
	@Setter
	int data;
	@Getter
	@Setter
	YouTubeData dataType;
	
	public YOUTUBE_GET_DATA(){}
	
	public YOUTUBE_GET_DATA(String packet){
		Set(packet);
	}
	
	public YOUTUBE_GET_DATA(String[] packet){
		Set(packet);
	}
	
	public YOUTUBE_GET_DATA create(String[] packet){
		return new YOUTUBE_GET_DATA(packet);
	}
	
	public YOUTUBE_GET_DATA(int data,YouTubeData dataType,String Channel,PacketState packetState){
		this.Channel=Channel;
		this.data=data;
		this.dataType=dataType;
		this.packetState=packetState;
	}
	
	public String getName(){
		return "YOUTUBE_GET_DATA";
	}
	
	public void Set(String[] split){
		setChannel(split[1]);
		setData(Integer.valueOf(split[2]));
		setPacketState(PacketState.valueOf(split[3]));
		setDataType(YouTubeData.valueOf(split[4]));
	}
	
	public void Set(String packet){
		String[] split = packet.split("-/-");
		setChannel(split[1]);
		setData(Integer.valueOf(split[2]));
		setPacketState(PacketState.valueOf(split[3]));
		setDataType(YouTubeData.valueOf(split[4]));
	}
	
	//YOUTUBE_GET_DATA-/-Channel-/-DATA-/-PacketState-/-YOUTUBEDATA
	public String toString(){
		return String.format(getName()+"-/-%s-/-%d-/-%s-/-%s", getChannel(),getData(),getPacketState().toString(),getDataType().toString());
	}

	public enum YouTubeData{
		ABONNENTEN,
		VIEWS;
	}
	
}
