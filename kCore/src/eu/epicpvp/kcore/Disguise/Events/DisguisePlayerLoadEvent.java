package eu.epicpvp.kcore.Disguise.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Disguise.DisguiseManager;
import eu.epicpvp.kcore.Disguise.DisguiseType;
import lombok.Getter;
import lombok.Setter;

public class DisguisePlayerLoadEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private DisguiseManager disguiseManager;
	@Getter
	private Player player;
	@Getter
	@Setter
	private DisguiseType type;
	@Getter
	@Setter
	private Object[] object;
	
	public DisguisePlayerLoadEvent(DisguiseManager disguiseManager,DisguiseType type,Player player){
		this.player=player;
		this.disguiseManager=disguiseManager;
		this.type=type;
		this.object=null;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}