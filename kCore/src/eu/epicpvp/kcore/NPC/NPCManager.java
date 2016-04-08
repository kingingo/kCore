package eu.epicpvp.kcore.NPC;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.NPC.Event.PlayerInteractNPCEvent;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayInUseEntity;
import eu.epicpvp.kcore.PacketAPI.packetlistener.event.PacketListenerReceiveEvent;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

public class NPCManager extends kListener {

	@Getter
	private HashMap<Integer,NPC> NPCList = new HashMap<>();
	@Getter
	private JavaPlugin instance;
	private HashMap<NPC,Player> owner;
	
	public NPCManager(JavaPlugin instance){
		super(instance,"NPCManager");
		this.instance=instance;
		UtilServer.createPacketListener(instance);
	}
	
	@EventHandler
	public void Receive(PacketListenerReceiveEvent ev){
		if(ev.getPacket() instanceof PacketPlayInUseEntity&&ev.getPlayer()!=null){
			 try {
	                kPacketPlayInUseEntity use = new kPacketPlayInUseEntity(ev.getPacket());
	                if(NPCList.containsKey(use.getEntityID())){
	                	ev.setCancelled(true);
	                	
	                	Bukkit.getScheduler().runTask(getInstance(), new Runnable(){

							@Override
							public void run() {
			                	PlayerInteractNPCEvent eve = new PlayerInteractNPCEvent(ev.getPlayer(),getNPCList().get( use.getEntityID() ));
			                	Bukkit.getPluginManager().callEvent(eve);
							}
	                		
	                	});
	                }
	            } catch (Exception e){
	            	Log("Error: ");
	            	e.printStackTrace();
	            }
		}
	}
	
	public void addNPC(NPC npc){
		if(this.NPCList.containsKey(npc.getEntityID()))return;
		this.NPCList.put(npc.getEntityID(), npc);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		for(NPC npc : NPCList.values()){
			npc.show(ev.getPlayer());
		}
	}
	
	@EventHandler
	public void Respawn(PlayerRespawnEvent ev){
		for(NPC npc : NPCList.values()){
			UtilPlayer.sendPacket(ev.getPlayer(), npc.getSpawnPacket());
		}
	}
	
	NPC npc;
	Location npcLoc;
	Player player;
	Location playerLoc;
	int xDiff;
	int yDiff;
	int zDiff;
	int xIndex;
    int zIndex;
    Block targetBlock;
	@EventHandler
	public void Move(UpdateEvent ev){
		if(owner!=null&&!owner.isEmpty()&&ev.getType()==UpdateType.FASTER){
			for(int i = 0 ; i<owner.size(); i++){
				player=owner.get(i);
				
				if(player!=null&&player.isOnline()){
					npc=((NPC)owner.keySet().toArray()[i]);
					
					npcLoc=npc.getLocation();
			    	playerLoc=player.getLocation();
			    	
			    	xDiff = Math.abs(npcLoc.getBlockX() - playerLoc.getBlockX());
			    	yDiff = Math.abs(npcLoc.getBlockY() - playerLoc.getBlockY());
			    	zDiff = Math.abs(npcLoc.getBlockZ() - playerLoc.getBlockZ());
			    	
			    	if(xDiff+zDiff+yDiff > 4){
			    		
			    		
			    		xIndex=-1;
			    		zIndex=-1;
			    		targetBlock = playerLoc.getBlock().getRelative(xIndex, -1, zIndex);
			 	        while ((targetBlock.isEmpty()) || (targetBlock.isLiquid())){
			 	          if (xIndex < 2) {
			 	            xIndex++;
			 	          } else if (zIndex < 2)
			 	          {
			 	            xIndex = -1;
			 	            zIndex++;
			 	          }
			 	          else {
			 	            return;
			 	          }
			 	          targetBlock = playerLoc.getBlock().getRelative(xIndex, -1, zIndex);
			 	        }
			 	        
			 	       if ( npcLoc.distance(playerLoc)<8 ){
			 	    	  npc.walk(targetBlock.getX(), targetBlock.getY() + 1, targetBlock.getZ(),npcLoc.getYaw(),npcLoc.getPitch());
				       }else{
				    	   npc.teleport(playerLoc);
				       }
			    	}
					
				}else{
					((NPC)owner.keySet().toArray()[i]).despawn();
					owner.remove(i);
				}
			}
		}
	}
	
	public NPC createNPCWithOwner(Player player,String name){
		if(owner==null)owner= new HashMap<>();
		NPC npc = new NPC(this,name,player.getLocation());
		owner.put(npc, player);
		return npc;
	}
	
	public NPC createNPC(String name,Location loc){
		NPC npc = new NPC(this,name,loc);
		return npc;
	}
	
}
