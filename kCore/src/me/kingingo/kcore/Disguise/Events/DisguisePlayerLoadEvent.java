package me.kingingo.kcore.Disguise.Events;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Disguise.DisguiseManager;
import me.kingingo.kcore.Disguise.DisguiseType;
import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Packet.Packet;
import me.kingingo.kcore.Pet.PetManager;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DisguisePlayerLoadEvent extends Event{
	private static HandlerList handlers = new HandlerList();
	@Getter
	private DisguiseManager disguiseManager;
	@Getter
	private Player player;
	@Getter
	@Setter
	private DisguiseType type;
	
	public DisguisePlayerLoadEvent(DisguiseManager disguiseManager,DisguiseType type,Player player){
		this.player=player;
		this.disguiseManager=disguiseManager;
		this.type=type;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

}