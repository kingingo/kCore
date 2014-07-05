package me.kingingo.kcore.ScoreboardManager;

import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerScoreboard {

	Scoreboard board;
	Player p;
	
	public PlayerScoreboard(Player p){
		this.p=p;
		this.board=Bukkit.getScoreboardManager().getNewScoreboard();
	}
	
	public void addBoard(DisplaySlot typ,String DisplayName){
		if(board.getObjective(typ.name())==null){
			Objective o = board.registerNewObjective(typ.name(), "dummy");
			o.setDisplaySlot(typ);
			o.setDisplayName(DisplayName);
		}
	}
	
	public void resetScore(OfflinePlayer p,DisplaySlot typ){
		board.resetScores(p);
	}
	
	public void setScore(OfflinePlayer p,DisplaySlot typ,int i){
		if(typ==DisplaySlot.SIDEBAR){
			board.getObjective(typ).getScore(p).setScore(i);
		}else{
			for(Player p1 : UtilServer.getPlayers()){
				board.getObjective(typ).getScore(p1).setScore(i);
			}
		}
	}
	
}
