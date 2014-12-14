package me.kingingo.kcore.Kit.Perks;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Perk;

public class PerkRespawnBuff extends Perk{

	PotionEffect[] potion;
	
	public PerkRespawnBuff(PotionEffect[] potion) {
		super("RespawnBuff");
		this.potion=potion;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Respawn(final PlayerRespawnEvent ev){
		if(this.getPerkData().hasPlayer(this,ev.getPlayer())){
			for(PotionEffect e : potion){
				ev.getPlayer().addPotionEffect(new PotionEffect(e.getType(),e.getDuration(),e.getAmplifier()));
			}
		}
	}
	
}
