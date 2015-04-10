package me.kingingo.kcore.Command.Admin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandBroadcast implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "broadcast", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(args.length==0){
			System.out.println("[EpicPvP] /broadcast [Nachricht]");
		}else{
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				sb.append(args[i]);
				sb.append(" ");
			}
			sb.setLength(sb.length() - 1);
			String msg = sb.toString();
			
			UtilServer.broadcast(msg.replaceAll("&", "§"));
		}
		return false;
	}
	
}
