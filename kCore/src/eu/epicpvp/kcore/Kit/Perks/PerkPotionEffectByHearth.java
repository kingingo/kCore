package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class PerkPotionEffectByHearth extends Perk{

	private PotionEffectType type;
	private int second=1;
	private int staerke=1;
	private Player p;
	
	public PerkPotionEffectByHearth(PotionEffectType type,int staerke, int second) {
		super("PotionEffectByHearth");
		this.type=type;
		this.staerke=staerke;
		this.second=second;
	}
	
	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player){
			p=(Player)ev.getEntity();
			if(!getPerkData().hasPlayer(this, p))return;
			if(UtilPlayer.getHealth( p ) < 8 && !p.hasPotionEffect(type)){
				p.addPotionEffect(new PotionEffect(type,second*20,staerke));
			}
		}
	}

}
