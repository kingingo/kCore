package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Command.Commands.Events.PlayerSetHomeEvent;
import eu.epicpvp.kcore.Permission.Permission;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.Util.UtilString;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandSetHome implements CommandExecutor{
	
	private UserDataConfig userData;
	private Player player;
	private kConfig config;
	private PermissionManager manager;
	
	public CommandSetHome(UserDataConfig userData,PermissionManager manager){
		this.userData=userData;
		this.manager=manager;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "sethome", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		config = userData.getConfig(player);
		
		if(args.length==0){
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/sethome [Name]");
		}else{
			if(player.hasPermission(PermissionType.HOME.getPermissionToString())){
				int a=-1;
				//epicpvp.home.set.5 damit kann man maximal 5 Homes setzten
				for(Permission perm : manager.getPermissionPlayer(player).getPermissions()){
					if(perm.getPermission().contains("epicpvp.home.set.")){
						a = Integer.valueOf(perm.getPermission().substring(("epicpvp.home.set.").length(), perm.getPermission().length() ));
						break;
					}
				}
				
				if(!UtilString.isNormalCharakter(args[0])){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NO_CHARAKTER"));
					return false;
				}
				
				if(a!=-1&&config.getPathList("homes").size()>=a){
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player,"HOME_MAX",String.valueOf(a)));
					return false;
				}
				
				PlayerSetHomeEvent ev = new PlayerSetHomeEvent(player,player.getLocation(),args[0]);
				Bukkit.getPluginManager().callEvent(ev);
				if(ev.isCancelled()){
					if(ev.getReason()!=null)player.sendMessage(TranslationHandler.getText(player, "PREFIX")+ev.getReason());
					return false;
				}
				
				config.setLocation("homes."+args[0], player.getLocation());
				config.save();
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HOME_SET",args[0]));
			}
		}
		return false;
	}

}
