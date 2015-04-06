package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.Util.UtilPlayer;
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
			player.sendMessage(Text.PREFIX.getText()+"/setwarp [Name]");
		}else{
			if(player.hasPermission(kPermission.WARP_SET.getPermissionToString())){
				config.setLocation("warps."+args[0], player.getLocation());
				config.save();
				player.sendMessage(Text.PREFIX.getText()+Text.WARP_SET.getText(args[0]));
			}
		}
		return false;
	}

}
