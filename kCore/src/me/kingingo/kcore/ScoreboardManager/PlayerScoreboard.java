package me.kingingo.kcore.ScoreboardManager;

import lombok.Getter;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerScoreboard {

	@Getter
	Scoreboard board;
	@Getter
	Player p;
	
	public PlayerScoreboard(Player p){
		this.p=p;
		this.board=Bukkit.getScoreboardManager().getNewScoreboard();
	}
	
	public void setBoard(){
		p.setScoreboard(board);
	}
	
	public void addPlayerToTeam(String Team,Player p){
		if(board.getTeam(Team)==null)return;
		Team r = board.getTeam(Team);
		r.addPlayer(p);
	}
	
	public void addTeam(String Team, String prefix,Player[] list){
		if(board.getTeam(Team)!=null)return;
		Team r = board.registerNewTeam(Team);
		if(prefix!=null)r.setPrefix(prefix);
		for(Player p : list)r.addPlayer(p);
	}
	
	public void addBoard(DisplaySlot typ,String DisplayName){
		if(board.getObjective(typ.name())==null){
			Objective o = board.registerNewObjective(typ.name(), "dummy");
			o.setDisplaySlot(typ);
			o.setDisplayName(DisplayName);
		}
	}
	
	public void resetScore(OfflinePlayer p1,DisplaySlot typ){
		board.resetScores(p1);
	}
	
	public void setScore(OfflinePlayer p2,DisplaySlot typ,int i){
			board.getObjective(typ).getScore(p2).setScore(i);
	}
	
	public void setScore(String p2,DisplaySlot typ,int i){
		board.getObjective(typ).getScore(Bukkit.getOfflinePlayer(p2)).setScore(i);
}
	
}
