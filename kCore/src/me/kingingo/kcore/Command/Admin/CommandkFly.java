package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Permission.PermissionManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandkFly implements CommandExecutor{
	
	private PermissionManager permManager;
	
	public CommandkFly(PermissionManager permManager){
		this.permManager=permManager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "kfly", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		if(permManager.hasPermission(player, Permission.kFLY)&&args.length==0){
			if(player.isFlying()){
				player.setFlying(false);
				player.setAllowFlight(false);
				player.sendMessage(Text.PREFIX.getText()+Text.kFLY_OFF.getText());
			}else{
				player.setFlying(true);
				player.setAllowFlight(true);
				player.sendMessage(Text.PREFIX.getText()+Text.kFLY_ON.getText());
			}
		}
		return false;
	}

}
