package me.kingingo.kcore.WalkEffect.Events;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WalkEffectEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	@Setter
	@Getter
	private Entity player;

    public WalkEffectEvent(Entity player){
        this.player=player;
    }

    public HandlerList getHandlers()
    {
      return handlers;
    }

    public static HandlerList getHandlerList()
    {
      return handlers;
    }
}
