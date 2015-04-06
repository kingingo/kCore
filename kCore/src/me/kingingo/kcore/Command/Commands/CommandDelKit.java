package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDelKit implements CommandExecutor{

	private CommandKit kit;
	private Player player;
	
	public CommandDelKit(CommandKit kit){
		this.kit=kit;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "setkit", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		
		if(player.hasPermission(kPermission.KIT_SET.getPermissionToString())){
			if(args.length<=1){
				player.sendMessage(Text.PREFIX.getText()+"/delkit [Name]");
			}else{
				if(kit.getKits().containsKey(args[0].toLowerCase())){
					kit.getKits().remove(args[0].toLowerCase());
					kit.getKits_delay().remove(args[0].toLowerCase());
					kit.getConfig().set("kits."+args[0].toLowerCase(), null);
					kit.getConfig().save();
					player.sendMessage(Text.PREFIX.getText()+Text.KIT_DEL.getText(args[0]));
				}else{
					player.sendMessage(Text.PREFIX.getText()+Text.KIT_EXIST.getText());
				}
			}
		}
		return false;
	}

}
