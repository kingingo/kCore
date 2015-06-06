package me.kingingo.kcore.NPC;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kGameProfile;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kPacketPlayOutBed;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kPacketPlayOutEntityDestroy;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kPacketPlayOutEntityEquipment;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kPacketPlayOutEntityMetadata;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kPacketPlayOutEntityTeleport;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kPacketPlayOutNamedEntitySpawn;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kPacketPlayOutPlayerInfo;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kPacketPlayOutRelEntityMoveLook;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R2.DataWatcher;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerInfo.PlayerInfoData;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

public class NPC {
   
   private NPCManager manager=null;
   private DataWatcher watcher;
   private Material chestplate;
   private Material leggings;
   private Location location;
   private Material inHand;
   private Material helmet;
   private Material boots;
   private String tablist;
   private int entityID;
   private String name;
   private UUID uuid;
   
   @Getter
   private kPacketPlayOutNamedEntitySpawn spawn_packet;
   
   public NPC(NPCManager manager,String name, String tablist, UUID uuid, int entityID, Location location, Material inHand) {
      this.location = location;
      this.manager=manager;
      this.tablist = tablist;
      this.name = name;
      this.uuid = uuid;
      this.entityID = entityID;
      this.inHand = inHand;
      this.watcher = new DataWatcher(null);
      watcher.a(6, (float) 20);
   }
   
   public NPC(NPCManager manager,String name, Location location) {
      this(manager,name, name, UUID.randomUUID(), new Random().nextInt(10000), location, Material.AIR);
   }
   
   public NPC(NPCManager manager,String name, Location location, Material inHand) {
      this(manager,name, name, UUID.randomUUID(), new Random().nextInt(10000), location, inHand);
   }
   
   public NPC(NPCManager manager,String name, String tablist, Location location) {
      this(manager,name, tablist, UUID.randomUUID(), new Random().nextInt(10000), location, Material.AIR);
   }
   
   public NPC(NPCManager manager,String name, String tablist, Location location, Material inHand) {
      this(manager,name, tablist, UUID.randomUUID(), new Random().nextInt(10000), location, inHand);
   }
   

