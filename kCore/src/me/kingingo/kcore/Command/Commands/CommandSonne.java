package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSonne implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "sonne",alias={"sun"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(kPermission.SUN.getPermissionToString())){
			if(args.length==0){
				player.getWorld().setStorm(false);
				player.sendMessage(Text.PREFIX.getText()+Text.SUN.getText());
			}
		}
		return false;
	}

}