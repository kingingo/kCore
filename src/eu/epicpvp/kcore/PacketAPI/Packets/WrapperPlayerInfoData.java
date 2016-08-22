package eu.epicpvp.kcore.PacketAPI.Packets;

import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;

import com.mojang.authlib.GameProfile;

import eu.epicpvp.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class WrapperPlayerInfoData extends PlayerInfoData {

	public WrapperPlayerInfoData(WrapperPacketPlayOutPlayerInfo packetPlayOutPlayerInfo, WrapperGameProfile profile, String tabName) {
		this(packetPlayOutPlayerInfo, (GameProfile) profile, tabName);
	}
	
	public WrapperPlayerInfoData(WrapperPacketPlayOutPlayerInfo packetPlayOutPlayerInfo,EnumGamemode enumGamemode, GameProfile profile, String tabName) {
		packetPlayOutPlayerInfo.getPacket().super(profile, 0, enumGamemode, CraftChatMessage.fromString(tabName)[0]);
	}

	public WrapperPlayerInfoData(WrapperPacketPlayOutPlayerInfo packetPlayOutPlayerInfo, GameProfile profile, String tabName) {
		packetPlayOutPlayerInfo.getPacket().super(profile, 0, EnumGamemode.NOT_SET, CraftChatMessage.fromString(tabName)[0]);
	}

	public WrapperGameProfile getGameProfile() {
		return (WrapperGameProfile) UtilReflection.getValue("d", this);
	}

}