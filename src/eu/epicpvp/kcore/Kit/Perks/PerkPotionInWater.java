package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;

public class PerkPotionInWater extends Perk{

	private PotionEffectType type;
	private int time;
	private int staerke;
	
	public PerkPotionInWater(PotionEffectType type,int time,int staerke) {
		super("RegnerationInWater");
		this.type=type;
		this.time=time;
		this.staerke=staerke;
	}

	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		if(!getPerkData().getPlayers().containsKey(this))return;
		for(Player p : getPerkData().getPlayers().get(this)){
			if(p.getLocation().getBlock().getType().toString().contains("WATER")&&!p.hasPotionEffect(type)){
				p.addPotionEffect(new PotionEffect(type,20*time,staerke));
			}
		}
	}
	
}
