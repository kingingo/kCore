package eu.epicpvp.kcore.Command.Admin;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Command.Commands.CommandHome;
import eu.epicpvp.kcore.Command.Commands.Events.PlayerHomeEvent;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.Teleporter;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandAHome implements CommandExecutor{
	
	private CommandHome cmdHome;
	private Player player;
	private kConfig config;
	private Map<String, Object> list;
	private Location home;
	
	public CommandAHome(CommandHome cmdHome){
		this.cmdHome=cmdHome;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "ahome", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		config=null;
		
		if(player.hasPermission(PermissionType.HOME_ADMIN.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/ahome [Player] [Home]");
			}else{
				if(UtilPlayer.isOnline(args[0])){
					config=cmdHome.getUserData().getConfig( Bukkit.getPlayer(args[0]) );
					
				}else{
					player.sendMessage(Language.getText(player,"PREFIX")+Language.getText(player,"PLAYER_IS_OFFLINE",args[0]));
					UUID uuid = UtilPlayer.getUUID(args[0], UtilServer.getMysql());
					if(uuid!=null){
						config=cmdHome.getUserData().loadConfig(uuid);
					}
				}
				
				if(config==null)return false;
				if(args.length==1){
					String homes = "";
					list = config.getPathList("homes");
					for(String s : list.keySet())homes+=s+",";
					player.sendMessage(Language.getText(player, "PREFIX")+ (homes.equalsIgnoreCase("") ? Language.getText(player,"HOMES_EMPTY") : args[0]+" Homes: "+homes.substring(0,homes.length()-1)) );	
				}else{
					if(config.isSet("homes."+args[1])){
						home = config.getLocation("homes."+args[1]);
						PlayerHomeEvent ev = new PlayerHomeEvent(player, home, config);
						Bukkit.getPluginManager().callEvent(ev);
						if(ev.isCancelled()){
							if(ev.getReason()!=null)player.sendMessage(Language.getText(player, "PREFIX")+ev.getReason());
							return false;
						}
						
						if(player.hasPermission(PermissionType.HOME_BYEPASS_DELAY.getPermissionToString())){
							player.teleport(home,TeleportCause.PLUGIN);
							player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "TELEPORT"));
						}else{
							cmdHome.getTeleport().getTeleport().add(new Teleporter(player,home,5));
						}
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "HOME_EXIST"));
					}
				}
			}
		}
		return false;
	}

}
