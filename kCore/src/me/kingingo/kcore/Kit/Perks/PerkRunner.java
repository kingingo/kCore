package me.kingingo.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerAddEvent;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerRemoveEvent;
import me.kingingo.kcore.Kit.Perks.Event.PerkStartEvent;

public class PerkRunner extends Perk{

	private float speed;
	
	public PerkRunner(float speed) {
		super("Runner");
		this.speed=speed;
	}
	
	@EventHandler
	public void Start(PerkStartEvent ev){
		if(!getPerkData().getPlayers().containsKey(this))return;
		for(Player p : getPerkData().getPlayers().get(this))p.setWalkSpeed(speed);
	}
	
	@EventHandler
	public void Remove(PerkPlayerRemoveEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer())){
			ev.getPlayer().setWalkSpeed(0.2F);
		}
	}
	
	@EventHandler
	public void Add(PerkPlayerAddEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer())){
			ev.getPlayer().setWalkSpeed(speed);
		}
	}

}
