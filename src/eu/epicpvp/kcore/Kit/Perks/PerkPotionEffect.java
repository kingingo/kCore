package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Kit.Perks.Event.PerkStartEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class PerkPotionEffect extends Perk{

	private int time;
	private PotionEffectType typ;
	private int staerke;
	
	public PerkPotionEffect(PotionEffectType typ,int time,int staerke) {
		super("PotionEffect");
		this.time=time;
		this.staerke=staerke;
		this.typ=typ;
	}
	
	@EventHandler
	public void Start(PerkStartEvent ev){
		if(!getPerkData().getPlayers().containsKey(this))return;
		for(Player p : getPerkData().getPlayers().get(this)){
			if(ev.getPlayers().contains(p)){
				UtilPlayer.addPotionEffect(p, typ, time*20,staerke);
			}
		}
	}

}
