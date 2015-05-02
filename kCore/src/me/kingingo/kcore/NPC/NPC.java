package me.kingingo.kcore.NPC;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.DataWatcher;
import net.minecraft.server.v1_8_R2.PacketPlayOutBed;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.minecraft.server.v1_8_R2.WorldSettings.EnumGamemode;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
   private PacketPlayOutNamedEntitySpawn spawn_packet;
   
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
    	 spawn_packet = new PacketPlayOutNamedEntitySpawn();
         this.addToTablist();
         
         this.setValue(spawn_packet, "a", this.entityID);
         this.setValue(spawn_packet, "b", this.uuid);
         this.setValue(spawn_packet, "c", this.toFixedPoint(this.location.getX()));
         this.setValue(spawn_packet, "d", this.toFixedPoint(this.location.getY()));
         this.setValue(spawn_packet, "e", this.toFixedPoint(this.location.getZ()));
         this.setValue(spawn_packet, "f", this.toPackedByte(this.location.getYaw()));
         this.setValue(spawn_packet, "g", this.toPackedByte(this.location.getPitch()));
         this.setValue(spawn_packet, "h", this.inHand == null ? 0 : this.inHand.getId());
         this.setValue(spawn_packet, "i", this.watcher);
         
         if(manager!=null)this.manager.getNPCList().put(this.entityID, this);
         
         for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(spawn_packet);
         }
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   

   public void despawn() {
      PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{this.entityID});
      this.removeFromTablist();
      if(manager!=null)this.manager.getNPCList().remove(this.entityID);
      for(Player online : Bukkit.getOnlinePlayers()) {
         ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
      }
   }
   
   public void walk(Location newLoc) {
       walk(newLoc, getLocation().getYaw(), getLocation().getPitch());
   }

   public byte getCompressedAngle(float value) {
	   return (byte) ((value * 256.0F) / 360.0F);
   }

   public void walk(Location newLoc, float yaw, float pitch) {
	   byte x = (byte)((newLoc.getBlockX() - getLocation().getBlockX()) * 32);
	   byte y = (byte)((newLoc.getBlockY() - getLocation().getBlockY()) * 32);
	   byte z = (byte)((newLoc.getBlockZ() - getLocation().getBlockZ()) * 32);
	   this.location.add((newLoc.getBlockX() - getLocation().getBlockX()), (newLoc.getBlockY() - getLocation().getBlockY()), (newLoc.getBlockZ() - getLocation().getBlockZ()));
       PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutRelEntityMoveLook(this.entityID,x,y,z,getCompressedAngle(yaw),getCompressedAngle(pitch),true);

       for (Player online : UtilServer.getPlayers()) {
           ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
       }
   }
   
   public void sleep(){
		try {
			PacketPlayOutBed packet = new PacketPlayOutBed();
			
			setValue(packet,"a", this.entityID);
			setValue(packet,"b", new BlockPosition(this.toFixedPoint(this.location.getX()),this.toFixedPoint(this.location.getY()),this.toFixedPoint(this.location.getZ())));
			
			for(Player online : Bukkit.getOnlinePlayers()) {
	            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
	         }
		} catch (Exception e) {
			e.printStackTrace();
		}
   }
   

   public void changePlayerlistName(String name) {
      try{
         PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
         
         PlayerInfoData data = packet.new PlayerInfoData(new GameProfile(this.uuid, this.name), 0, EnumGamemode.NOT_SET, CraftChatMessage.fromString(name)[0]);
         @SuppressWarnings("unchecked") List<PlayerInfoData> players = (List<PlayerInfoData>) this.getValue(packet, "b");
         players.add(data);
         
         this.setValue(packet, "a", EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
         this.setValue(packet, "b", players);
         this.tablist = name;
         
         for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
         }
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   

   private void addToTablist() {
      try {
         PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
         PlayerInfoData data = packet.new PlayerInfoData(new GameProfile(this.uuid, this.name), 0, EnumGamemode.NOT_SET, CraftChatMessage.fromString(this.tablist)[0]);
         @SuppressWarnings("unchecked") List<PlayerInfoData> players = (List<PlayerInfoData>) this.getValue(packet, "b");
         players.add(data);
         
         this.setValue(packet, "a", EnumPlayerInfoAction.ADD_PLAYER);
         this.setValue(packet, "b", players);
         
         for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
   

   private void removeFromTablist() {
      try{
         PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
         PlayerInfoData data = packet.new PlayerInfoData(new GameProfile(this.uuid, this.name), 0, EnumGamemode.NOT_SET, CraftChatMessage.fromString(this.tablist)[0]);
         @SuppressWarnings("unchecked") List<PlayerInfoData> players = (List<PlayerInfoData>) this.getValue(packet, "b");
         players.add(data);
         
         this.setValue(packet, "a", EnumPlayerInfoAction.REMOVE_PLAYER);
         this.setValue(packet, "b", players);
         
         for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
         }
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   

   public void teleport(Location location) {
      try{
         PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
         
         this.setValue(packet, "a", this.entityID);
         this.setValue(packet, "b", this.toFixedPoint(location.getX()));
         this.setValue(packet, "c", this.toFixedPoint(location.getY()));
         this.setValue(packet, "d", this.toFixedPoint(location.getZ()));
         this.setValue(packet, "e", this.toPackedByte(location.getYaw()));
         this.setValue(packet, "f", this.toPackedByte(location.getPitch()));
         this.setValue(packet, "g", this.location.getBlock().getType() == Material.AIR ? false : true);
         this.location = location;
         
         for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
         }
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   

   public void setItemInHand(Material material) {
      try{
         PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
         
         this.setValue(packet, "a", this.entityID);
         this.setValue(packet, "b", 0);
         this.setValue(packet, "c", material == Material.AIR || material == null ? CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)) : CraftItemStack.asNMSCopy(new ItemStack(material)));
         this.inHand = material;
         
         for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
         }
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public Material getItemInHand() {
      return this.inHand;
   }
   

   public void setHelmet(Material material) {
      try{
         PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
         
         this.setValue(packet, "a", this.entityID);
         this.setValue(packet, "b", 4);
         this.setValue(packet, "c", material == Material.AIR || material == null ? CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)) : CraftItemStack.asNMSCopy(new ItemStack(material)));
         this.helmet = material;
         
         for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
         }
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public Material getHelmet() {
      return this.helmet;
   }
   

   public void setChestplate(Material material) {
      try{
         PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
         
         this.setValue(packet, "a", this.entityID);
         this.setValue(packet, "b", 3);
         this.setValue(packet, "c", material == Material.AIR || material == null ? CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)) : CraftItemStack.asNMSCopy(new ItemStack(material)));
         this.chestplate = material;
         
         for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
         }
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
       PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(this.entityID, this.watcher, true);
       for (Player online : Bukkit.getOnlinePlayers())((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
   }
   
   public Material getChestplate() {
      return this.chestplate;
   }
   

   public void setLeggings(Material material) {
      try{
         PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
         
         this.setValue(packet, "a", this.entityID);
         this.setValue(packet, "b", 2);
         this.setValue(packet, "c", material == Material.AIR || material == null ? CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)) : CraftItemStack.asNMSCopy(new ItemStack(material)));
         this.leggings = material;
         
         for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
         }
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   public Material getLeggings() {
      return this.leggings;
   }
   
   public void setBoots(Material material) {
      try{
         PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
         
         this.setValue(packet, "a", this.entityID);
         this.setValue(packet, "b", 1);
         this.setValue(packet, "c", material == Material.AIR || material == null ? CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)) : CraftItemStack.asNMSCopy(new ItemStack(material)));
         this.boots = material;
         
         for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
         }
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
   
   private void setValue(Object instance, String field, Object value) throws Exception {
      Field f = instance.getClass().getDeclaredField(field);
      f.setAccessible(true);
      f.set(instance, value);
   }
   
   private Object getValue(Object instance, String field) throws Exception {
      Field f = instance.getClass().getDeclaredField(field);
      f.setAccessible(true);
      return f.get(instance);
   }
   
   private int toFixedPoint(double d) {
      return (int) (d * 32.0);
   }
   
   private byte toPackedByte(float f) {
      return (byte) ((int) (f * 256.0F / 360.0F));
   }
   
}