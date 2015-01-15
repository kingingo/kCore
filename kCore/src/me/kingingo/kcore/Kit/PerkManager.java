package me.kingingo.kcore.Kit;

import java.util.ArrayList;

import lombok.Getter;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerAddEvent;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerRemoveEvent;
import me.kingingo.kcore.Permission.Permission;
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
		
		setPermission("noHunger", Permission.PERK_NO_HUNGER);
		setPermission("DoubleXP", Permission.PERK_DOUBLE_XP);
		setPermission("Dropper", Permission.PERK_DROPPER);
		setPermission("GetXP", Permission.PERK_GET_XP);
		setPermission("PotionClear", Permission.PERK_GOLENAPPLE);
		setPermission("noFiredamage", Permission.PERK_NO_FIRE);
		setPermission("HealPotion", Permission.PERK_HEALER);
		setPermission("ItemName", Permission.PERK_ITEM_NAME);
		setPermission("DoubleJump", Permission.PERK_JUMP);
		setPermission("Runner", Permission.PERK_RUNNER);
	}
	
	public void removePlayer(Player player){
		for(Perk perk : getPlayers().keySet())getPlayers().get(perk).remove(player);
		Bukkit.getPluginManager().callEvent(new PerkPlayerRemoveEvent(player));
	}
	
	public void removePlayer(String perkString,Player player){
		for(Perk perk : getPlayers().keySet()){
			if(perk.getName().equalsIgnoreCase(perkString)){
				getPlayers().get(perk).remove(player);
				break;
			}
		}
		Bukkit.getPluginManager().callEvent(new PerkPlayerRemoveEvent(player));
	}
	
	public boolean setPermission(String perkString,Permission permission){
		for(Perk perk : getPlayers().keySet()){
			if(perk.getName().equalsIgnoreCase(perkString)){
				perk.setPermission(permission);
				return true;
			}
		}
		return false;
	}
	
	public void addPlayer(String perkString, Player player){
		for(Perk perk : getPlayers().keySet()){
			if(perk.getName().equalsIgnoreCase(perkString)){
				getPlayers().get(perk).add(player);
				break;
			}
		}
		Bukkit.getPluginManager().callEvent(new PerkPlayerAddEvent(player));
	}
	
	public boolean hasPlayer(String perkString,Player player){
		for(Perk perk : getPlayers().keySet()){
			if(perkString.equalsIgnoreCase(perk.getName())&&manager.hasPermission(player, perk.getPermission())){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPlayer(Player player){
		for(Perk perk : getPlayers().keySet()){
			if(manager.hasPermission(player, perk.getPermission()))return true;
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
