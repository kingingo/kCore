package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandMemFix implements CommandExecutor, Listener{
	
	PermissionManager permManager;
	boolean chat=true;
	long online;
	
	public CommandMemFix(PermissionManager permManager){
		this.permManager=permManager;
		this.online=System.currentTimeMillis();
		UtilServer.createLagListener(permManager.getInstance());
		Bukkit.getPluginManager().registerEvents(this, permManager.getInstance());
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "memfix", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			
			if(permManager.hasPermission(p, kPermission.COMMAND_MEM)){
				System.gc();
				p.sendMessage("§aMemory Fix wurde durchgeführt!");
			}
		}else{
			System.gc();
			System.out.println("Memory Fix wurde durchgeführt!");
		}
		return false;
	}
	
}

