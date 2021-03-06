package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Command.Commands.Events.PlayerDelHomeEvent;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandDelHome implements CommandExecutor{
	
	private UserDataConfig userData;

	public CommandDelHome(UserDataConfig userData){
		this.userData=userData;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "delhome", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		kConfig config = userData.getConfig(player);
		
		if(args.length==0){
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/delhome [Name]");
		}else if(args.length>=1){
			if(player.hasPermission(PermissionType.HOME_DEL.getPermissionToString())){
				if(config.isSet("homes."+args[0])){
					Bukkit.getPluginManager().callEvent(new PlayerDelHomeEvent(player, config.getLocation("homes."+args[0]),args[0], config));
					
					config.set("homes."+args[0], null);
					config.save();
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HOME_DEL",args[0]));
				}else{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HOME_EXIST"));
				}
			}
		}
		return false;
	}

}
