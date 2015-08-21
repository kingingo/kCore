package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandBroadcast implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "broadcast", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player p = (Player)sender;
			if(!p.hasPermission(kPermission.BROADCAST.getPermissionToString()))return false;
			if(args.length==0){
				p.sendMessage(Language.getText(p, "PREFIX")+"§7/broadcast [Message]");
			}else{
				broadcast(args);
			}
		}else if(sender instanceof ConsoleCommandSender){
			if(args.length==0){
				System.out.println("[EpicPvP] /broadcast [Nachricht]");
			}else{
				broadcast(args);
			}
		}
		return false;
	}
	
	public void broadcast(String[] args){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			sb.append(args[i]);
			sb.append(" ");
		}
		sb.setLength(sb.length() - 1);
		String msg = sb.toString();
		
		UtilServer.broadcast(msg.replaceAll("&", "§"));
	}
	
}