   public void spawn() {
      try{
    	 spawn_packet = new kPacketPlayOutNamedEntitySpawn();
         this.addToTablist();
         
         spawn_packet.setEntityID(entityID);
         spawn_packet.setUUID(uuid);
         spawn_packet.setLocation(location);
         spawn_packet.setItemInHand(inHand);
         spawn_packet.setDataWatcher(watcher);
         
         if(manager!=null)this.manager.getNPCList().put(this.entityID, this);
         
         for(Player online : Bukkit.getOnlinePlayers())UtilPlayer.sendPacket(online, spawn_packet);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   

   public void despawn() {
      kPacketPlayOutEntityDestroy packet = new kPacketPlayOutEntityDestroy(new int[]{this.entityID});
      this.removeFromTablist();
      if(manager!=null)this.manager.getNPCList().remove(this.entityID);
      for(Player online : Bukkit.getOnlinePlayers())UtilPlayer.sendPacket(online, packet);
   }
   
   public void walk(Location newLoc) {
       walk(newLoc.getBlockX(),newLoc.getBlockY(),newLoc.getBlockZ(), getLocation().getYaw(), getLocation().getPitch());
   }
   
   public void walk(Location newLoc,float yaw,float pitch) {
	   walk(newLoc.getBlockX(),newLoc.getBlockY(),newLoc.getBlockZ(), yaw,pitch);
   }

   public byte getCompressedAngle(float value) {
	   return (byte) ((value * 256.0F) / 360.0F);
   }

   public void walk(int xs ,int ys,int zs, float yaw, float pitch) {
	   byte x = (byte)((xs - getLocation().getBlockX()) * 32);
	   byte y = (byte)((ys - getLocation().getBlockY()) * 32);
	   byte z = (byte)((zs - getLocation().getBlockZ()) * 32);
	   this.location.add((xs - getLocation().getBlockX()), (ys - getLocation().getBlockY()), (zs - getLocation().getBlockZ()));
       kPacketPlayOutRelEntityMoveLook packet = new kPacketPlayOutRelEntityMoveLook(this.entityID,x,y,z,getCompressedAngle(yaw),getCompressedAngle(pitch),true);

       for (Player online : UtilServer.getPlayers()) UtilPlayer.sendPacket(online, packet);
   }
   
   public void sleep(){
		try {
			kPacketPlayOutBed packet = new kPacketPlayOutBed();
			
			packet.setEntityID(entityID);
			packet.setPosition(location);
			
			for (Player online : UtilServer.getPlayers()) UtilPlayer.sendPacket(online, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
   }
   

   public void changePlayerlistName(String name) {
      try{
         kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();
         
         PlayerInfoData data = packet.new kPlayerInfoData(packet, new kGameProfile(getUUID(), name),name);
         List<PlayerInfoData> players = packet.getList();
         players.add(data);

         packet.setEnumPlayerInfoAction(EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
         packet.setList(players);
         this.tablist = name;

         for (Player online : UtilServer.getPlayers()) UtilPlayer.sendPacket(online, packet);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   

   private void addToTablist() {
      try {
         kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();
         PlayerInfoData data = packet.new kPlayerInfoData(packet,new kGameProfile(this.uuid, this.name), tablist);
         List<PlayerInfoData> players = packet.getList();
         players.add(data);
         
         packet.setEnumPlayerInfoAction(EnumPlayerInfoAction.ADD_PLAYER);
         packet.setList(players);

         for (Player online : UtilServer.getPlayers()) UtilPlayer.sendPacket(online, packet);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   

   private void removeFromTablist() {
	   try {
	         kPacketPlayOutPlayerInfo packet = new kPacketPlayOutPlayerInfo();
	         PlayerInfoData data = packet.new kPlayerInfoData(packet,new kGameProfile(this.uuid, this.name), tablist);
	         List<PlayerInfoData> players = packet.getList();
	         players.add(data);
	         
	         packet.setEnumPlayerInfoAction(EnumPlayerInfoAction.REMOVE_PLAYER);
	         packet.setList(players);
	         
	         for (Player online : UtilServer.getPlayers()) UtilPlayer.sendPacket(online, packet);
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
   }
   

   public void teleport(Location location) {
      try{
         kPacketPlayOutEntityTeleport packet = new kPacketPlayOutEntityTeleport();
         
         packet.setEntityID(entityID);
         packet.setLocation(location);
         packet.setOnGround(this.location.getBlock().getType() == Material.AIR ? false : true);
         this.location = location;
         
         for (Player online : UtilServer.getPlayers()) UtilPlayer.sendPacket(online, packet);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   

   public void setItemInHand(Material material) {
      try{
         kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(this.entityID,0,material);
         this.inHand = material;
         for (Player online : UtilServer.getPlayers()) UtilPlayer.sendPacket(online, packet);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public Material getItemInHand() {
      return this.inHand;
   }
   

   public void setHelmet(Material material) {
      try{
         kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(this.entityID,4,material);
         
         this.helmet=material;
         for (Player online : UtilServer.getPlayers()) UtilPlayer.sendPacket(online, packet);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public Material getHelmet() {
      return this.helmet;
   }
   

   public void setChestplate(Material material) {
      try{
         kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(entityID,3,material);
         
         this.chestplate = material;
         for (Player online : UtilServer.getPlayers()) UtilPlayer.sendPacket(online, packet);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public void setName(String s) {
       this.watcher.a(0, (Object) (byte) 0);
       this.watcher.a(1, (Object) (short) 300);
       this.watcher.a(3, (Object) (byte) 0);
       this.watcher.a(2, (Object) (String) s);
       this.watcher.a(4, (Object) (byte) 0);
       kPacketPlayOutEntityMetadata packet = new kPacketPlayOutEntityMetadata(this.entityID, this.watcher);
       for (Player online : UtilServer.getPlayers()) UtilPlayer.sendPacket(online, packet);
   }
   
   public Material getChestplate() {
      return this.chestplate;
   }
   

   public void setLeggings(Material material) {
      try{
         kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(entityID,2,material);
         
         this.leggings = material;
         for (Player online : UtilServer.getPlayers()) UtilPlayer.sendPacket(online, packet);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public Material getLeggings() {
      return this.leggings;
   }
   
   public void setBoots(Material material) {
      try{
         kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(entityID,1,material);
         this.boots = material;
         for (Player online : UtilServer.getPlayers()) UtilPlayer.sendPacket(online, packet);
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public Material getBoots() {
      return this.boots;
   }
   
   public int getEntityID() {
      return this.entityID;
   }
   
   public UUID getUUID() {
      return this.uuid;
   }
   
   public Location getLocation() {
      return this.location;
   }
   
   public String getName() {
      return this.name;
   }
   
   public String getPlayerlistName() {
      return this.tablist;
   }
}