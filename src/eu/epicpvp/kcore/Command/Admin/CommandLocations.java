package eu.epicpvp.kcore.Command.Admin;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilFile;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandLocations implements CommandExecutor{
	
	private static kConfig config;
	
	public CommandLocations(JavaPlugin instance){
		config=new kConfig(UtilFile.getYMLFile(instance, "locations"));
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "loc", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
			if(args.length==0){
				for(String s : getLocations())
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/loc "+s);
			}else{
				setLocation(player.getLocation(), args[0]);
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"§aDie Location wurde gespeichert!");
			}
		}
		return false;
	}
	
	public static void delLocation(String s){
		s=s.toLowerCase();
		List<String> l = getLocations();
		if(l.contains(s))l.remove(s);
		config.set("Locations",l);
		
		config.set(s, null);
		config.save();
	}
	
	public static void setLocation(Location loc,String s){
		s=s.toLowerCase();
		List<String> l = getLocations();
		if(!l.contains(s))l.add(s);
		config.set("Locations",l);
		
		config.setLocation(s, loc);
		config.save();
	}
	
	public static List<String> getLocations(){
		return config.getStringList("Locations");
	}

	public static Location getLocation(String s){
		s=s.toLowerCase();
		return getLocation(new Location(Bukkit.getWorld("world"),0,90,0),s);
	}

	public static Location getLocation(Location loc,String s){
		s=s.toLowerCase();
		if(config.contains(s)){
			return config.getLocation(s);
		}else{
			setLocation(loc, s);
			return loc;
		}
	}
	
}
