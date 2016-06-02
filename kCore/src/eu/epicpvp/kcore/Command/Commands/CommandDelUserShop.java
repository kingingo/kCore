package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandDelUserShop implements CommandExecutor{

	private kConfig config;
	
	public CommandDelUserShop(kConfig config){
		this.config=config;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "delusershop", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		
		if(args.length==0){
			if(config.isSet("shops."+ player.getName().toLowerCase())){
				config.set("shops."+ player.getName().toLowerCase(), null);
				config.save();
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "SHOP_DEL"));
			}else{
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "SHOP_EXIST"));
			}
		}
		return false;
	}

}
