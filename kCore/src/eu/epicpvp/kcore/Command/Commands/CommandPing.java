package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandPing implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "ping", alias={"kping"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(args.length==0){
			((Player)sender).sendMessage(TranslationHandler.getText(((Player)sender), "PREFIX")+"Player-Ping: §e"+UtilPlayer.getPlayerPing(((Player)sender))+"§7 Server-TPS: §e"+(int)UtilServer.getLagMeter().getTicksPerSecond());
		}else{
			if(((Player)sender).isOp()){
				if(UtilPlayer.isOnline(args[0])){
					((Player)sender).sendMessage(TranslationHandler.getText(((Player)sender), "PREFIX")+args[0]+"-Ping: §e"+UtilPlayer.getPlayerPing(Bukkit.getPlayer(args[0]))+"§7 Server-TPS: §e"+(int)UtilServer.getLagMeter().getTicksPerSecond());
				}
			}
		}
		return false;
	}

}
