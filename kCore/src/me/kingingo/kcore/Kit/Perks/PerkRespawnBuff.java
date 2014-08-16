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
		super("RespawnBuff",Text.PERK_PREMIUM.getTexts());
		this.potion=potion;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Respawn(final PlayerRespawnEvent ev){
		if(this.getKit().hasPlayer(this,ev.getPlayer())){

					for(PotionEffect e : potion){
						System.out.println("TYP: "+e.getType().getName()+" DUR:"+e.getDuration()+" AMP:"+e.getAmplifier());
						ev.getPlayer().addPotionEffect(new PotionEffect(e.getType(),e.getDuration(),e.getAmplifier()));
					}
		}
	}
	
}
