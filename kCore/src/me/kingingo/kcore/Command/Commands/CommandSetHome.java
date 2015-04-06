package me.kingingo.kcore.Command.Commands;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.kConfig.kConfig;

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
			player.sendMessage(Text.PREFIX.getText()+"/sethome [Name]");
		}else{
			if(player.hasPermission(kPermission.HOME_SET.getPermissionToString())){
				int a=-1;
				for(String perm : manager.getPlist().get(UtilPlayer.getRealUUID(player)).getPermissions().keySet()){
					if(perm.contains("epicpvp.home.set.")){
						a = Integer.valueOf(perm.substring(("epicpvp.home.set.").length(), perm.length() ));
						break;
					}
				}
				
				if(a!=-1&&config.getPathList("homes").size()>=a){
					player.sendMessage(Text.PREFIX.getText()+Text.HOME_MAX.getText(a));
					return false;
				}
				
				config.setLocation("homes."+args[0], player.getLocation());
				player.sendMessage(Text.PREFIX.getText()+Text.HOME_SET.getText(args[0]));
			}
		}
		return false;
	}

}
