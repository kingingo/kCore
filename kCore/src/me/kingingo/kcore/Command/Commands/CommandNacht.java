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

public class CommandNacht implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "nacht",alias={"night"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(kPermission.NACHT.getPermissionToString())){
			if(args.length==0){
				player.getWorld().setTime(12575);
				player.sendMessage(Text.PREFIX.getText()+Text.NIGHT.getText());
			}
		}
		return false;
	}

}
