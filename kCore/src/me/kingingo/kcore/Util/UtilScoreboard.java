package me.kingingo.kcore.Util;

import java.util.ArrayList;
import java.util.Set;

import me.kingingo.kcore.Scoreboard.Events.PlayerSetScoreboardEvent;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class UtilScoreboard {
	
	public static void setTeams(Scoreboard board,Set<Team> teams){
		
		Team tt;
		for(Team team : teams){
			if(!UtilScoreboard.existTeam(board,team.getName())){
				tt = board.registerNewTeam(team.getName());
				tt.setPrefix(team.getPrefix());
			}
			
			tt=board.getTeam(team.getName());
			for(OfflinePlayer player : team.getPlayers()){
				if(!tt.hasPlayer(player)){
					tt.addPlayer(player);
				}
			}
		}
	}
	
	public static Set<Team> cloneTeams(Scoreboard board){
		return board.getTeams();
	}
	
	public static void setScoreboard(Scoreboard board,Player player){
		player.setScoreboard(board);
		Bukkit.getPluginManager().callEvent(new PlayerSetScoreboardEvent(player));
	}
	
	public static void removePlayer(Player player){
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	public static boolean existTeam(Scoreboard board,String Team){
		return (board.getTeam(Team)!=null);
	}
	
	public static void addPlayerToTeam(Scoreboard board,String Team,Player p){
		if(board.getTeam(Team)==null)return;
		Team r = board.getTeam(Team);
		r.addPlayer(p);
	}
	
	public static Team addTeam(Scoreboard board,String Team, String prefix,String suffix){
		if(board.getTeam(Team)!=null)return null;
		Team r = board.registerNewTeam(Team);
		String pr = UtilString.cut(prefix);
		if(prefix!=null)r.setPrefix(pr);
		if(suffix!=null)r.setSuffix(UtilString.cut(suffix));
		return r;
	}
	
	public static Team addTeam(Scoreboard board,String Team, String prefix,String suffix,ArrayList<Player> list){
		if(board.getTeam(Team)!=null)return null;
		Team r = board.registerNewTeam(Team);
		if(prefix!=null)r.setPrefix(UtilString.cut(prefix));
		if(list!=null)for(Player p : list)r.addPlayer(p);
		if(suffix!=null)r.setSuffix(UtilString.cut(suffix));
		return r;
	}
	
	public static Team addTeam(Scoreboard board,String Team, String prefix,String suffix,Player[] list){
		if(board.getTeam(Team)!=null)return null;
		Team r = board.registerNewTeam(Team);
		if(prefix!=null)r.setPrefix(UtilString.cut(prefix));
		if(suffix!=null)r.setSuffix(UtilString.cut(suffix));
		if(list!=null)for(Player p : list)r.addPlayer(p);
		return r;
	}
	
	public static Team addTeam(Scoreboard board,String Team, String prefix){
		if(board.getTeam(Team)!=null)return null;
		Team r = board.registerNewTeam(Team);
		String pr = UtilString.cut(prefix);
		if(prefix!=null)r.setPrefix(pr);
		return r;
	}
	
	public static Team addTeam(Scoreboard board,String Team, String prefix,ArrayList<Player> list){
		if(board.getTeam(Team)!=null)return null;
		Team r = board.registerNewTeam(Team);
		if(prefix!=null)r.setPrefix(UtilString.cut(prefix));
		if(list!=null)for(Player p : list)r.addPlayer(p);
		return r;
	}
	
	public static Team addTeam(Scoreboard board,String Team, String prefix,Player[] list){
		if(board.getTeam(Team)!=null)return null;
		Team r = board.registerNewTeam(Team);
		if(prefix!=null)r.setPrefix(UtilString.cut(prefix));
		if(list!=null)for(Player p : list)r.addPlayer(p);
		return r;
	}
	
	public static void resetScore(Scoreboard board,String p1,DisplaySlot typ){
		board.resetScores(p1);
	}
	
	public static void setScore(Scoreboard board,String p2,DisplaySlot typ,int i){
		board.getObjective(typ).getScore(p2).setScore(i);
	}
	
	public static void addBoard(Scoreboard board,DisplaySlot typ,String DisplayName){
		if(typ==DisplaySlot.SIDEBAR&&DisplayName.length()>=32){
			DisplayName=DisplayName.substring(0,31);
		}
		if(board.getObjective(typ.name())==null){
			Objective o = board.registerNewObjective(typ.name(), "dummy");
			o.setDisplaySlot(typ);
			o.setDisplayName(DisplayName);
		}
	}
	
}
