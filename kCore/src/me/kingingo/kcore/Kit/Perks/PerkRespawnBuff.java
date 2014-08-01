package me.kingingo.kcore.Kit.Perks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Perk;

public class PerkRespawnBuff extends Perk{

	PotionEffect[] potion;
	
	public PerkRespawnBuff(PotionEffect[] potion) {
		super("RespawnBuff",Text.PERK_PREMIUM.getTexts());
		this.potion=potion;
	}

	@EventHandler
	public void Respawn(PlayerRespawnEvent ev){
		if(this.getKit().hasPlayer(this,ev.getPlayer())){
			for(PotionEffect e : potion){
				ev.getPlayer().addPotionEffect(e);
			}
		}
	}
	
}
