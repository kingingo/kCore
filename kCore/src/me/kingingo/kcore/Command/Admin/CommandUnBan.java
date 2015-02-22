package me.kingingo.kcore.Command.Admin;

import java.util.UUID;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandUnBan implements CommandExecutor{
	
	private MySQL mysql;
	
	public CommandUnBan(MySQL mysql){
		this.mysql=mysql;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "unkban", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(args.length==2){
			try{
				int i = Integer.valueOf(args[1]);
				UUID uuid = UtilPlayer.getUUID(args[0], mysql);
				System.out.println("UUID : "+uuid);
				mysql.Update("UPDATE BG_BAN SET aktiv='false' WHERE name_uuid='" + uuid + "' AND level='"+i+"'");
				mysql.Update("UPDATE BG_ZEITBAN SET aktiv='false' WHERE name_uuid='" + uuid + "'");
				mysql.Update("UPDATE BG_BAN SET aktiv='false' WHERE name='" + args[0].toLowerCase() + "' AND level='"+i+"'");
				mysql.Update("UPDATE BG_ZEITBAN SET aktiv='false' WHERE name='" + args[0].toLowerCase() + "'");
				System.err.println("[UnkBan] "+args[0]+" wurde entbannt");
			}catch(NumberFormatException e){
				System.out.println("Das ist keine Zahl "+args[1]);
			}
		}
		return false;
	}
	
}
