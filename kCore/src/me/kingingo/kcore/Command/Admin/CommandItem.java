package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Util.UtilNumber;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandItem implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "i", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(p.isOp()){
				if(args[0].contains(":")){
					p.getInventory().addItem(new ItemStack(UtilNumber.toInt(args[0].split(":")[0]), (args.length==2 ? UtilNumber.toInt(args[1]) : 1) , UtilNumber.toByte(args[0].split(":")[1])));
				}else{
					p.getInventory().addItem(new ItemStack(UtilNumber.toInt(args[0]), (args.length==2 ? UtilNumber.toInt(args[1]) : 1) ));
				}
			}
		}
		return false;
	}
	
}

