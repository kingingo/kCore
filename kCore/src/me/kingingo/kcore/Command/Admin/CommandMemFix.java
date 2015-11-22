package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMemFix implements CommandExecutor{
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "memfix", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			
			if(p.hasPermission(kPermission.COMMAND_MEM.getPermissionToString())){
				System.gc();
				p.sendMessage(Language.getText(p,"PREFIX")+"§aMemory Fix wurde durchgeführt!");
			}
		}else{
			System.gc();
			System.out.println("[EpicPvP]: Memory Fix wurde durchgeführt!");
		}
		return false;
	}
	
}

