package me.kingingo.kcore.Nick.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSendMessageEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	@Setter
	private String message;
	@Getter
	private Player player;
	
	public PlayerSendMessageEvent(Player player,String message){
		this.player=player;
		this.message=message;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}