package me.kingingo.kcore.Disguise.disguises.livings;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.kingingo.kcore.Disguise.disguises.DisguiseHuman;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.PacketAPI.Packets.kDataWatcher;
import me.kingingo.kcore.PacketAPI.Packets.kGameProfile;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutNamedEntitySpawn;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo.kPlayerInfoData;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;

public class DisguisePlayer extends DisguiseHuman
{
  private String _name;

  public DisguisePlayer(org.bukkit.entity.Entity entity, String name)
  {
    super(entity);
    
    if (name.length() > 16)
    {
      name = name.substring(0, 16);
    }

    this._name = name;
    this.DataWatcher.setCustomName(_name);
  }
  
  public String getName(){
	  return this._name;
  }
  
  public kPacket getTabList() {
      try {
         kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();
         PlayerInfoData data = packet.new kPlayerInfoData(packet,new kGameProfile(this.Entity.getUniqueID(), this._name), this._name);
         List<PlayerInfoData> players = packet.getList();
         players.add(data);
         
         packet.setEnumPlayerInfoAction(EnumPlayerInfoAction.ADD_PLAYER);
         packet.setList(players);

         return packet;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

  public kPacket removeFromTablist() {
	   try {
	         kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();
	         PlayerInfoData data = packet.new kPlayerInfoData(packet,new kGameProfile(this.Entity.getUniqueID(), this._name), _name);
	         List<PlayerInfoData> players = packet.getList();
	         players.add(data);
	         
	         packet.setEnumPlayerInfoAction(EnumPlayerInfoAction.REMOVE_PLAYER);
	         packet.setList(players);
	         
	         return packet;
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   return null;
  }
  
  public kPacket GetSpawnPacket(){
	  kPacketPlayOutNamedEntitySpawn packet = new kPacketPlayOutNamedEntitySpawn();
	  packet.setEntityID(this.Entity.getId());
	  packet.setUUID(this.Entity.getUniqueID());
	  packet.setX(this.Entity.locX);
	  packet.setY(this.Entity.locY);
	  packet.setZ(this.Entity.locZ);
	  packet.setYaw(this.Entity.yaw);
	  packet.setPitch(this.Entity.pitch);
	  packet.setDataWatcher(this.DataWatcher);
	  return packet;
  }
}