package me.kingingo.kcore.NPC;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.NPC.Event.PlayerInteractNPCEvent;
import me.kingingo.kcore.PacketAPI.packetlistener.kPacketListener;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerReceiveEvent;
import me.kingingo.kcore.PacketAPI.packetlistener.handler.PacketHandler;
import me.kingingo.kcore.PacketAPI.packetlistener.handler.ReceivedPacket;
import me.kingingo.kcore.PacketAPI.packetlistener.handler.SentPacket;
import me.kingingo.kcore.PacketAPI.v1_8_R2.kPacketPlayInUseEntity;
import me.kingingo.kcore.Util.UtilPlayer;
import net.minecraft.server.v1_8_R2.PacketPlayInUseEntity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NPCManager implements Listener {

	@Getter
	private HashMap<Integer,NPC> NPCList = new HashMap<>();
	@Getter
	private JavaPlugin instance;
	private kPacketListener listener;
	
	public NPCManager(JavaPlugin instance){
		this.instance=instance;
		this.listener=new kPacketListener(instance);
//		
//		this.listener.addPacketHandler(new PacketHandler() {
//			
//			@Override
//			public void onSend(SentPacket packet) {
//				
//			}
//			
//			@Override
//			public void onReceive(ReceivedPacket packet) {
//				if(packet.getPacket() instanceof PacketPlayInUseEntity){
//					 try {
//			                kPacketPlayInUseEntity use = new kPacketPlayInUseEntity(packet.getPacket());
//			                System.out.println("E: "+use.getEntityID()+ " "+packet.getPlayername());
//			                if(NPCList.containsKey(use.getEntityID())){
//			                	packet.setCancelled(true);
//				                System.out.println("E1: "+use.getEntityID()+ " "+packet.getPlayername());
//			                	PlayerInteractNPCEvent ev = new PlayerInteractNPCEvent(packet.getPlayer(),getNPCList().get( use.getEntityID() ));
//			                	Bukkit.getPluginManager().callEvent(ev);
//			                }
//			            } catch (Exception e){
//			            	System.err.println("[NPCManager] Error: "+e.getMessage());
//			            }
//				}
//			}
//			
//		});
		
//		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY){
//		    public void onPacketReceiving(PacketEvent event){
//		        if(event.getPacketType() == PacketType.Play.Client.USE_ENTITY){
//		            Player player = event.getPlayer();
//		            try {
//		                WrapperPlayClientUseEntity packet = new WrapperPlayClientUseEntity(event.getPacket());
//		                if(NPCList.containsKey(packet.getTargetID())){
//		                	PlayerInteractNPCEvent ev = new PlayerInteractNPCEvent(player,getNPCList().get( packet.getTargetID() ));
//		                	Bukkit.getPluginManager().callEvent(ev);
//		                	event.setCancelled(true);
//		                }
//		            } catch (Exception e){
//		            	System.err.println("[NPCManager] Error: "+e.getMessage());
//		            }
//		        }
//		    }
//		});
		
		
		
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void Receive(PacketListenerReceiveEvent ev){
		if(ev.getPacket() instanceof PacketPlayInUseEntity&&ev.getPlayer()!=null){
			 try {
	                kPacketPlayInUseEntity use = new kPacketPlayInUseEntity(ev.getPacket());
	                if(NPCList.containsKey(use.getEntityID())){
	                	ev.setCancelled(true);
	                	PlayerInteractNPCEvent eve = new PlayerInteractNPCEvent(ev.getPlayer(),getNPCList().get( use.getEntityID() ));
	                	Bukkit.getPluginManager().callEvent(eve);
	                }
	            } catch (Exception e){
	            	System.err.println("[NPCManager] Error: ");
	            	e.printStackTrace();
	            }
		}
	}
	
	@EventHandler
	public void Respawn(PlayerRespawnEvent ev){
		for(NPC npc : NPCList.values()){
			UtilPlayer.sendPacket(ev.getPlayer(), npc.getSpawn_packet());
		}
	}
	
	public NPC createNPC(String name,Location loc){
		NPC npc = new NPC(this,name,loc);
		return npc;
	}
	
}
