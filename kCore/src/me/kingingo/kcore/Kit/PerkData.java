package me.kingingo.kcore.Kit;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Kit.Perks.Event.PerkHasPlayerEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
