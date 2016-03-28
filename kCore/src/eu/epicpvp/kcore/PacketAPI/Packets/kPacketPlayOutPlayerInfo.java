package eu.epicpvp.kcore.PacketAPI.Packets;

import java.util.List;

import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;

public class kPacketPlayOutPlayerInfo implements kPacket{
	@Getter
	private PacketPlayOutPlayerInfo packet;
	private String PLAYER_INFO_DATA_LIST = "b";
	private String ENUM_PLAYER_INFO_ACTION = "a";
	
	public kPacketPlayOutPlayerInfo(){
		packet=new PacketPlayOutPlayerInfo();
	}
	
	public kPacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo packet){
		this.packet=packet;
	}
	
	public kPacketPlayOutPlayerInfo(EnumPlayerInfoAction action,EntityPlayer[] players){
		packet=new PacketPlayOutPlayerInfo(action,players);
	}
	
	public void setEnumPlayerInfoAction(EnumPlayerInfoAction action){
		UtilReflection.setValue(ENUM_PLAYER_INFO_ACTION, packet, action);
	}
	
	public EnumPlayerInfoAction getEnumPlayerInfoAction(){
		return (EnumPlayerInfoAction) UtilReflection.getValue(ENUM_PLAYER_INFO_ACTION, packet);
	}
	
	public void setList(List<PlayerInfoData> list){
		UtilReflection.setValue(PLAYER_INFO_DATA_LIST, packet, list);
	}
	
	public List<PlayerInfoData> getList(){
		return (List<PlayerInfoData>) UtilReflection.getValue(PLAYER_INFO_DATA_LIST, packet);
	}
}
