package eu.epicpvp.kcore.Hologram.nametags.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Hologram.nametags.NameTagMessage;
import lombok.Getter;

public class HologramCreatureSendEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private NameTagMessage nts;
	@Getter
	private Player player;
	
	public HologramCreatureSendEvent(Player player, NameTagMessage m){
		this.nts=m;
		this.player=player;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}
