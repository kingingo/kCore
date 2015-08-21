package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilNumber;
import me.kingingo.kcore.Util.UtilString;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetKit implements CommandExecutor{

	private CommandKit kit;
	private Player player;
	private kConfig config;
	
	public CommandSetKit(CommandKit kit,kConfig config){
		this.kit=kit;
		this.config=config;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "setkit", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		
		if(player.hasPermission(kPermission.KIT_SET.getPermissionToString())){
			if(args.length<=1){
				player.sendMessage(Language.getText(player, "PREFIX")+"/setkit [Name] [Delay STD]");
			}else{
				
				if(!UtilString.isNormalCharakter(args[0])){
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_CHARAKTER"));
					return false;
				}
				
				config.setInventory("kits."+args[0].toLowerCase()+".Inventory", player.getInventory());
				this.kit.getKits().put(args[0].toLowerCase(), player.getInventory().getContents().clone());
				if(UtilNumber.toInt(args[1])!=0){
					config.set("kits."+args[0].toLowerCase()+".Delay", UtilNumber.toInt(args[1])*TimeSpan.HOUR );
					this.kit.getKits_delay().put(args[0].toLowerCase(), UtilNumber.toInt(args[1])*TimeSpan.HOUR );
				}
				kit.getConfig().save();
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "KIT_SET",args[0]));
			}
		}
		return false;
	}

}
