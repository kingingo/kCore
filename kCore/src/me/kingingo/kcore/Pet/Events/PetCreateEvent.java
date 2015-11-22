package me.kingingo.kcore.Pet.Events;

import lombok.Getter;
import me.kingingo.kcore.Pet.PetManager;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetCreateEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private PetManager petManager;
	@Getter
	private LivingEntity pet;
	@Getter
	private Player player;
	
	public PetCreateEvent(PetManager petManager,LivingEntity pet,Player player){
		this.pet=pet;
		this.player=player;
		this.petManager=petManager;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}