package eu.epicpvp.kcore.Command.Admin;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import eu.epicpvp.datenclient.client.LoadedPlayer;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Command.Commands.CommandHome;
import eu.epicpvp.kcore.Command.Commands.Events.PlayerHomeEvent;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.TeleportManager.Teleporter;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;

public class CommandAHome implements CommandExecutor{

	private CommandHome cmdHome;

	public CommandAHome(CommandHome cmdHome){
		this.cmdHome=cmdHome;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "ahome", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player) sender;
		kConfig config = null;

		if(player.hasPermission(PermissionType.HOME_ADMIN.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")+"/ahome [Player] [Home]");
			}else{
				if(UtilPlayer.isOnline(args[0])){
					config =cmdHome.getUserData().getConfig( Bukkit.getPlayer(args[0]) );

				}else{
					player.sendMessage(TranslationHandler.getText(player,"PREFIX")+TranslationHandler.getText(player,"PLAYER_IS_OFFLINE",args[0]));
					LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(args[0]);
					config =cmdHome.getUserData().loadConfig(loadedplayer.getPlayerId());
				}

				if(config ==null)return false;
				if(args.length==1){
					String homes = "";
					Map<String, Object> list = config.getPathList("homes");
					for(String s : list.keySet())homes+=s+",";
					player.sendMessage(TranslationHandler.getText(player, "PREFIX")+ (homes.equalsIgnoreCase("") ? TranslationHandler.getText(player,"HOMES_EMPTY") : args[0]+" Homes: "+homes.substring(0,homes.length()-1)) );
				}else{
					if(config.isSet("homes."+args[1])){
						Location home = config.getLocation("homes." + args[1]);
						PlayerHomeEvent ev = new PlayerHomeEvent(player, home,args[1], config);
						Bukkit.getPluginManager().callEvent(ev);
						if(ev.isCancelled()){
							if(ev.getReason()!=null) player.sendMessage(TranslationHandler.getText(player, "PREFIX")+ev.getReason());
							return false;
						}

						if(player.hasPermission(PermissionType.HOME_BYEPASS_DELAY.getPermissionToString())){
							player.teleport(home,TeleportCause.PLUGIN);
							player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "TELEPORT"));
						}else{
							cmdHome.getTeleport().getTeleport().add(new Teleporter(player, home,5));
						}
					}else{
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "HOME_EXIST"));
					}
				}
			}
		}
		return false;
	}

}
