package eu.epicpvp.kcore.Command.Admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

import eu.epicpvp.datenserver.definitions.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilScoreboard;

public class CommandAddEpics implements CommandExecutor{

	private StatsManager statsManager;

	public CommandAddEpics(StatsManager statsManager){
		this.statsManager=statsManager;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "addepics", sender = Sender.CONSOLE)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		if(sender instanceof CommandSender){
			if(args.length==0){
				System.out.println("[EpicPvP:] /addepics [Spieler] [+/- Epics]");
			}else if(args.length >= 2){
				String spieler = args[0];

				double c=UtilNumber.toDouble(args[1]);

				if(c==-1)return false;

				if(UtilPlayer.isOnline(spieler)){

					if(Bukkit.getPlayer(spieler).getScoreboard()!=null&&Bukkit.getPlayer(spieler).getScoreboard().getObjective(DisplaySlot.SIDEBAR)!=null){
						Score score = UtilScoreboard.searchScore(Bukkit.getPlayer(spieler).getScoreboard(), String.valueOf(UtilMath.trim(2, statsManager.getDouble(Bukkit.getPlayer(spieler), StatsKey.MONEY))+"$"));
						if(score!=null){
							UtilScoreboard.resetScore(score.getScoreboard(), score.getEntry(), score.getObjective().getDisplaySlot());
							UtilScoreboard.setScore(Bukkit.getPlayer(spieler).getScoreboard(), UtilMath.trim(2, statsManager.getDouble(Bukkit.getPlayer(spieler), StatsKey.MONEY)+c)+"$", score.getObjective().getDisplaySlot(), score.getScore());
						}
					}

					statsManager.addDouble(Bukkit.getPlayer(spieler), c, StatsKey.MONEY);
					System.out.println("[EpicPvP]: Der Spieler "+spieler+" hat die Epics erhalten!");
				}else{
					System.out.println("[EpicPvP]: Der Spieler "+spieler+" ist nicht online!");
				}
			}
		}
		return false;
	}

}
