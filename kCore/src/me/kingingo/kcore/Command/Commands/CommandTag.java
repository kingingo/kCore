package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTag implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "day",alias={"tag"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(kPermission.TAG.getPermissionToString())){
			if(args.length==0){
				player.getWorld().setTime(22925);
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "DAY"));
			}
		}
		return false;
	}

}
