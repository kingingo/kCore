package eu.epicpvp.kcore.Command.Admin;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationManager;

public class CommandToggle extends kListener implements CommandExecutor{
	
	boolean chat=true;
	long online;
	
	public CommandToggle(JavaPlugin instance){
		super(instance,"Toggle");
		this.online=System.currentTimeMillis();
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "toggle", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		if(cs instanceof Player){
			Player p = (Player)cs;
			if(p.hasPermission(PermissionType.COMMAND_TOGGLE.getPermissionToString())){
				if(args.length == 0){
					p.sendMessage(TranslationManager.getText(p, "PREFIX")+"§a/toggle [Plugin]");
					return false;
				}else if(args[0].equalsIgnoreCase("list")){
					String list = "";
					Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
					
					for(Plugin pl: plugins){
						if(pl.isEnabled()){
							list = list + "§a" + pl.getName() + "§7,";
						}else{
							list = list + "§c" + pl.getName() + "§7,";
						}
					}
					
					p.sendMessage(TranslationManager.getText(p, "PREFIX")+"§aPlugins §7: " + list);
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
					p.sendMessage(TranslationManager.getText(p, "PREFIX")+"§cDas Plugin wurde geladen!");
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
						p.sendMessage(TranslationManager.getText(p, "PREFIX")+"§cDu kannst das Plugin nicht Disablen!");
						return false;
					}
					
					if(Bukkit.getPluginManager().isPluginEnabled(args[0])){
						Bukkit.getPluginManager().disablePlugin(pl);
						p.sendMessage(TranslationManager.getText(p, "PREFIX")+"§cDas Plugin " + pl.getName() + " wurde Disable!");
						
					}else{
						
						Bukkit.getPluginManager().enablePlugin(pl);
						p.sendMessage(TranslationManager.getText(p, "PREFIX")+"§aDas Plugin " + pl.getName() + " wurde Enable!");
						
					}
					}else{
						p.sendMessage(TranslationManager.getText(p, "PREFIX")+"§cDas Plugin wurde nicht gefunden " + args[0]);
						return false;
					}
				} 
			}
		}
		return false;
	}
	
}

