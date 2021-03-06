package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandSetUserShop implements CommandExecutor{

	private kConfig config;
	
	public CommandSetUserShop(kConfig config){
		this.config=config;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "setusershop", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		
		if(args.length==0){
			if(UtilServer.getUserData().getConfig(player).contains("UserStores")){
				if(player.getWorld().getUID() != Bukkit.getWorld("world").getUID()){
					config.setLocation("shops."+player.getName().toLowerCase(), player.getLocation());
					config.save();
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "SHOP_SET"));
				}else{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "SHOP_NOT_IN_WORLD"));
				}
			}else{
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "SHOP_NOT"));
			}
		}
		return false;
	}

}
