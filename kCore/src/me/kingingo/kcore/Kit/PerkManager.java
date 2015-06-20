package me.kingingo.kcore.Kit;

import java.util.ArrayList;

import lombok.Getter;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerAddEvent;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerRemoveEvent;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PerkManager extends PerkData{
	
	public UserDataConfig userData;
	@Getter
	public JavaPlugin instance;
	
	public PerkManager(JavaPlugin instance,UserDataConfig userData,Perk[] perks){
		this.instance=instance;
		this.userData=userData;
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
		setPermission("GoldenApple", kPermission.PERK_APPLE);
		setPermission("Hat", kPermission.PERK_HAT);
		setPermission("noWaterdamage", kPermission.PERK_WATER_DAMAGE);
		setPermission("ArrowPotionEffect", kPermission.PERK_ARROW_POTIONEFFECT);
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
	
	public void configPlayer(Player player){
		if(userData!=null&&userData.getConfigs().containsKey(UtilPlayer.getRealUUID(player))){
			configPlayer(player, userData.getConfig(player));
		}
	}
	
	public void configPlayer(Player player,UserDataConfig userData){
		if(userData.getConfigs().containsKey(UtilPlayer.getRealUUID(player))){
			configPlayer(player, userData.getConfig(player));
		}
	}
	
	public void configPlayer(Player player,kConfig config){
		for(Perk perk : getPlayers().keySet()){
			if(player.hasPermission(perk.getPermission().getPermissionToString())){
				if(config.isSet("perks."+perk.getName())){
					if(!config.getString("perks."+perk.getName()).equalsIgnoreCase("true")){
						getPlayers().get(perk).remove(player);
					}
				}else{
					config.set("perks."+perk.getName(), "true");
				}
			}
		}
	}
	
	public boolean hasPlayer(String perkString,Player player){
		for(Perk perk : getPlayers().keySet()){
			if(perkString.equalsIgnoreCase(perk.getName())&& (player.hasPermission( perk.getPermission().getPermissionToString())||player.hasPermission( kPermission.PERK_ALL.getPermissionToString()))){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPlayer(Player player){
		for(Perk perk : getPlayers().keySet()){
			if((player.hasPermission(perk.getPermission().getPermissionToString())||player.hasPermission( kPermission.PERK_ALL.getPermissionToString())))return true;
		}
		return false;
	}
	
	public void registerPerks(){
		for(Perk perk : getPlayers().keySet()){
			perk.setPerkData(this);
			Bukkit.getPluginManager().registerEvents(perk, instance);
		}
	}
	
}
