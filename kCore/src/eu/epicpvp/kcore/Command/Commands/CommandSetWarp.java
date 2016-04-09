package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Util.UtilString;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandSetWarp implements CommandExecutor{
	
	private Player player;
	private kConfig config;
	
	public CommandSetWarp(kConfig config){
		this.config=config;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "setwarp", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(args.length==0){
			player.sendMessage(TranslationManager.getText(player, "PREFIX")+"/setwarp [Name]");
		}else{
			if(player.hasPermission(PermissionType.WARP_SET.getPermissionToString())){
				
				if(!UtilString.isNormalCharakter(args[0].toLowerCase())){
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "NO_CHARAKTER"));
					return false;
				}
				
				config.setLocation("warps."+args[0].toLowerCase(), player.getLocation());
				config.save();
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "WARP_SET",args[0].toLowerCase()));
			}
		}
		return false;
	}

}
