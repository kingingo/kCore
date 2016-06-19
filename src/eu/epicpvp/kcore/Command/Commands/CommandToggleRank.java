package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilServer;

public class CommandToggleRank implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "togglerank", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player=(Player)sender;
		if(player.hasPermission(PermissionType.COMMAND_TOGGLERANK.getPermissionToString())){
			
			if(UtilServer.getPermissionManager().getPermissionPlayer(player).isPrefix()){
				player.sendMessage(TranslationHandler.getPrefixAndText(player, "TOGGLE_RANK_OFF"));
				UtilServer.getPermissionManager().getPermissionPlayer(player).setPrefix(false);
			}else{
				player.sendMessage(TranslationHandler.getPrefixAndText(player, "TOGGLE_RANK_ON"));
				UtilServer.getPermissionManager().getPermissionPlayer(player).setPrefix(true);
			}
		}
		return false;
	}
}
