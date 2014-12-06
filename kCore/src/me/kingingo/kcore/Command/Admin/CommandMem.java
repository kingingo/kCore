package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Util.UtilTime;
import me.kingingo.kcore.lag.Lag;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandMem implements CommandExecutor, Listener{
	
	PermissionManager permManager;
	boolean chat=true;
	long online;
	
	public CommandMem(PermissionManager permManager){
		this.permManager=permManager;
		this.online=System.currentTimeMillis();
		new Lag(permManager.getInstance());
		Bukkit.getPluginManager().registerEvents(this, permManager.getInstance());
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "mem", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(permManager.hasPermission(p, Permission.COMMAND_MEM)){
				 Runtime run = Runtime.getRuntime();
				 p.sendMessage("§7Online:§b "+UtilTime.formatMili(System.currentTimeMillis() - online)+"§7 TPS:§b "+Math.round((Lag.getTPS()/20)*100)+"%§7 Ram: §b" + run.totalMemory() / 1048576L + " MB§7 / §b" + run.maxMemory() / 1048576L + " MB");
			}
		}else{
			 Runtime run = Runtime.getRuntime();
			 System.out.println("Online: "+UtilTime.formatMili(System.currentTimeMillis() - online)+" TPS: "+Math.round((Lag.getTPS()/20)*100)+"% Ram: " + run.totalMemory() / 1048576L + " MB / " + run.maxMemory() / 1048576L + " MB");
		}
		return false;
	}
	
}

