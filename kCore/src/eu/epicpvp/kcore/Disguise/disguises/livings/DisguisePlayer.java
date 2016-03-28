package eu.epicpvp.kcore.Disguise.disguises.livings;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseHuman;
import eu.epicpvp.kcore.PacketAPI.kPacket;
import eu.epicpvp.kcore.PacketAPI.Packets.kGameProfile;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutNamedEntitySpawn;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutPlayerInfo;
import eu.epicpvp.kcore.PacketAPI.Packets.kPlayerInfoData;
import eu.epicpvp.kcore.Util.SkinData;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;

public class DisguisePlayer extends DisguiseHuman
{
  private String _name;
  @Getter
  private UUID uuid;
  @Getter
  private kGameProfile profile;
  @Getter
  private UUID skinUuid;
  @Getter
  private boolean tab=false;
  
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
    this.profile=new kGameProfile(this.uuid, this._name);
  }
  
  public void loadSkin(SkinData data){
	  if(data!=null&&data.isReady()){
		  this.profile.loadSkin(data);
		  if(UtilServer.getDisguiseManager().getSkinLoad().contains(this))UtilServer.getDisguiseManager().getSkinLoad().remove(this);
	  }else{
		 if(data!=null){
			 this.skinUuid=data.getUuid();
			 if(!UtilServer.getDisguiseManager().getSkinLoad().contains(this))UtilServer.getDisguiseManager().getSkinLoad().add(this);
		 }
	  }
  }
  
  public void loadSkin(JavaPlugin instance){
	  loadSkin(instance,this.uuid);
  }
  
  public void loadSkin(JavaPlugin instance,UUID uuid){
	  this.profile.loadSkin(instance,uuid);
  }
  
  public String getName(){
	  return this._name;
  }
  
  public kPacket updateTabList(String prefix) {
      try {
         kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();
         kPlayerInfoData data = new kPlayerInfoData(packet,this.profile,prefix+this._name);
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
  
  public kPacket getTabList(String prefix) {
      try {
         kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();
         PlayerInfoData data = new kPlayerInfoData(packet,this.profile, prefix+this._name);
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
  
  public kPacket getTabList() {
      try {
         kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();
         PlayerInfoData data = new kPlayerInfoData(packet,this.profile, this._name);
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
	         PlayerInfoData data = new kPlayerInfoData(packet,this.profile, _name);
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