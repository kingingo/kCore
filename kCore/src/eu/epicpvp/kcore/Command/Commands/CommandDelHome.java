package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandDelHome implements CommandExecutor{
	
	private UserDataConfig userData;
	private Player player;
	private kConfig config;
	
	public CommandDelHome(UserDataConfig userData){
		this.userData=userData;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "delhome", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		config = userData.getConfig(player);
		
		if(args.length==0){
			player.sendMessage(Language.getText(player, "PREFIX")+"/delhome [Name]");
		}else if(args.length>=1){
			if(player.hasPermission(PermissionType.HOME_DEL.getPermissionToString())){
				if(config.isSet("homes."+args[0])){
					config.set("homes."+args[0], null);
					config.save();
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "HOME_DEL",args[0]));
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "HOME_EXIST"));
				}
			}
		}
		return false;
	}

}
