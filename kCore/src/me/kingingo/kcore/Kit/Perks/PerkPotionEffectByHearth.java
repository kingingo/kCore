package me.kingingo.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilPlayer;

public class PerkPotionEffectByHearth extends Perk{

	private PotionEffectType type;
	private int second=1;
	private int stärke=1;
	private Player p;
	
	public PerkPotionEffectByHearth(PotionEffectType type,int stärke, int second) {
		super("PotionEffectByHearth");
		this.type=type;
		this.stärke=stärke;
		this.second=second;
	}
	
	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player){
			p=(Player)ev.getEntity();
			if(!getPerkData().hasPlayer(this, p))return;
			if(UtilPlayer.getHealth( p ) < 8 && !p.hasPotionEffect(type)){
				p.addPotionEffect(new PotionEffect(type,second*20,stärke));
			}
		}
	}

}
