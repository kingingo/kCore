package eu.epicpvp.kcore.PacketAPI.Packets;

import java.util.List;

import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.Util.UtilReflection;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;

public class WrapperPacketPlayOutPlayerInfo implements PacketWrapper{
	@Getter
	private PacketPlayOutPlayerInfo packet;
	private String PLAYER_INFO_DATA_LIST = "b";
	private String ENUM_PLAYER_INFO_ACTION = "a";
	
	public WrapperPacketPlayOutPlayerInfo(){
		packet=new PacketPlayOutPlayerInfo();
	}
	
	public WrapperPacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo packet){
		this.packet=packet;
	}
	
	public WrapperPacketPlayOutPlayerInfo(EnumPlayerInfoAction action,EntityPlayer[] players){
		packet=new PacketPlayOutPlayerInfo(action,players);
	}
	
	public void setEnumPlayerInfoAction(EnumPlayerInfoAction action){
		UtilReflection.setValue(ENUM_PLAYER_INFO_ACTION, packet, action);
	}
	
	public EnumPlayerInfoAction getEnumPlayerInfoAction(){
		return (EnumPlayerInfoAction) UtilReflection.getValue(ENUM_PLAYER_INFO_ACTION, packet);
	}
	
	public void setEntries(List<WrapperPlayerInfoData> list){
		setList(list);
	}
	
	public void setList(List<? extends PlayerInfoData> list){
		UtilReflection.setValue(PLAYER_INFO_DATA_LIST, packet, list);
	}
	
	public List<PlayerInfoData> getList(){
		return (List<PlayerInfoData>) UtilReflection.getValue(PLAYER_INFO_DATA_LIST, packet);
	}
}
