package me.kingingo.kcore.Command.Commands.Events;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeleteKitEvent extends Event {
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	@Setter
	private String kit;
	@Getter
	@Setter
	private boolean exist = true;
	
	public DeleteKitEvent(Player player,String kit){
		this.player=player;
		this.kit=kit;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}
