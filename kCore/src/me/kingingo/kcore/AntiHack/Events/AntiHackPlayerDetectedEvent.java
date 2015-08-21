package me.kingingo.kcore.AntiHack.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.AntiHack.HackType;
import me.kingingo.kcore.AntiHack.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AntiHackPlayerDetectedEvent  extends Event implements Cancellable{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private Player player;
	@Getter
	@Setter
	private boolean cancelled=false;
	@Getter
	@Setter
	private String reason=null;
	@Getter
	@Setter
	private HackType hackType;
	@Getter
	@Setter
	private Level level;
	
	public AntiHackPlayerDetectedEvent(Player player,HackType hackType,Level level,String reason){
		this.player=player;
		this.hackType=hackType;
		this.level=level;
		this.reason=reason;
	}
	
	public AntiHackPlayerDetectedEvent(Player player,HackType hackType,Level level){
		this(player,hackType,level,null);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}
