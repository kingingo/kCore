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
	private int st�rke;
	
	public PerkPotionEffect(PotionEffectType typ,int time,int st�rke) {
		super("PotionEffect");
		this.time=time;
		this.st�rke=st�rke;
		this.typ=typ;
	}
	
	@EventHandler
	public void Start(PerkStartEvent ev){
		for(Player p : getKit().getPlayers())UtilPlayer.addPotionEffect(p, typ, time*20,st�rke);
	}

}
