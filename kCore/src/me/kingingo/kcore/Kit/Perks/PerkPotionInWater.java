package me.kingingo.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

public class PerkPotionInWater extends Perk{

	private PotionEffectType type;
	private int time;
	private int stärke;
	
	public PerkPotionInWater(PotionEffectType type,int time,int stärke) {
		super("RegnerationInWater");
		this.type=type;
		this.time=time;
		this.stärke=stärke;
	}

	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		for(Player p : getKit().getPlayers()){
			if(p.getLocation().getBlock().getType().toString().contains("WATER")&&!p.hasPotionEffect(type)){
				p.addPotionEffect(new PotionEffect(type,20*time,stärke));
			}
		}
	}
	
}
