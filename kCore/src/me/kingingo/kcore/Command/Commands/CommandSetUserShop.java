package me.kingingo.kcore.Command.Commands;

import me.kingingo.kSkyblock.SkyBlockManager;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.Util.UtilString;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetUserShop implements CommandExecutor{
	
	private Player player;
	private kConfig config;
	
	public CommandSetUserShop(kConfig config){
		this.config=config;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "setusershop", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(args.length==0){
			if(UtilServer.getUserData().getConfig(player).contains("UserStores")){
				if(player.getWorld().getUID() != Bukkit.getWorld("world").getUID()){
					config.setLocation("shops."+player.getName().toLowerCase(), player.getLocation());
					config.save();
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "SHOP_SET"));
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "SHOP_NOT_IN_WORLD"));
				}
			}else{
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "SHOP_NOT"));
			}
		}
		return false;
	}

}
