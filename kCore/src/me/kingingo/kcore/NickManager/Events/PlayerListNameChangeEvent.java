package me.kingingo.kcore.NickManager.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerListNameChangeEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	@Setter
	private String nick;
	@Getter
	private Player player;
	
	public PlayerListNameChangeEvent(Player player,String nick){
		this.player=player;
		this.nick=nick;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}