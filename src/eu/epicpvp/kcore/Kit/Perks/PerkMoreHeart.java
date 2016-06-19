package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Kit.Perks.Event.PerkStartEvent;

public class PerkMoreHeart extends Perk{

	private int heart=20; 
	
	public PerkMoreHeart(int heart){
		super("MoreHeart");
		this.heart=heart;
	}
	
	@EventHandler
	public void Start(PerkStartEvent ev){
		if(!this.getPerkData().getPlayers().containsKey(this))return;
		for(Player p : this.getPerkData().getPlayers().get(this)){
			if(ev.getPlayers().contains(p)){
				p.setMaxHealth(heart);
				p.setHealth(heart);
			}
		}
	}
	
}
