package eu.epicpvp.kcore.Kit.Perks.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Kit.Perk;
import lombok.Getter;

public class PerkPlayerAddEvent extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	private Perk perk;
	
	public PerkPlayerAddEvent(Player player,Perk perk){
		this.player=player;
		this.perk=perk;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}
