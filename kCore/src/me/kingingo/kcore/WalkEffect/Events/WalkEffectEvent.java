package me.kingingo.kcore.WalkEffect.Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import lombok.Getter;

public class WalkEffectEvent {
    
	@Getter
	private Entity player;

    public WalkEffectEvent(Entity player){
        this.player=player;
    }
}
