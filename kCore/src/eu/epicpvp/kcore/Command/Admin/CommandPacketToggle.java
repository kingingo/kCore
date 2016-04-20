package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.PacketAPI.packetlistener.event.PacketListenerReceiveEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying.PacketPlayInPosition;

public class CommandPacketToggle extends kListener implements CommandExecutor{
	
	private JavaPlugin instance;
	private Player player;
	
	public CommandPacketToggle(JavaPlugin instance){
		super(instance,"packetby");
		this.instance=instance;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "packetby", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(args.length != 0){
			if(UtilPlayer.isOnline(args[0])){
				player=Bukkit.getPlayer(args[0]);
			}else{
				player=null;
			}
		}
		return false;
	}
	
	@EventHandler
	public void rec(PacketListenerReceiveEvent ev){
		if(ev.getPlayer()!=null && player!=null){
			if(ev.getPlayer().getUniqueId()==player.getUniqueId()){
				System.err.println(""+ev.getPacketName());
				
				if(ev.getPacket() instanceof PacketPlayInPosition){
					System.err.println(ev.getPacketName()+" -> "+((PacketPlayInPosition)ev.getPacket()));
				}else if(ev.getPacket() instanceof PacketPlayInFlying){
					System.err.println(ev.getPacketName()+" -> "+((PacketPlayInFlying)ev.getPacket()));
				}
			}
		}
	}
	
}

