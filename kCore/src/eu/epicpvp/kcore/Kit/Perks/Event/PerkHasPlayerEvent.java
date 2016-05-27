package eu.epicpvp.kcore.Kit.Perks.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Kit.PerkData;
import lombok.Getter;

public class PerkHasPlayerEvent extends Event implements Cancellable{
	private static HandlerList handlers = new HandlerList();
	private boolean cancel=false;
	@Getter
	private Perk perk;
	@Getter
	private Player player;
	@Getter
	private PerkData perkData;
	
	public PerkHasPlayerEvent(Perk perk,Player player,PerkData perkData){
		this.player=player;
		this.perkData=perkData;
		this.perk=perk;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancel=arg0;
	}

}
