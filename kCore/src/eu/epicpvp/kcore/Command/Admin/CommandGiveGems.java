package eu.epicpvp.kcore.Command.Admin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

import dev.wolveringer.client.Callback;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Util.UtilInteger;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilScoreboard;

public class CommandGiveGems implements CommandExecutor{
	
	private StatsManager money;
	
	public CommandGiveGems(StatsManager money){
		this.money=money;
	}
	
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "givegems", sender = Sender.EVERYONE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(player.hasPermission(PermissionType.ALL_PERMISSION.getPermissionToString())){
				if(args.length==0){
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+"§a/GiveGems [Spieler] [+/- Gems]");
				}else if(args.length >= 2){
					String spieler = args[0];
					int c=UtilInteger.isNumber(args[1]);
					
					if(c==-1)return false;
					
					if(UtilPlayer.isOnline(spieler)){
						if(Bukkit.getPlayer(spieler).getScoreboard()!=null&&Bukkit.getPlayer(spieler).getScoreboard().getObjective(DisplaySlot.SIDEBAR)!=null){
							Score score = UtilScoreboard.searchScore(Bukkit.getPlayer(spieler).getScoreboard(), String.valueOf("§a§r"+money.getInt(Bukkit.getPlayer(spieler), StatsKey.GEMS)));
							if(score!=null){
								UtilScoreboard.resetScore(score.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot());
								UtilScoreboard.setScore(Bukkit.getPlayer(spieler).getScoreboard(), "§a§r"+money.getInt(Bukkit.getPlayer(spieler), StatsKey.GEMS)+c, score.getObjective().getDisplaySlot(), score.getScore());
							}
						}
						
						money.add(Bukkit.getPlayer(spieler), StatsKey.GEMS, c);
					}else{
						money.loadPlayer(spieler, new Callback<UUID>() {
							
							@Override
							public void call(UUID uuid) {
								money.add(uuid, null, StatsKey.GEMS, c);
							}
						});
					}
					
					player.sendMessage(TranslationManager.getText(player, "PREFIX")+(c<0?TranslationManager.getText(player, "GEMS_DEL_PLAYER",new String[]{player.getName(),String.valueOf(c)}):TranslationManager.getText(player, "GEMS_ADD_PLAYER",new String[]{player.getName(),String.valueOf(c)})));
				}
			}
		}else if(sender instanceof CommandSender){
			if(args.length==0){
				System.out.println("[EpicPvP:] /GiveGems [Spieler] [+/- Gems]");
			}else if(args.length >= 2){
				String spieler = args[0];
				
				int c=UtilInteger.isNumber(args[1]);
				
				if(c==-1)return false;
				
				if(UtilPlayer.isOnline(spieler)){
					if(Bukkit.getPlayer(spieler).getScoreboard()!=null&&Bukkit.getPlayer(spieler).getScoreboard().getObjective(DisplaySlot.SIDEBAR)!=null){
						Score score = UtilScoreboard.searchScore(Bukkit.getPlayer(spieler).getScoreboard(), String.valueOf("§a§r"+money.getInt(Bukkit.getPlayer(spieler), StatsKey.GEMS)));
						if(score!=null){
							UtilScoreboard.resetScore(score.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot());
							UtilScoreboard.setScore(Bukkit.getPlayer(spieler).getScoreboard(), "§a§r"+money.getInt(Bukkit.getPlayer(spieler), StatsKey.GEMS)+c, score.getObjective().getDisplaySlot(), score.getScore());
						}
					}
					
					money.add(Bukkit.getPlayer(spieler), StatsKey.GEMS, c);
				}else{
					money.loadPlayer(spieler, new Callback<UUID>() {
						
						@Override
						public void call(UUID uuid) {
							money.add(uuid, null, StatsKey.GEMS, c);
						}
					});
				}
				
				System.out.println("[EpicPvP]: Der Spieler "+spieler+" hat die Gems erhalten!");
			}
		}
		return false;
	}
	
}
