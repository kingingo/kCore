package me.kingingo.kcore.WalkEffect.Events;

import org.bukkit.entity.Player;
import lombok.Getter;

public class WalkEffectEvent {
    
	@Getter
	private Player player;

    public WalkEffectEvent(Player player){
        this.player=player;
    }
}
