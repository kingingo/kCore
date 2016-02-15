package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Command.Commands.Events.ResetKitEvent;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandResetKit implements CommandExecutor{

	private Player player;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "resetkit", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		
		if(player.hasPermission(kPermission.KIT_RESET.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/resetkit [Name]");
			}else{
				String kit_player=args[0].toLowerCase();
				ResetKitEvent reset = new ResetKitEvent(player, kit_player);
				Bukkit.getPluginManager().callEvent(reset);
				
				if(reset.isCancelled()){
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "KIT_EXIST"));
				}
			}
		}
		return false;
	}

}
