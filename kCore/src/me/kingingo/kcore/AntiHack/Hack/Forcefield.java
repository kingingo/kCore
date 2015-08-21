package me.kingingo.kcore.AntiHack.Hack;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.AntiHack.AntiHack;
import me.kingingo.kcore.AntiHack.IHack;
import me.kingingo.kcore.AntiHack.Level;
import me.kingingo.kcore.AntiHack.Events.AntiHackPlayerDetectedEvent;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutBed;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityDestroy;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityEquipment;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityMetadata;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutEntityTeleport;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutNamedEntitySpawn;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutRelEntityMoveLook;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilPlayer;
import net.minecraft.server.v1_8_R3.DataWatcher;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Forcefield extends IHack{

	private HashMap<Player,NPC> list = new HashMap<>();
	private double max_hits;
	
	public Forcefield(AntiHack antiHack,double max_hits) {
		super(antiHack, "Forcefield");
		this.max_hits=max_hits;
		
//		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter( getAntiHack().getInstance() , ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY){
//		    public void onPacketReceiving(PacketEvent event){
//		        if(event.getPacketType() == PacketType.Play.Client.USE_ENTITY){
//		            try {
//		                if(list.containsKey(event.getPlayer())){
//		                	NPC npc = list.get(event.getPlayer());
//		                	npc.setHits(npc.getHits()+1);
//		                	event.setCancelled(true);
//		                }
//		            } catch (Exception e){
//		            	Log("Error: "+e.getLocalizedMessage());
//		            }
//		        }
//		    }
//		});
		
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
		   private kPacketPlayOutNamedEntitySpawn spawn_packet;
		   
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
			    	 spawn_packet = new kPacketPlayOutNamedEntitySpawn();
			         
			         spawn_packet.setEntityID(entityID);
			         spawn_packet.setUUID(uuid);
			         spawn_packet.setLocation(location);
			         spawn_packet.setItemInHand(inHand);
			         spawn_packet.setDataWatcher(watcher);

			         UtilPlayer.sendPacket(player, spawn_packet);
			      }catch(Exception e) {
			         e.printStackTrace();
			      }
			   }
		   

		   public void despawn() {
		      kPacketPlayOutEntityDestroy packet = new kPacketPlayOutEntityDestroy(new int[]{this.entityID});
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
		       kPacketPlayOutRelEntityMoveLook packet = new kPacketPlayOutRelEntityMoveLook(this.entityID,x,y,z,getCompressedAngle(yaw),getCompressedAngle(pitch),true);

		       UtilPlayer.sendPacket(player, packet);
		   }
		   
		   public void sleep(){
				try {
					kPacketPlayOutBed packet = new kPacketPlayOutBed();
					
					packet.setEntityID(entityID);
					packet.setPosition(location);

			         UtilPlayer.sendPacket(player, packet);
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
		         
		         UtilPlayer.sendPacket(player, packet);
		      }catch(Exception e) {
		         e.printStackTrace();
		      }
		   }
		   

		   public void setItemInHand(Material material) {
		      try{
		         kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(this.entityID,0,material);
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
		         kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(this.entityID,4,material);
		         
		         this.helmet=material;
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
		         kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(entityID,3,material);
		         
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
		       kPacketPlayOutEntityMetadata packet = new kPacketPlayOutEntityMetadata(this.entityID, this.watcher);
		         UtilPlayer.sendPacket(player, packet);
		   }
		   
		   public Material getChestplate() {
		      return this.chestplate;
		   }
		   

		   public void setLeggings(Material material) {
		      try{
		         kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(entityID,2,material);
		         
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
		         kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(entityID,1,material);
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
		   
		}

}
