package me.kingingo.kcore.Command.Admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Arena.ArenaManager;
import me.kingingo.kcore.Arena.ArenaType;
import me.kingingo.kcore.Arena.GameRound;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Command.Commands.CommandHome;
import me.kingingo.kcore.Command.Commands.Events.PlayerHomeEvent;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.TeleportManager.Teleporter;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class CommandStatsAdmin implements CommandExecutor{
	
	@Getter
	private StatsManager statsManager;
	
	public CommandStatsAdmin(StatsManager statsManager){
		this.statsManager=statsManager;
	}
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "statsadmin", alias={"sa"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		Player player = (Player)sender;
		
		if(player.hasPermission(kPermission.STATS_ADMIN.getPermissionToString())){
			if(args.length==0){
				player.sendMessage("/statsadmin reset all");
				player.sendMessage("/statsadmin reset [Player]");
				player.sendMessage("/statsadmin add [Player] [Stats] [-/+ INT]");
				player.sendMessage("/statsadmin set [Player] [Stats] [-/+ INT]");
			}else{
				if((args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("set"))&&args.length==3){
					UUID uuid = null;
					
					if(UtilPlayer.isOnline(args[1])){
						uuid=UtilPlayer.getRealUUID(Bukkit.getPlayer(args[1]));
					}else{
						uuid=UtilPlayer.getUUID(args[1], statsManager.getMysql());
					}
					
					Stats s = Stats.get(args[2]);
					
					if(s!=null){
						try{
							int i = Integer.valueOf(args[3]);
							
							if(args[0].equalsIgnoreCase("set")){
								player.sendMessage(Language.getText(player, "PREFIX")+(i<0?"§c":"§a")+"Die Stats Daten wurden auf §e"+statsManager.setInt(uuid, i, s)+(i<0?"§c":"§a")+" gesetzt!");
							}else{
								player.sendMessage(Language.getText(player, "PREFIX")+(i<0?"§c":"§a")+"Die Stats Daten wurden auf §e"+statsManager.addInt(uuid, i, s)+(i<0?"§c":"§a")+" geändert!");
							}
						}catch(NumberFormatException e){
							player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_INTEGER", args[3]));
						}
					}else{
						player.sendMessage(Language.getText(player, "PREFIX")+"§cStats Type nicht gefunden!?");
					}
				}else if(args[0].equalsIgnoreCase("reset") && args.length==2){
						Player target = null;
						
						if(UtilPlayer.isOnline(args[1])){
							target=Bukkit.getPlayer(args[1]);
						}else{
							target=UtilPlayer.loadPlayer(UtilPlayer.getUUID(args[1], statsManager.getMysql()));
						}
						
						if(target!=null){
							statsManager.deleteEintrag(target);
							statsManager.createEintrag(target);
							player.sendMessage(Language.getText(player, "PREFIX")+"§cDie Stats wurden von den Spieler §e"+target.getName()+"§c resetet!");
						}else{
							player.sendMessage(Language.getText(player, "PREFIX")+"§cDieser Spieler wurde nicht gefunden!");
						}
					}
			}
		}
		return false;
	}

}
