package me.kingingo.kcore.Listener.Command;

import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.NPC.Event.PlayerInteractNPCEvent;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayInTabComplete;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayInUseEntity;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayOutTabComplete;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerReceiveEvent;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutTabComplete;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerCMD extends kListener{

	public ListenerCMD(JavaPlugin instance) {
		super(instance, "ListenerCMD");
		UtilServer.createPacketListener(instance);
	}
	
	String cmd;
	String[] parts;
	@EventHandler
	public void CMD(PlayerCommandPreprocessEvent ev){
		cmd = "";
	    if (ev.getMessage().contains(" ")){
	      parts = ev.getMessage().split(" ");
	      cmd = parts[0];
	    }else{
	      cmd = ev.getMessage();
	    }
	    
	    if(cmd.equalsIgnoreCase("/plugins")){
	    	ev.setCancelled(true);
	    }else if(cmd.equalsIgnoreCase("/pl")){
	    	ev.setCancelled(true);
	    }else if(cmd.equalsIgnoreCase("/?")){
	    	ev.setCancelled(true);
	    }else if(cmd.equalsIgnoreCase("/help")){
	    	ev.setCancelled(true);
	    }else if(cmd.contains("/bukkit:")){
	    	ev.setCancelled(true);
	    }else if(cmd.contains("/minecraft:")){
	    	ev.setCancelled(true);
	    }else if(cmd.contains("/essentials:")){
	    	ev.setCancelled(true);
	    }
	}
	
	@EventHandler
	public void Receive(PacketListenerReceiveEvent ev){
		if(ev.getPacket() instanceof PacketPlayInTabComplete&&ev.getPlayer()!=null){
			 try {
	                kPacketPlayInTabComplete use = new kPacketPlayInTabComplete(ev.getPacket());
	                
	                if(use.getChatMessage().contains("/?")){
	        			ev.setCancelled(true);
	        		}else if(use.getChatMessage().contains("/bukkit:")){
	        			ev.setCancelled(true);
	        		}else if(use.getChatMessage().contains("/minecraft:")){
	        			ev.setCancelled(true);
	        		}else if(use.getChatMessage().contains("/essentials:")){
	        			ev.setCancelled(true);
	        		}else if(use.getChatMessage().contains("/ver")){
	        			ev.setCancelled(true);
	        		}else if(use.getChatMessage().equalsIgnoreCase("/")){
	        			ev.setCancelled(true);
	        		}
	                
	            } catch (Exception e){
	            	Log("Error: ");
	            	e.printStackTrace();
	            }
		}
	}

}
