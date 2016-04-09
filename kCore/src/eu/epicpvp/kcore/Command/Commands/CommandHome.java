package eu.epicpvp.kcore.Command.Commands;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Command.Admin.CommandAHome;
import eu.epicpvp.kcore.Command.Commands.Events.PlayerHomeEvent;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.TeleportManager.Teleporter;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class CommandHome implements CommandExecutor{
	
	@Getter
	private UserDataConfig userData;
	private Player player;
	private kConfig config;
	private Map<String, Object> list;
	@Getter
	private TeleportManager teleport;
	private Location home;
	
	public CommandHome(UserDataConfig userData,TeleportManager teleport,CommandHandler handler){
		this.userData=userData;
		this.teleport=teleport;
		handler.register(CommandAHome.class, new CommandAHome(this));
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "home", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(player.hasPermission(PermissionType.HOME.getPermissionToString())){
			config = userData.getConfig(player);
			if(args.length==0){
				String homes = "";
				list = config.getPathList("homes");
				for(String s : list.keySet())homes+=s+",";
				player.sendMessage(TranslationManager.getText(player, "PREFIX")+ (homes.equalsIgnoreCase("") ? TranslationManager.getText(player,"HOMES_EMPTY") : "Homes: "+homes.substring(0,homes.length()-1)) );
			}else{
				if(config.isSet("homes."+args[0])){
					home = config.getLocation("homes."+args[0]);
					PlayerHomeEvent ev = new PlayerHomeEvent(player, home, config);
					Bukkit.getPluginManager().callEvent(ev);
					if(ev.isCancelled()){
						if(ev.getReason()!=null)player.sendMessage(TranslationManager.getText(player, "PREFIX")+ev.getReason());
						return false;
					}
					
					if(player.hasPermission(PermissionType.HOME_BYEPASS_DELAY.getPermissionToString())){
						player.teleport(home,TeleportCause.PLUGIN);
						player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "TELEPORT"));
					}else{
						teleport.getTeleport().add(new Teleporter(player,home,5));
					}
				}else{
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+TranslationManager.getText(player, "HOME_EXIST"));
				}
			}
		}
		return false;
	}

}
