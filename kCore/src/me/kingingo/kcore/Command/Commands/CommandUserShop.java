package me.kingingo.kcore.Command.Commands;

import java.io.File;
import java.util.Map;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.TeleportManager.TeleportManager;
import me.kingingo.kcore.TeleportManager.Teleporter;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class CommandUserShop implements CommandExecutor{
	
	private Player player;
	private kConfig config;
	private Map<String, Object> list;
	private TeleportManager teleport;
	
	public CommandUserShop(TeleportManager teleport){
		this.config=new kConfig(new File("plugins"+File.separator+teleport.getPermManager().getInstance().getPlugin(teleport.getPermManager().getInstance().getClass()).getName()+File.separator+"shops.yml"));
		this.teleport=teleport;
		teleport.getCmd().register(CommandDelUserShop.class, new CommandDelUserShop(config));
		teleport.getCmd().register(CommandSetUserShop.class, new CommandSetUserShop(config));
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "usershop", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		
		if(args.length==0){
			player.sendMessage(Language.getText(player, "PREFIX")+"/usershop [Name]");
			if(player.hasPermission(kPermission.WARP_LIST.getPermissionToString())){
				String warps = "";
				list = config.getPathList("shops");
				for(String s : list.keySet())warps+=s+",";
				player.sendMessage(Language.getText(player, "PREFIX")+ (warps.equalsIgnoreCase("") ? Language.getText(player, "WARPS_EMPTY") : "Shops: "+warps.substring(0,warps.length()-1)) );
			}
		}else{
			if(config.isSet("shops."+args[0].toLowerCase())){
				if(player.hasPermission(kPermission.WARP_BYEPASS_DELAY.getPermissionToString())){
					player.teleport(config.getLocation("shops."+args[0].toLowerCase()),TeleportCause.PLUGIN);
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "TELEPORT"));
				}else{
					teleport.getTeleport().add(new Teleporter(player,config.getLocation("shops."+args[0].toLowerCase()),3));
				}
			}else{
				player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "shop_EXIST"));
			}
		}
		return false;
	}

}
