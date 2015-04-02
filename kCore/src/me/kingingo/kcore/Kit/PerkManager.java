package me.kingingo.kcore.Kit;

import java.util.ArrayList;

import lombok.Getter;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerAddEvent;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerRemoveEvent;
import me.kingingo.kcore.Permission.kPermission;
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
		
		setPermission("noHunger", kPermission.PERK_NO_HUNGER);
		setPermission("DoubleXP", kPermission.PERK_DOUBLE_XP);
		setPermission("Dropper", kPermission.PERK_DROPPER);
		setPermission("GetXP", kPermission.PERK_GET_XP);
		setPermission("PotionClear", kPermission.PERK_GOLENAPPLE);
		setPermission("noFiredamage", kPermission.PERK_NO_FIRE);
		setPermission("HealPotion", kPermission.PERK_HEALER);
		setPermission("ItemName", kPermission.PERK_ITEM_NAME);
		setPermission("DoubleJump", kPermission.PERK_JUMP);
		setPermission("Runner", kPermission.PERK_RUNNER);
	}
	
	public void removePlayer(Player player){
		Bukkit.getPluginManager().callEvent(new PerkPlayerRemoveEvent(player,null));
		for(Perk perk : getPlayers().keySet())getPlayers().get(perk).remove(player);
	}
	
	public void removePlayer(String perkString,Player player){
		Bukkit.getPluginManager().callEvent(new PerkPlayerRemoveEvent(player,perkString));
		for(Perk perk : getPlayers().keySet()){
			if(perk.getName().equalsIgnoreCase(perkString)){
				getPlayers().get(perk).remove(player);
				break;
			}
		}
	}
	
	public boolean setPermission(String perkString,kPermission permission){
		for(Perk perk : getPlayers().keySet()){
			if(perk.getName().equalsIgnoreCase(perkString)){
				perk.setPermission(permission);
				return true;
			}
		}
		return false;
	}
	
	public void addPlayer(Player player){
		for(Perk perk : getPlayers().keySet()){
			getPlayers().get(perk).add(player);
		}
		Bukkit.getPluginManager().callEvent(new PerkPlayerAddEvent(player,null));
	}
	
	public void addPlayer(String perkString, Player player){
		for(Perk perk : getPlayers().keySet()){
			if(perk.getName().equalsIgnoreCase(perkString)){
				getPlayers().get(perk).add(player);
				break;
			}
		}
		Bukkit.getPluginManager().callEvent(new PerkPlayerAddEvent(player,perkString));
	}
	
	public boolean hasPlayer(String perkString,Player player){
		for(Perk perk : getPlayers().keySet()){
			if(perkString.equalsIgnoreCase(perk.getName())&& (manager.hasPermission(player, perk.getPermission())||manager.hasPermission(player, kPermission.PERK_ALL))){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPlayer(Player player){
		for(Perk perk : getPlayers().keySet()){
			if((manager.hasPermission(player, perk.getPermission())||manager.hasPermission(player, kPermission.PERK_ALL)))return true;
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
