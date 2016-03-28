package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Kit.Perks.Event.PerkPlayerAddEvent;
import eu.epicpvp.kcore.Kit.Perks.Event.PerkPlayerRemoveEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class PerkPotionEffectUnlimited extends Perk{
	
	private PotionEffectType typ;
	private int staerke;
	
	public PerkPotionEffectUnlimited(PotionEffectType typ,int staerke) {
		super("PerkPotionEffectUnlimited");
		this.staerke=staerke;
		this.typ=typ;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PerkPlayerAddEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer())){
			UtilPlayer.addPotionEffect(ev.getPlayer(), typ, 999999,staerke);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PerkPlayerRemoveEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer())){
			ev.getPlayer().removePotionEffect(typ);
		}
	}
	
	

}
