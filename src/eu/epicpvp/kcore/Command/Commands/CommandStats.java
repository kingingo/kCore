package eu.epicpvp.kcore.Command.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.StatsManager.Ranking;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import lombok.Getter;

public class CommandStats implements CommandExecutor{
	@Getter
	private StatsManager statsManager;
	private Ranking ranking;
	
	public CommandStats(StatsManager statsmanager){
		this.statsManager=statsmanager;
		this.ranking=new Ranking(statsmanager.getType(),StatsKey.KILLS);
		this.statsManager.addRanking(ranking);
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "stats", alias = {"kdr","money"}, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
		Player p = (Player)cs;
		if(args.length==0){
			int kills = statsManager.getInt(p, StatsKey.KILLS);
			int deaths = statsManager.getInt(p, StatsKey.DEATHS);
			p.sendMessage(TranslationHandler.getText(p, "STATS_PREFIX"));
			p.sendMessage(TranslationHandler.getText(p, "STATS_KILLS")+ kills);
			p.sendMessage(TranslationHandler.getText(p, "STATS_DEATHS")+ deaths);
			p.sendMessage(TranslationHandler.getText(p, "STATS_KDR")+getStatsManager().getKDR(kills, deaths));
		}else if(args[0].equalsIgnoreCase("ranking")){
			getStatsManager().SendRankingMessage(p, ranking);
		}
		return false;
	}
	
}
