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

public class CommandDelWarp implements CommandExecutor{
	
	private Player player;
	private kConfig config;
	
	public CommandDelWarp(kConfig config){
		this.config=config;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "delwarp", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(args.length==0){
			player.sendMessage(Text.PREFIX.getText()+"/delwarp [Name]");
		}else{
			if(player.hasPermission(kPermission.WARP_SET.getPermissionToString())){
				if(config.isSet("warps."+args[0])){
					config.set("warps."+args[0], null);
					config.save();
					player.sendMessage(Text.PREFIX.getText()+Text.WARP_DEL.getText(args[0]));
				}else{
					player.sendMessage(Text.PREFIX.getText()+Text.WARP_EXIST.getText());
				}
			}
		}
		return false;
	}

}
