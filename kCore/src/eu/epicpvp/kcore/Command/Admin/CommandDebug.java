package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Util.UtilDebug;

public class CommandDebug implements CommandExecutor{
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "kdebug", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
			Player p = (Player)sender;
			if(p.isOp()){
				if(args.length==0){
					p.sendMessage(Language.getText(p, "PREFIX")+"§aDebug: "+ (UtilDebug.isDebug() ? "§atrue" : "§cfalse") );
				}else{
					if(args[0].equalsIgnoreCase("on")||args[0].equalsIgnoreCase("true")||args[0].equalsIgnoreCase("an")){
						UtilDebug.setDebug(true);
						p.sendMessage(Language.getText(p, "PREFIX")+"§aDebug: "+ (UtilDebug.isDebug() ? "§atrue" : "§cfalse") );
					}else if(args[0].equalsIgnoreCase("off")||args[0].equalsIgnoreCase("false")||args[0].equalsIgnoreCase("aus")){
						UtilDebug.setDebug(false);
						p.sendMessage(Language.getText(p, "PREFIX")+"§aDebug: "+ (UtilDebug.isDebug() ? "§atrue" : "§cfalse") );
					}
				}
			}
		return false;
	}
	
}
