package eu.epicpvp.kcore.Disguise.Events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import eu.epicpvp.kcore.Disguise.DisguiseManager;
import eu.epicpvp.kcore.Disguise.disguises.DisguiseBase;
import lombok.Getter;

public class DisguiseEntityLivingEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private DisguiseManager disguiseManager;
	@Getter
	private DisguiseBase base;
	@Getter
	private LivingEntity entity;
	
	public DisguiseEntityLivingEvent(DisguiseManager disguiseManager,DisguiseBase base,LivingEntity entity){
		this.base=base;
		this.entity=entity;
		this.disguiseManager=disguiseManager;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}