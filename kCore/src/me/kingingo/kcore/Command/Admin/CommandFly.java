package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFly implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "fly",alias={"kfly"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission(kPermission.kFLY.getPermissionToString())){
			if(player.getAllowFlight()){
				player.setAllowFlight(false);
				player.setFlying(false);
				player.sendMessage(Text.PREFIX.getText()+Text.kFLY_OFF.getText());
			}else{
				player.setAllowFlight(true);
				player.setFlying(true);
				player.sendMessage(Text.PREFIX.getText()+Text.kFLY_ON.getText());
			}
		}
		return false;
	}

}
