package me.kingingo.kcore.PacketAPI.Packets;

import java.util.List;

import lombok.Getter;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;

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
		packet=new PacketPlayOutPlayerInfo();
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
	
	public class kPlayerInfoData extends PlayerInfoData{

		public kPlayerInfoData(kPacketPlayOutPlayerInfo packetPlayOutPlayerInfo,kGameProfile profile,String tabName) {
			packetPlayOutPlayerInfo.getPacket().super(profile, 0, EnumGamemode.NOT_SET, CraftChatMessage.fromString(tabName)[0]);
		}
		
		public kGameProfile getGameProfile(){
			return (kGameProfile)UtilReflection.getValue("d", this);
		}
		
	}
	
}
