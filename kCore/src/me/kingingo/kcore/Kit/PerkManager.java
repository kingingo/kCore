package me.kingingo.kcore.Kit;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PerkManager extends PerkData{
	
	public JavaPlugin instance;
	
	public PerkManager(JavaPlugin instance,Perk[] perks){
		for(Perk perk: perks)getPlayers().put(perk, new ArrayList<Player>());
		this.instance=instance;
		registerPerks();
	}
	
	public void removePlayer(Player player){
		for(Perk perk : getPlayers().keySet())getPlayers().get(perk).remove(player);
	}
	
	public void addPlayer(String perkString, Player player){
		for(Perk perk : getPlayers().keySet()){
			if(perk.getName().equalsIgnoreCase(perkString))getPlayers().get(perk).remove(player);
		}
	}
	
	public void registerPerks(){
		for(Perk perk : getPlayers().keySet()){
			Bukkit.getPluginManager().registerEvents(perk, instance);
		}
	}
	
}
