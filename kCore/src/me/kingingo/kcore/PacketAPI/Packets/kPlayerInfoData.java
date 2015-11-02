package me.kingingo.kcore.PacketAPI.Packets;

import me.kingingo.kcore.Util.UtilReflection;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;

import com.mojang.authlib.GameProfile;

public class kPlayerInfoData extends PlayerInfoData{
		
		public kPlayerInfoData(kPacketPlayOutPlayerInfo packetPlayOutPlayerInfo,kGameProfile profile,String tabName) {
			this(packetPlayOutPlayerInfo,(GameProfile)profile,tabName);
		}
		
		public kPlayerInfoData(kPacketPlayOutPlayerInfo packetPlayOutPlayerInfo,GameProfile profile,String tabName) {
			packetPlayOutPlayerInfo.getPacket().super(profile, 0, EnumGamemode.NOT_SET, CraftChatMessage.fromString(tabName)[0]);
		}
		
		public kGameProfile getGameProfile(){
			return (kGameProfile)UtilReflection.getValue("d", this);
		}
		
	}