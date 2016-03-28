package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandDelWarp implements CommandExecutor{
	
	private Player player;
	private kConfig config;
	
	public CommandDelWarp(kConfig config){
		this.config=config;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "delwarp", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(args.length==0){
			player.sendMessage(Language.getText(player, "PREFIX")+"/delwarp [Name]");
		}else{
			if(player.hasPermission(PermissionType.WARP_SET.getPermissionToString())){
				if(config.isSet("warps."+args[0])){
					config.set("warps."+args[0], null);
					config.save();
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "WARP_DEL",args[0]));
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "WARP_EXIST"));
				}
			}
		}
		return false;
	}

}
