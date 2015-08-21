package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Command.Commands.Events.PlayerSetHomeEvent;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilString;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetHome implements CommandExecutor{
	
	private UserDataConfig userData;
	private Player player;
	private kConfig config;
	private PermissionManager manager;
	
	public CommandSetHome(UserDataConfig userData,PermissionManager manager){
		this.userData=userData;
		this.manager=manager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "sethome", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		config = userData.getConfig(player);
		
		if(args.length==0){
			player.sendMessage(Language.getText(player, "PREFIX")+"/sethome [Name]");
		}else{
			if(player.hasPermission(kPermission.HOME.getPermissionToString())){
				int a=-1;
				for(String perm : manager.getPlist().get(UtilPlayer.getRealUUID(player)).getPermissions().keySet()){
					if(perm.contains("epicpvp.home.set.")){
						a = Integer.valueOf(perm.substring(("epicpvp.home.set.").length(), perm.length() ));
						break;
					}
				}
				
				if(!UtilString.isNormalCharakter(args[0])){
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_CHARAKTER"));
					return false;
				}
				
				if(a!=-1&&config.getPathList("homes").size()>=a){
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player,"HOME_MAX",String.valueOf(a)));
					return false;
				}
				
				PlayerSetHomeEvent ev = new PlayerSetHomeEvent(player,player.getLocation(),args[0]);
				Bukkit.getPluginManager().callEvent(ev);
				if(ev.isCancelled()){
					if(ev.getReason()!=null)player.sendMessage(Language.getText(player, "PREFIX")+ev.getReason());
					return false;
				}
				
				config.setLocation("homes."+args[0], player.getLocation());
				config.save();
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "HOME_SET",args[0]));
			}
		}
		return false;
	}

}
