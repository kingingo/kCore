package me.kingingo.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkStartEvent;
import me.kingingo.kcore.Util.UtilPlayer;

public class PerkPotionEffect extends Perk{

	private int time;
	private PotionEffectType typ;
	private int stärke;
	
	public PerkPotionEffect(PotionEffectType typ,int time,int stärke) {
		super("PotionEffect");
		this.time=time;
		this.stärke=stärke;
		this.typ=typ;
	}
	
	@EventHandler
	public void Start(PerkStartEvent ev){
		for(Player p : getKit().getPlayers())UtilPlayer.addPotionEffect(p, typ, time*20,stärke);
	}

}
