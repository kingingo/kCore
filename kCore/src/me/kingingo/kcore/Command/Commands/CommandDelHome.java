package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDelHome implements CommandExecutor{
	
	private UserDataConfig userData;
	private Player player;
	private kConfig config;
	
	public CommandDelHome(UserDataConfig userData){
		this.userData=userData;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "delhome", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		config = userData.getConfig(player);
		
		if(args.length==0){
			player.sendMessage(Text.PREFIX.getText()+"/delhome [Name]");
		}else if(args.length>=1){
			if(player.hasPermission(kPermission.HOME_DEL.getPermissionToString())){
				if(config.isSet("homes."+args[0])){
					config.set("homes."+args[0], null);
					config.save();
					player.sendMessage(Text.PREFIX.getText()+Text.HOME_DEL.getText(args[0]));
				}else{
					player.sendMessage(Text.PREFIX.getText()+Text.HOME_EXIST.getText());
				}
			}
		}
		return false;
	}

}
