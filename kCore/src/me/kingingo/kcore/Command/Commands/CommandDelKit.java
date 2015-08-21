package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDelKit implements CommandExecutor{

	private CommandKit kit;
	private Player player;
	private kConfig config;
	
	public CommandDelKit(CommandKit kit,kConfig config){
		this.kit=kit;
		this.config=config;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "delkit", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		
		if(player.hasPermission(kPermission.KIT_SET.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/delkit [Name]");
			}else{
				if(kit.getKits().containsKey(args[0].toLowerCase())){
					kit.getKits().remove(args[0].toLowerCase());
					kit.getKits_delay().remove(args[0].toLowerCase());
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
