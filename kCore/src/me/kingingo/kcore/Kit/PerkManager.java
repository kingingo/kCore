package me.kingingo.kcore.Kit;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.Hologram.nametags.NameTagType;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryCopy;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerAddEvent;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerRemoveEvent;
import me.kingingo.kcore.Listener.EntityClick.EntityClickListener;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.Util.UtilEnt;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PerkManager extends PerkData{
	
	@Getter
	public JavaPlugin instance;
	@Getter
	@Setter
	private InventoryPageBase page;
	@Getter
	private Entity entity;
	
	public PerkManager(JavaPlugin instance,Perk[] perks){
		UtilServer.setPerkManager(this);
		this.instance=instance;
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
	
	public void setPerkEntity(Location location){
		entity = location.getWorld().spawnEntity(location, EntityType.WITCH);
		UtilEnt.setNoAI(entity, true);
		UtilEnt.setSilent(entity, true);
		
		NameTagMessage m = new NameTagMessage(NameTagType.SERVER, entity.getLocation().add(0, 2.1, 0), "§c§lPerks");
		m.send();
		
		 new EntityClickListener(getInstance(), new Click() {
			
			@Override
			public void onClick(Player player, ActionType type, Object object) {
				((InventoryCopy)getPage()).open(player, UtilInv.getBase());
			}
		}, entity);
	}
	
	public void removePlayer(Player player){
		Bukkit.getPluginManager().callEvent(new PerkPlayerRemoveEvent(player,null));
		for(Perk perk : getPlayers().keySet())getPlayers().get(perk).remove(player);
	}
	
	public void removePlayer(Perk perk,Player player){
		Bukkit.getPluginManager().callEvent(new PerkPlayerRemoveEvent(player,perk.getName()));
		getPlayers().get(perk).remove(player);
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
	
	public void addPlayer(Perk perk, Player player){
		getPlayers().get(perk).add(player);
		Bukkit.getPluginManager().callEvent(new PerkPlayerAddEvent(player,perk.getName()));
		
	}
	
	public void addPlayer(String perkString, Player player){
		for(Perk perk : getPlayers().keySet()){
			if(perk.getName().equalsIgnoreCase(perkString)){
				addPlayer(perk, player);
				break;
			}
		}
	}
	
	public void configPlayer(Player player){
		if(UtilServer.getUserData().getConfigs().containsKey(UtilPlayer.getRealUUID(player))){
			configPlayer(player, UtilServer.getUserData().getConfig(player));
		}
	}
	
	public void configPlayer(Player player,kConfig config){
		for(Perk perk : getPlayers().keySet()){
			if(config.isSet("perks."+perk.getName())){
				if(config.getString("perks."+perk.getName()).equalsIgnoreCase("true")){
					getPlayers().get(perk).add(player);
				}else{
					getPlayers().get(perk).remove(player);
				}
			}else{
				config.set("perks."+perk.getName(), "false");
				config.save();
			}
		}

		Bukkit.getPluginManager().callEvent(new PerkPlayerAddEvent(player,null));
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
