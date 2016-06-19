package eu.epicpvp.kcore.WalkEffect.Events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

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
