package me.kingingo.kcore.Command.Commands;

import java.io.File;
import java.util.Map;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.TeleportManager.TeleportManager;
import me.kingingo.kcore.TeleportManager.Teleporter;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWarp implements CommandExecutor{
	
	private Player player;
	private kConfig config;
	private Map<String, Object> list;
	private TeleportManager teleport;
	
	public CommandWarp(TeleportManager teleport){
		this.config=new kConfig(new File("plugins"+File.separator+teleport.getPermManager().getInstance().getPlugin(teleport.getPermManager().getInstance().getClass()).getName()+File.separator+"warps.yml"));
		this.teleport=teleport;
		teleport.getCmd().register(CommandDelWarp.class, new CommandDelWarp(config));
		teleport.getCmd().register(CommandSetWarp.class, new CommandSetWarp(config));
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "warp", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(player.hasPermission(kPermission.WARP.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Text.PREFIX.getText()+"/warp [Name]");
				if(player.hasPermission(kPermission.WARP_LIST.getPermissionToString())){
					String warps = "";
					list = config.getPathList("warps");
					for(String s : list.keySet())warps+=s+",";
					player.sendMessage(Text.PREFIX.getText()+ (warps.equalsIgnoreCase("") ? "Es exestieren noch keine Warps" : "Warps: "+warps.substring(0,warps.length()-1)) );
				}
			}else{
				if(args[0].equalsIgnoreCase("premium")&&!player.hasPermission(kPermission.WARP.getPermissionToString()+".premium"))return false;
				if(config.isSet("warps."+args[0])){
					if(player.hasPermission(kPermission.WARP_BYEPASS_DELAY.getPermissionToString())){
						player.teleport(config.getLocation("warps."+args[0]));
						player.sendMessage(Text.PREFIX.getText()+Text.TELEPORT.getText());
					}else{
						teleport.getTeleport().add(new Teleporter(player,config.getLocation("warps."+args[0]),5));
					}
				}else{
					player.sendMessage(Text.PREFIX.getText()+Text.WARP_EXIST.getText());
				}
			}
		}
		return false;
	}

}
