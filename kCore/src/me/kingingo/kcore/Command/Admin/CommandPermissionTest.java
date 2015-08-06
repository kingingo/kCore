package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandPermissionTest implements CommandExecutor{
	
	private PermissionManager perm;
	
	public CommandPermissionTest(PermissionManager perm){
		this.perm=perm;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "permtest", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof ConsoleCommandSender){
			if(args.length==0){
				System.out.println("[EpicPvP]: /permtest [Spieler]");
			}else{
				if(UtilPlayer.isOnline(args[0])){
					Player p = Bukkit.getPlayer(args[0]);
					System.out.println("Group: "+perm.getGroup(p));
					for(String pe : perm.getPermissionList(p))System.out.println("PERM: "+pe);
				}
			}
		}
		return false;
	}
	
}
