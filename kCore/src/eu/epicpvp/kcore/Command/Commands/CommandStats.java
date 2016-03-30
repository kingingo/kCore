package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.StatsManager.Ranking;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import lombok.Getter;

public class CommandStats implements CommandExecutor{
	@Getter
	private StatsManager statsManager;
	private Ranking ranking;
	
	public CommandStats(StatsManager statsmanager,MySQL mysql){
		this.statsManager=statsmanager;
		this.ranking=new Ranking(mysql,statsManager, StatsKey.KILLS, -1, 10);
		this.statsManager.addRanking(ranking);
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "stats", alias = {"kdr","money"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(args.length==0){
			p.sendMessage(Language.getText(p, "STATS_PREFIX"));
			p.sendMessage(Language.getText(p, "STATS_KILLS")+statsManager.getInt(p, StatsKey.KILLS));
			p.sendMessage(Language.getText(p, "STATS_DEATHS")+statsManager.getInt(p, StatsKey.DEATHS));
			p.sendMessage(Language.getText(p, "STATS_KDR")+getStatsManager().getKDR(statsManager.getInt(p, StatsKey.KILLS), statsManager.getInt(p, StatsKey.DEATHS)));
//			p.sendMessage(Language.getText(p, "STATS_RANKING")+getStatsManager().getRank(Stats.KILLS, p));
		}else if(args[0].equalsIgnoreCase("ranking")){
			if(args.length==1){
				getStatsManager().SendRankingMessage(p, ranking);
			}else{
				getStatsManager().SendRankingMessage(p, ranking);
			}
			
		}
		return false;
	}
	
}
