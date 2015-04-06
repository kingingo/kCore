package me.kingingo.kcore.Command.Commands;

import java.util.Map;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.TeleportManager.TeleportManager;
import me.kingingo.kcore.TeleportManager.Teleporter;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHome implements CommandExecutor{
	
	private UserDataConfig userData;
	private Player player;
	private kConfig config;
	private Map<String, Object> list;
	private TeleportManager teleport;
	
	public CommandHome(UserDataConfig userData,TeleportManager teleport){
		this.userData=userData;
		this.teleport=teleport;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "home", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(player.hasPermission(kPermission.HOME.getPermissionToString())){
			config = userData.getConfig(player);
			if(args.length==0){
				String homes = "";
				list = config.getPathList("homes");
				for(String s : list.keySet())homes+=s+",";
				player.sendMessage(Text.PREFIX.getText()+ (homes.equalsIgnoreCase("") ? "Du hast keine Homes" : "Homes: "+homes.substring(0,homes.length()-1)) );
			}else{
				if(config.isSet("homes."+args[0])){
					if(player.hasPermission(kPermission.HOME_BYEPASS_DELAY.getPermissionToString())){
						player.teleport(config.getLocation("homes."+args[0]));
						player.sendMessage(Text.PREFIX.getText()+Text.TELEPORT.getText());
					}else{
						teleport.getTeleport().add(new Teleporter(player,config.getLocation("homes."+args[0]),5));
					}
				}else{
					player.sendMessage(Text.PREFIX.getText()+Text.HOME_EXIST.getText());
				}
			}
		}
		return false;
	}

}
