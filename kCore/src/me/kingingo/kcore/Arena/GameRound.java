package me.kingingo.kcore.Arena;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;

public class GameRound {

	@Getter
	@Setter
	private UUID[] players;
	@Getter
	@Setter
	private ArenaType type;
	@Getter
	@Setter
	private UUID owner;
	
	public GameRound(UUID owner,UUID[] players,ArenaType type){
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
