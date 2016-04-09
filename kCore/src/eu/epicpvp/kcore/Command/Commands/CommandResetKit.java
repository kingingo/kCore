package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Command.Commands.Events.ResetKitEvent;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationManager;

public class CommandResetKit implements CommandExecutor{

	private Player player;
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "resetkit", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		
		if(player.hasPermission(PermissionType.KIT_RESET.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+"/resetkit [Name]");
			}else{
				String kit_player=args[0].toLowerCase();
				ResetKitEvent reset = new ResetKitEvent(player, kit_player);
				Bukkit.getPluginManager().callEvent(reset);
				
				if(reset.isCancelled()){
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "KIT_EXIST"));
				}
			}
		}
		return false;
	}

}
