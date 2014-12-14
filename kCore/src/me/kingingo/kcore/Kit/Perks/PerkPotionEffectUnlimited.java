package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;

public class PerkPotionEffectUnlimited extends Perk{
	
	private PotionEffectType typ;
	private int stärke;
	
	public PerkPotionEffectUnlimited(PotionEffectType typ,int stärke) {
		super("PerkPotionEffectUnlimited");
		this.stärke=stärke;
		this.typ=typ;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PlayerJoinEvent ev){
		if(!getPerkData().getPlayers().containsKey(this))return;
		UtilPlayer.addPotionEffect(ev.getPlayer(), typ, 999999,stärke);
	}

}
