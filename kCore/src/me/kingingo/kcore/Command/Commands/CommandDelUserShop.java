package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDelUserShop implements CommandExecutor{
	
	private Player player;
	private kConfig config;
	
	public CommandDelUserShop(kConfig config){
		this.config=config;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "delusershop", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(args.length==0){
			if(config.isSet("shops."+player.getName().toLowerCase())){
				config.set("shops."+player.getName().toLowerCase(), null);
				config.save();
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "SHOP_DEL"));
			}else{
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "SHOP_EXIST"));
			}
		}
		return false;
	}

}
