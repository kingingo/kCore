package eu.epicpvp.kcore.Disguise.disguises.livings;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import eu.epicpvp.datenserver.definitions.skin.Skin;
import eu.epicpvp.kcore.Disguise.disguises.DisguiseHuman;
import eu.epicpvp.kcore.PacketAPI.PacketWrapper;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperGameProfile;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPacketPlayOutNamedEntitySpawn;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPacketPlayOutPlayerInfo;
import eu.epicpvp.kcore.PacketAPI.Packets.WrapperPlayerInfoData;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilSkin;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;

public class DisguisePlayer extends DisguiseHuman
{
  private String _name;
  @Getter
  private UUID uuid;
  @Getter
  private WrapperGameProfile profile;
  @Getter
  private UUID skinUuid;
  @Getter
  private boolean tab=false;

  public DisguisePlayer(org.bukkit.entity.Entity entity, Player player)
  {
	  super(entity);

	  this._name = player.getName();
	  this.uuid=entity.getUniqueId();
	  this.DataWatcher.setCustomName(_name);
	  this.profile=new WrapperGameProfile( UtilPlayer.getCraftPlayer(player).getProfile() );
  }

  public DisguisePlayer(org.bukkit.entity.Entity entity, String name)
  {
    super(entity);

    if(name==null)System.err.println("[DisguisePlayer] name ist gleich null");

    if (name.length() > 14)
    {
      name = name.substring(0, 14);
    }

    this._name = name;
    this.uuid=entity.getUniqueId();
    this.DataWatcher.setCustomName(_name);
    this.profile=new WrapperGameProfile(this.uuid, this._name);
  }

  public void loadSkin(UUID uuid){
	  UtilSkin.loadSkin(this.profile, uuid);
  }

  public void loadSkin(String playerName){
	  UtilSkin.loadSkin(this.profile, playerName);
  }

  public void loadSkin(Skin data){
	  if(data!=null){
		  this.profile.loadSkin(data);
		  UtilServer.getDisguiseManager().updateDisguise(this);
	  }
  }

  public String getName(){
	  return this._name;
  }

  public PacketWrapper updateTabList(String prefix) {
      try {
         WrapperPacketPlayOutPlayerInfo packet = new WrapperPacketPlayOutPlayerInfo();
         WrapperPlayerInfoData data = new WrapperPlayerInfoData(packet,this.profile,prefix+this._name);
         List<PlayerInfoData> players = packet.getList();
         players.add(data);

         packet.setEnumPlayerInfoAction(EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
         packet.setList(players);

         return packet;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return null;
   }

  public PacketWrapper getTabList(String prefix) {
      try {
         WrapperPacketPlayOutPlayerInfo packet = new WrapperPacketPlayOutPlayerInfo();
         PlayerInfoData data = new WrapperPlayerInfoData(packet,this.profile, prefix+this._name);
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

  public PacketWrapper getTabList() {
      try {
         WrapperPacketPlayOutPlayerInfo packet = new WrapperPacketPlayOutPlayerInfo();
         PlayerInfoData data = new WrapperPlayerInfoData(packet,this.profile, this._name);
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

  public PacketWrapper removeFromTablist() {
	   try {
	         WrapperPacketPlayOutPlayerInfo packet = new WrapperPacketPlayOutPlayerInfo();
	         PlayerInfoData data = new WrapperPlayerInfoData(packet,this.profile, _name);
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

  public PacketWrapper GetSpawnPacket(){
	  WrapperPacketPlayOutNamedEntitySpawn packet = new WrapperPacketPlayOutNamedEntitySpawn();
	  packet.setEntityID(this.Entity.getId());
	  packet.setUUID(uuid);
	  packet.setX(this.Entity.locX);
	  packet.setY(this.Entity.locY);
	  packet.setZ(this.Entity.locZ);
	  packet.setYaw(this.Entity.yaw);
	  packet.setPitch(this.Entity.pitch);
	  packet.setDataWatcher(this.DataWatcher);
	  return packet;
  }

  public EntityType GetEntityTypeId() {
	return EntityType.PLAYER;
  }
}
