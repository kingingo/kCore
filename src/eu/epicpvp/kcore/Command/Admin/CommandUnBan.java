package eu.epicpvp.kcore.Command.Admin;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import dev.wolveringer.client.LoadedPlayer;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandUnBan implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "unban",alias={"unkban"}, sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		
		if(args.length==0){
			System.out.println("/unban [Player]");
		}else{
			LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(args[0]);
			loadedplayer.banPlayer("undefined", "unban", "system", UUID.nameUUIDFromBytes("system".getBytes()), -2, System.currentTimeMillis(), "§cunbanned Buycraft");
			System.out.println(loadedplayer.getName()+ "("+loadedplayer.getPlayerId()+") wurde entbannt!!");
		}
		return false;
	}
	
}
