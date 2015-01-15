package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerAddEvent;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerRemoveEvent;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffectType;

public class PerkDoubleJump extends Perk{
	
	private PotionEffectType typ;
	private int st�rke;
	
	public PerkDoubleJump() {
		super("DoubleJump");
		this.st�rke=2;
		this.typ=PotionEffectType.JUMP;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PerkPlayerAddEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer())){
			UtilPlayer.addPotionEffect(ev.getPlayer(), typ, 999999,st�rke);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PerkPlayerRemoveEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer())){
			ev.getPlayer().removePotionEffect(typ);
		}
	}
	
	

}
