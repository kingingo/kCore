package me.kingingo.kcore.Listener.Command;

import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.PacketAPI.Packets.kPacketPlayInTabComplete;
import me.kingingo.kcore.PacketAPI.packetlistener.event.PacketListenerReceiveEvent;
import me.kingingo.kcore.Util.UtilServer;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerCMD extends kListener{

	public ListenerCMD(JavaPlugin instance) {
		super(instance, "ListenerCMD");
		UtilServer.createPacketListener(instance);
		org.spigotmc.SpigotConfig.unknownCommandMessage=Language.getText("PREFIX")+Language.getText("UNKNOWN_CMD");
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
	    }else if(cmd.contains("/about")){
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
	        		}else if(use.getChatMessage().contains("/about")){
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
