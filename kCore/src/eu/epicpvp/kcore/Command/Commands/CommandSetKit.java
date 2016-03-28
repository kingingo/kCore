package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Command.Commands.Events.AddKitEvent;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilString;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandSetKit implements CommandExecutor{

	private Player player;
	private kConfig config;
	
	public CommandSetKit(kConfig config){
		this.config=config;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "setkit", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		
		if(player.hasPermission(PermissionType.KIT_SET.getPermissionToString())){
			if(args.length<=1){
				player.sendMessage(Language.getText(player, "PREFIX")+"/setkit [Name] [Delay STD]");
			}else{
				if(args[0].equalsIgnoreCase("reload")){
					
				}else{
					if(!UtilString.isNormalCharakter(args[0])){
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_CHARAKTER"));
						return false;
					}
					config.setInventory("kits."+args[0].toLowerCase()+".Inventory", player.getInventory());
					
					if(UtilNumber.toInt(args[1])!=0){
						config.set("kits."+args[0].toLowerCase()+".Delay", UtilNumber.toInt(args[1])*TimeSpan.HOUR );
						AddKitEvent ev = new AddKitEvent(player, args[0].toLowerCase(), UtilNumber.toInt(args[1])*TimeSpan.HOUR);
						Bukkit.getPluginManager().callEvent(ev);
					}else{
						AddKitEvent ev = new AddKitEvent(player, args[0].toLowerCase(),0);
						Bukkit.getPluginManager().callEvent(ev);
					}
					config.save();
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "KIT_SET",args[0]));
				}
			}
		}
		return false;
	}

}
