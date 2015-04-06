package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilNumber;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetKit implements CommandExecutor{

	private CommandKit kit;
	private Player player;
	
	public CommandSetKit(CommandKit kit){
		this.kit=kit;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "setkit", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player=(Player)sender;
		
		if(player.hasPermission(kPermission.KIT_SET.getPermissionToString())){
			if(args.length<=1){
				player.sendMessage(Text.PREFIX.getText()+"/setkit [Name] [Delay STD]");
			}else{
				this.kit.getConfig().setInventory("kits."+args[0].toLowerCase()+".Inventory", player.getInventory());
				if(UtilNumber.toInt(args[1])!=0){
					this.kit.getConfig().set("kits."+args[0].toLowerCase()+".Delay", UtilNumber.toInt(args[1])*TimeSpan.HOUR );
					this.kit.getKits_delay().put(args[0].toLowerCase(), UtilNumber.toInt(args[1])*TimeSpan.HOUR );
				}
				this.kit.getKits().put(args[0].toLowerCase(), player.getInventory().getContents().clone());
				kit.getConfig().save();
				player.sendMessage(Text.PREFIX.getText()+Text.KIT_SET.getText(args[0]));
			}
		}
		return false;
	}

}
