package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;

public class CommandTag implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "day",alias={"tag"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(PermissionType.TAG.getPermissionToString())){
			if(args.length==0){
				player.getWorld().setTime(22925);
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "DAY"));
			}
		}
		return false;
	}

}
