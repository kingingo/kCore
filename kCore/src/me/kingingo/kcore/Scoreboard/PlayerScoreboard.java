package me.kingingo.kcore.Scoreboard;

import java.util.ArrayList;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PlayerScoreboard {

	@Getter
	Scoreboard board;
	@Getter
	ArrayList<Player> players;
	
	public PlayerScoreboard(){
		this.players=new ArrayList<Player>();
		this.board=Bukkit.getScoreboardManager().getNewScoreboard();
	}
	
	public PlayerScoreboard(Player player){
		this.players=new ArrayList<Player>();
		this.players.add(player);
		this.board=Bukkit.getScoreboardManager().getNewScoreboard();
	}
	
	public PlayerScoreboard(ArrayList<Player> players){
		this.players=players;
		this.board=Bukkit.getScoreboardManager().getNewScoreboard();
	}
	
	public PlayerScoreboard(Player[] players){
		this.players=new ArrayList<Player>();
		for(Player player : players)this.players.add(player);
		this.board=Bukkit.getScoreboardManager().getNewScoreboard();
	}
	
	public void resetScoreboard(){
		this.board=Bukkit.getScoreboardManager().getNewScoreboard();
		for(Player player : this.players)player.setScoreboard(this.board);
		this.players.clear();
		this.players=null;
		this.board=null;
	}
	
	public void addPlayer(Player player,boolean set){
		this.players.add(player);
		if(set)player.setScoreboard(board);
	}
	
	public void removePlayer(Player player){
		if(this.players.contains(player)){
			this.players.remove(player);
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}
	
	public void setBoard(Player player){
		if(players.contains(player)){
			player.setScoreboard(board);
		}else{
			addPlayer(player, true);
		}
	}
	
	public void setBoard(){
		for(Player player: players)player.setScoreboard(board);
	}
	
	public void addPlayerToTeam(String Team,Player p){
		if(board.getTeam(Team)==null)return;
		Team r = board.getTeam(Team);
		r.addPlayer(p);
	}
	
	public Team addTeam(String Team, String prefix){
		if(board.getTeam(Team)!=null)return null;
		Team r = board.registerNewTeam(Team);
		if(prefix!=null)r.setPrefix(prefix);
		return r;
	}
	
	public Team addTeam(String Team, String prefix,ArrayList<Player> list){
		if(board.getTeam(Team)!=null)return null;
		Team r = board.registerNewTeam(Team);
		if(prefix!=null)r.setPrefix(prefix);
		if(list!=null)for(Player p : list)r.addPlayer(p);
		return r;
	}
	
	public Team addTeam(String Team, String prefix,Player[] list){
		if(board.getTeam(Team)!=null)return null;
		Team r = board.registerNewTeam(Team);
		if(prefix!=null)r.setPrefix(prefix);
		if(list!=null)for(Player p : list)r.addPlayer(p);
		return r;
	}
	
	public void addBoard(DisplaySlot typ,String DisplayName){
		
		if(typ==DisplaySlot.SIDEBAR&&DisplayName.length()>=32){
			DisplayName=DisplayName.substring(0,31);
		}
		if(board.getObjective(typ.name())==null){
			Objective o = board.registerNewObjective(typ.name(), "dummy");
			o.setDisplaySlot(typ);
			o.setDisplayName(DisplayName);
		}
	}
	
	public void resetScore(String p1,DisplaySlot typ){
		board.resetScores(p1);
	}
	
	public void setScore(String p2,DisplaySlot typ,int i){
		board.getObjective(typ).getScore(p2).setScore(i);
	}
	
}
