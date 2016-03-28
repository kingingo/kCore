package eu.epicpvp.kcore.StatsManager.Exception;

import java.util.UUID;

import lombok.Getter;

@SuppressWarnings("serial")
public class PlayerNotFoundException extends RuntimeException{

	@Getter
	private UUID uuid;
	
	public PlayerNotFoundException(UUID uuid){
		super("Player "+uuid+" not found!");
		this.uuid=uuid;
	}
	
}
