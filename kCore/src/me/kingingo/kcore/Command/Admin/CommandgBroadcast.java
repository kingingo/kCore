package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Packet.Packets.BROADCAST;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandgBroadcast implements CommandExecutor{

	private PacketManager packetManager;
	
	public CommandgBroadcast(PacketManager packetManager){
		this.packetManager=packetManager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "gbroadcast", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
			String m = "";
			for(int i = 0; i < args.length; i++) {
                m =m + args[i] + " ";
            }
			packetManager.SendPacket("BG", new BROADCAST(m.replaceAll("&", "§")));
		return false;
	}

}
