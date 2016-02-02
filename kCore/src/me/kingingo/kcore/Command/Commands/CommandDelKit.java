package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Command.Commands.Events.DeleteKitEvent;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDelKit implements CommandExecutor{

	private Player player;
	private kConfig config;
	
	public CommandDelKit(kConfig config){
		this.config=config;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "delkit", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		
		if(player.hasPermission(kPermission.KIT_SET.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/delkit [Name]");
			}else{
				DeleteKitEvent delete = new DeleteKitEvent(player, args[0].toLowerCase());
				Bukkit.getPluginManager().callEvent(delete);
				
				if(delete.isExist()){
					config.set("kits."+args[0].toLowerCase(), null);
					config.save();
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "KIT_DEL",args[0]));
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "KIT_EXIST"));
				}
			}
		}
		return false;
	}

}
