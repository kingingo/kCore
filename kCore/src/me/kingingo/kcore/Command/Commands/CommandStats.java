package me.kingingo.kcore.Command.Commands;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.StatsManager.Ranking;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStats implements CommandExecutor{
	@Getter
	private StatsManager statsManager;
	private Ranking ranking;
	
	public CommandStats(StatsManager statsmanager){
		this.statsManager=statsmanager;
		this.ranking=new Ranking(statsManager, Stats.KILLS, -1, 10);
		this.statsManager.addRanking(ranking);
	}

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "stats", alias = {"kdr","money"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		if(args.length==0){
			p.sendMessage(Language.getText(p, "STATS_PREFIX"));
			p.sendMessage(Language.getText(p, "STATS_KILLS")+getStatsManager().getInt(Stats.KILLS, p));
			p.sendMessage(Language.getText(p, "STATS_DEATHS")+getStatsManager().getInt(Stats.DEATHS, p));
			p.sendMessage(Language.getText(p, "STATS_KDR")+getStatsManager().getKDR(getStatsManager().getInt(Stats.KILLS, p), getStatsManager().getInt(Stats.DEATHS, p)));
			p.sendMessage(Language.getText(p, "STATS_RANKING")+getStatsManager().getRank(Stats.KILLS, p));
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
