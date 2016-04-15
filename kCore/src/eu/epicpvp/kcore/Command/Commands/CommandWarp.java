package eu.epicpvp.kcore.Command.Commands;

import java.io.File;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.TeleportManager;
import eu.epicpvp.kcore.TeleportManager.Teleporter;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.kConfig.kConfig;

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
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "warp", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(player.hasPermission(PermissionType.WARP.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/warp [Name]");
				if(player.hasPermission(PermissionType.WARP_LIST.getPermissionToString())){
					String warps = "";
					list = config.getPathList("warps");
					for(String s : list.keySet())warps+=s+",";
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+ (warps.equalsIgnoreCase("") ? TranslationHandler.getText(player, "WARPS_EMPTY") : "Warps: "+warps.substring(0,warps.length()-1)) );
				}
			}else{
				if(args[0].equalsIgnoreCase("premium")&&!player.hasPermission(PermissionType.WARP.getPermissionToString()+".premium"))return false;
				if(config.isSet("warps."+args[0].toLowerCase())){
					if(player.hasPermission(PermissionType.WARP_BYEPASS_DELAY.getPermissionToString())){
						player.teleport(config.getLocation("warps."+args[0].toLowerCase()),TeleportCause.PLUGIN);
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "TELEPORT"));
					}else{
						teleport.getTeleport().add(new Teleporter(player,config.getLocation("warps."+args[0].toLowerCase()),3));
					}
				}else{
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "WARP_EXIST"));
				}
			}
		}
		return false;
	}

}
