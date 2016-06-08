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

public class CommandUserShop implements CommandExecutor{

	private kConfig config;
	private TeleportManager teleport;
	
	public CommandUserShop(TeleportManager teleport){
		this.config=new kConfig(new File(teleport.getPermManager().getInstance().getDataFolder(), "shops.yml"));
		this.teleport=teleport;
		teleport.getCmd().register(CommandDelUserShop.class, new CommandDelUserShop(config));
		teleport.getCmd().register(CommandSetUserShop.class, new CommandSetUserShop(config));
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "usershop", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		
		if(args.length==0){
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/usershop [Name]");
			if(player.hasPermission(PermissionType.WARP_LIST.getPermissionToString())){
				StringBuilder sb = new StringBuilder();
				Map<String, Object> list = config.getPathList("shops");
				for(String s : list.keySet())sb.append(s).append(", ");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + (sb.length() == 0 ? TranslationHandler.getText(player, "WARPS_EMPTY") : "Shops: " + sb.substring(0, sb.length() - 2)));
			}
		}else{
			if(config.isSet("shops."+args[0].toLowerCase())){
				if(player.hasPermission(PermissionType.WARP_BYEPASS_DELAY.getPermissionToString())){
					player.teleport(config.getLocation("shops."+args[0].toLowerCase()),TeleportCause.PLUGIN);
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "TELEPORT"));
				}else{
					teleport.getTeleport().add(new Teleporter(player,config.getLocation("shops."+args[0].toLowerCase()),3));
				}
			}else{
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "shop_EXIST"));
			}
		}
		return false;
	}

}
