package me.kingingo.kcore.Arenen;

import lombok.Getter;

import org.bukkit.entity.Player;

public class GameRound {

	@Getter
	private Player[] players;
	@Getter
	private ArenaType type;
	@Getter
	private Player owner;
	
	public GameRound(Player owner,Player[] players,ArenaType type){
		this.players=players;
		this.type=type;
		this.owner=owner;
	}
	
	public void remove(){
		this.type=null;
		this.owner=null;
		this.players=null;
	}
}
