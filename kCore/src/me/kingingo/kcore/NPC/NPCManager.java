package me.kingingo.kcore.NPC;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.NPC.Event.PlayerInteractNPCEvent;
import me.kingingo.kcore.PacketWrapper.WrapperPlayClientUseEntity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class NPCManager implements Listener {

	@Getter
	HashMap<Integer,NPC> NPCList = new HashMap<>();
	@Getter
	JavaPlugin instance;
//	@Getter
//	NickManager nManager;
	
	public NPCManager(JavaPlugin instance){
		this.instance=instance;
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY){
		    public void onPacketReceiving(PacketEvent event){
		        if(event.getPacketType() == PacketType.Play.Client.USE_ENTITY){
		            Player player = event.getPlayer();
		            try {
		                WrapperPlayClientUseEntity packet = new WrapperPlayClientUseEntity(event.getPacket());
		                if(NPCList.containsKey(packet.getTargetID())){
		                	PlayerInteractNPCEvent ev = new PlayerInteractNPCEvent(player,getNPCList().get( packet.getTargetID() ));
		                	Bukkit.getPluginManager().callEvent(ev);
		                	event.setCancelled(true);
		                }
		            } catch (Exception e){
		            	System.err.println("[NPCManager] Error: "+e.getMessage());
		            }
		        }
		    }
		});
		
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void Respawn(PlayerRespawnEvent ev){
		for(NPC npc : NPCList.values()){
			((CraftPlayer) ev.getPlayer()).getHandle().playerConnection.sendPacket(npc.getSpawn_packet());
		}
	}
	
	public NPC createNPC(String name,Location loc){
		NPC npc = new NPC(this,name,loc);
		return npc;
	}
	
}
