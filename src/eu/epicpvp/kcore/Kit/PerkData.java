package eu.epicpvp.kcore.Kit;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Kit.Perks.Event.PerkHasPlayerEvent;
import lombok.Getter;

public class PerkData {
	@Getter
	public HashMap<Perk,ArrayList<Player>> players;
	
	public PerkData(){
		this.players=new HashMap<>();
	}
	
	public boolean hasPlayer(Perk perk,Player p){
		PerkHasPlayerEvent e = new PerkHasPlayerEvent(perk,p,this);
		Bukkit.getPluginManager().callEvent(e);
		if(!e.isCancelled()&&getPlayers().containsKey(perk)){
			return getPlayers().get(perk).contains(p);
		}
		return false;
	}
	
}
