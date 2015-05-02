package me.kingingo.kcore.AntiHack.Hack;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.AntiHack.AntiHack;
import me.kingingo.kcore.AntiHack.IHack;
import me.kingingo.kcore.AntiHack.Level;
import me.kingingo.kcore.AntiHack.Events.AntiHackPlayerDetectedEvent;
import me.kingingo.kcore.NPC.Event.PlayerInteractNPCEvent;
import me.kingingo.kcore.PacketWrapper.WrapperPlayClientUseEntity;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilPlayer;
import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.DataWatcher;
import net.minecraft.server.v1_8_R2.PacketPlayOutBed;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R2.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R2.PacketPlayOutNamedEntitySpawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class Forcefield extends IHack{

	private HashMap<Player,NPC> list = new HashMap<>();
	private double max_hits;
	
	public Forcefield(AntiHack antiHack,double max_hits) {
		super(antiHack, "Forcefield");
		this.max_hits=max_hits;
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter( getAntiHack().getInstance() , ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY){
		    public void onPacketReceiving(PacketEvent event){
		        if(event.getPacketType() == PacketType.Play.Client.USE_ENTITY){
		            try {
		                if(list.containsKey(event.getPlayer())){
		                	NPC npc = list.get(event.getPlayer());
		                	npc.setHits(npc.getHits()+1);
		                	event.setCancelled(true);
		                }
		            } catch (Exception e){
		            	Log("Error: "+e.getLocalizedMessage());
		            }
		        }
		    }
		});
		
	}
	
	public void check(Player player){
		if(!list.containsKey(player)){
			NPC npc = new NPC(player, player.getName()+"-FORCEFIELD", player.getLocation().add(0, 2.3, 0) );
			npc.spawn();
			npc.setTimer( System.currentTimeMillis()+(TimeSpan.HALF_SECOND*3) );
			list.put(player, npc);
		}
	}
	
	NPC npc;
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FASTEST){
			for(int i = 0; i<list.values().size(); i++){
				npc=(NPC)list.values().toArray()[i];
				if(npc.getTimer() <= System.currentTimeMillis()){
					if(npc.getHits() >= max_hits){
						Bukkit.getPluginManager().callEvent(new AntiHackPlayerDetectedEvent(npc.getPlayer(),getHackType(),getLevel(npc.getHits())));
					}
					list.remove(npc);
				}
			}
		}
	}
	
	public Level getLevel(int hits){
		long h = Math.round(((hits/max_hits)*Level.values().length));
		for(Level lvl : Level.values())if(lvl.getLevel()==h)return lvl;
		return null;
	}
	
	public class NPC {
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
		   private Player player;
		   @Getter
		   @Setter
		   private int hits=0;
		   @Getter
		   @Setter
		   private long timer;
		   
		   @Getter
		   private PacketPlayOutNamedEntitySpawn spawn_packet;
		   
		   public NPC(Player player,String name, UUID uuid, int entityID, Location location, Material inHand) {
		      this.location = location;
		      this.player=player;
		      this.name = name;
		      this.uuid = uuid;
		      this.entityID = entityID;
		      this.inHand = inHand;
		      this.watcher = new DataWatcher(null);
		      watcher.a(6, (float) 20);
		   }
		   
		   public NPC(Player player, String name, Location location) {
		      this(player,name, UUID.randomUUID(), new Random().nextInt(10000), location, Material.AIR);
		   }

		   public void spawn() {
		      try{
		    	 spawn_packet = new PacketPlayOutNamedEntitySpawn();
		         
		         this.setValue(spawn_packet, "a", this.entityID);
		         this.setValue(spawn_packet, "b", this.uuid);
		         this.setValue(spawn_packet, "c", this.toFixedPoint(this.location.getX()));
		         this.setValue(spawn_packet, "d", this.toFixedPoint(this.location.getY()));
		         this.setValue(spawn_packet, "e", this.toFixedPoint(this.location.getZ()));
		         this.setValue(spawn_packet, "f", this.toPackedByte(this.location.getYaw()));
		         this.setValue(spawn_packet, "g", this.toPackedByte(this.location.getPitch()));
		         this.setValue(spawn_packet, "h", this.inHand == null ? 0 : this.inHand.getId());
		         this.setValue(spawn_packet, "i", this.watcher);
		         
		         UtilPlayer.sendPacket(player, spawn_packet);
		      }catch(Exception e) {
		         e.printStackTrace();
		      }
		   }
		   

		   public void despawn() {
		      PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{this.entityID});
		      UtilPlayer.sendPacket(player, packet);
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

		       UtilPlayer.sendPacket(player, packet);
		   }
		   
		   public void sleep(){
				try {
					PacketPlayOutBed packet = new PacketPlayOutBed();
					
					setValue(packet,"a", this.entityID);
					setValue(packet,"b", new BlockPosition(this.toFixedPoint(this.location.getX()),this.toFixedPoint(this.location.getY()),this.toFixedPoint(this.location.getZ())));
					
					UtilPlayer.sendPacket(player, packet);
				} catch (Exception e) {
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
		         
		         UtilPlayer.sendPacket(player, packet);
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
		         
		         UtilPlayer.sendPacket(player, packet);
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
		         
		         UtilPlayer.sendPacket(player, packet);
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
		         
		         UtilPlayer.sendPacket(player, packet);
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
		       UtilPlayer.sendPacket(player, packet);
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
		         
		         UtilPlayer.sendPacket(player, packet);
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
		         
		         UtilPlayer.sendPacket(player, packet);
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
		   
		   private int toFixedPoint(double d) {
		      return (int) (d * 32.0);
		   }
		   
		   private byte toPackedByte(float f) {
		      return (byte) ((int) (f * 256.0F / 360.0F));
		   }
		   
		}

}
