package me.kingingo.kcore.Command.Admin;

import java.io.File;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.lag.Lag;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

public class CommandToggle implements CommandExecutor, Listener{
	
	PermissionManager permManager;
	boolean chat=true;
	long online;
	
	public CommandToggle(PermissionManager permManager){
		this.permManager=permManager;
		this.online=System.currentTimeMillis();
		new Lag(permManager.getInstance());
		Bukkit.getPluginManager().registerEvents(this, permManager.getInstance());
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "toggle", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(permManager.hasPermission(p, Permission.COMMAND_TOGGLE)){
				if(args.length == 0){
					p.sendMessage(Text.PREFIX.getText()+"�a/toggle [Plugin]");
					return false;
				}else if(args[0].equalsIgnoreCase("list")){
					String list = "";
					Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
					
					for(Plugin pl: plugins){
						if(pl.isEnabled()){
							list = list + "�a" + pl.getName() + "�7,";
						}else{
							list = list + "�c" + pl.getName() + "�7,";
						}
					}
					
					p.sendMessage(Text.PREFIX.getText()+"�aPlugins �7: " + list);
					return false;
				}else if(args[0].equalsIgnoreCase("load")){
					File pl = new File(args[1]);
					try {
						Bukkit.getPluginManager().loadPlugin(pl);
						
						
					} catch (UnknownDependencyException e) {
						System.err.println(e);
						e.printStackTrace();
					} catch (InvalidPluginException e) {
						System.err.println(e);
						e.printStackTrace();
					} catch (InvalidDescriptionException e) {
						System.err.println(e);
						e.printStackTrace();
					}
					p.sendMessage(Text.PREFIX.getText()+"�cDas Plugin wurde geladen!");
				}else if(args.length == 1){
					
					boolean on = false;
					for(Plugin l: Bukkit.getPluginManager().getPlugins()){
						if(l.getName().equals(args[0])){
							on = true;
						}
					}
					
					if(on){
						Plugin pl = Bukkit.getPluginManager().getPlugin(args[0]);
					if(pl.getName().equalsIgnoreCase("kHub")||pl.getName().equalsIgnoreCase("kSkyBlock")||pl.getName().equalsIgnoreCase("kCore")||pl.getName().equalsIgnoreCase("kPvP")||pl.getName().equalsIgnoreCase("kArcade")||pl.getName().equalsIgnoreCase("kWarZ")){
						p.sendMessage(Text.PREFIX.getText()+"�cDu kannst das Plugin nicht Disablen!");
						return false;
					}
					
					if(Bukkit.getPluginManager().isPluginEnabled(args[0])){
						Bukkit.getPluginManager().disablePlugin(pl);
						p.sendMessage(Text.PREFIX.getText()+"�cDas Plugin " + pl.getName() + " wurde Disable!");
						
					}else{
						
						Bukkit.getPluginManager().enablePlugin(pl);
						p.sendMessage(Text.PREFIX.getText()+"�aDas Plugin " + pl.getName() + " wurde Enable!");
						
					}
					}else{
						p.sendMessage(Text.PREFIX.getText()+"�cDas Plugin wurde nicht gefunden " + args[0]);
						return false;
					}
				} 
			}
		}
		return false;
	}
	
}
