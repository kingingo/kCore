package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationManager;

public class CommandSonne implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "sonne",alias={"sun"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(PermissionType.SUN.getPermissionToString())){
			if(args.length==0){
				player.getWorld().setStorm(false);
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "SUN"));
			}
		}
		return false;
	}

}
