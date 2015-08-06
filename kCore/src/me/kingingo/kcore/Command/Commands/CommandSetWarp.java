package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilString;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetWarp implements CommandExecutor{
	
	private Player player;
	private kConfig config;
	
	public CommandSetWarp(kConfig config){
		this.config=config;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "setwarp", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(args.length==0){
			player.sendMessage(Language.getText(player, "PREFIX")+"/setwarp [Name]");
		}else{
			if(player.hasPermission(kPermission.WARP_SET.getPermissionToString())){
				
				if(!UtilString.isNormalCharakter(args[0])){
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_CHARAKTER"));
					return false;
				}
				
				config.setLocation("warps."+args[0], player.getLocation());
				config.save();
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "WARP_SET",args[0]));
			}
		}
		return false;
	}

}
