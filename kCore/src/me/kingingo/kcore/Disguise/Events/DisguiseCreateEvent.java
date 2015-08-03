package me.kingingo.kcore.Disguise.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Disguise.DisguiseManager;
import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Packet.Packet;
import me.kingingo.kcore.Pet.PetManager;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DisguiseCreateEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private DisguiseManager disguiseManager;
	@Getter
	private DisguiseBase base;
	@Getter
	private LivingEntity entity;
	
	public DisguiseCreateEvent(DisguiseManager disguiseManager,DisguiseBase base,LivingEntity entity){
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