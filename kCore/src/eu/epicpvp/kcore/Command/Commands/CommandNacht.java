package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationManager;

public class CommandNacht implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "nacht",alias={"night"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(PermissionType.NACHT.getPermissionToString())){
			if(args.length==0){
				player.getWorld().setTime(12575);
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "NIGHT"));
			}
		}
		return false;
	}

}
