package me.kingingo.kcore.Kit;

import java.util.ArrayList;

import lombok.Getter;
import me.kingingo.kcore.Permission.PermissionManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PerkManager extends PerkData{
	
	@Getter
	public PermissionManager manager;
	
	public PerkManager(PermissionManager manager,Perk[] perks){
		this.manager=manager;
		for(Perk perk: perks)getPlayers().put(perk, new ArrayList<Player>());
		registerPerks();
	}
	
	public void removePlayer(Player player){
		for(Perk perk : getPlayers().keySet())getPlayers().get(perk).remove(player);
	}
	
	public void removePlayer(String perkString,Player player){
		for(Perk perk : getPlayers().keySet()){
			if(perk.getName().equalsIgnoreCase(perkString)){
				getPlayers().get(perk).remove(player);
				break;
			}
		}
	}
	
	public void addPlayer(String perkString, Player player){
		for(Perk perk : getPlayers().keySet()){
			if(perk.getName().equalsIgnoreCase(perkString)){
				getPlayers().get(perk).add(player);
				break;
			}
		}
	}
	
	public boolean hasPlayer(Player player){
		for(Perk perk : getPlayers().keySet()){
			if(getPlayers().get(perk).contains(player))return true;
		}
		return false;
	}
	
	public void registerPerks(){
		for(Perk perk : getPlayers().keySet()){
			perk.setPerkData(this);
			Bukkit.getPluginManager().registerEvents(perk, manager.getInstance());
		}
	}
	
}
