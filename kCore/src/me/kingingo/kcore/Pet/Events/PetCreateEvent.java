package me.kingingo.kcore.Pet.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;
import me.kingingo.kcore.Pet.PetManager;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetCreateEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private PetManager petManager;
	@Getter
	private Creature pet;
	@Getter
	private Player player;
	
	public PetCreateEvent(PetManager petManager,Creature pet,Player player){
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