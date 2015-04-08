package me.kingingo.kcore.Command.Commands.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerMsgSendEvent  extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	@Setter
	private Player target;
	@Getter
	private String message;
	@Getter
	private boolean b;
	
	public PlayerMsgSendEvent(Player player,Player target,String message,boolean b){
		this.player=player;
		this.target=target;
		this.message=message;
		this.b=b;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}
