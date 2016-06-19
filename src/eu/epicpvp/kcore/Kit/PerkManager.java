package eu.epicpvp.kcore.Kit;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Hologram.nametags.NameTagMessage;
import eu.epicpvp.kcore.Hologram.nametags.NameTagType;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Kit.Perks.Event.PerkPlayerAddEvent;
import eu.epicpvp.kcore.Kit.Perks.Event.PerkPlayerRemoveEvent;
import eu.epicpvp.kcore.Listener.EntityClick.EntityClickListener;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilEnt;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;
import lombok.Setter;

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
		
		setPermission("noHunger", PermissionType.PERK_NO_HUNGER);
		setPermission("DoubleXP", PermissionType.PERK_DOUBLE_XP);
		setPermission("Dropper", PermissionType.PERK_DROPPER);
		setPermission("GetXP", PermissionType.PERK_GET_XP);
		setPermission("PotionClear", PermissionType.PERK_GOLENAPPLE);
		setPermission("noFiredamage", PermissionType.PERK_NO_FIRE);
		setPermission("HealPotion", PermissionType.PERK_HEALER);
		setPermission("ItemName", PermissionType.PERK_ITEM_NAME);
		setPermission("DoubleJump", PermissionType.PERK_JUMP);
		setPermission("Runner", PermissionType.PERK_RUNNER);
		setPermission("GoldenApple", PermissionType.PERK_APPLE);
		setPermission("Hat", PermissionType.PERK_HAT);
		setPermission("noWaterdamage", PermissionType.PERK_WATER_DAMAGE);
		setPermission("ArrowPotionEffect", PermissionType.PERK_ARROW_POTIONEFFECT);
		setPermission("NoPotion", PermissionType.PERK_NO_POTION);
		setPermission("PerkStrength", PermissionType.PERK_UNLIMITED_STRENGTH);
		setPermission("KeepXP", PermissionType.PERK_KEEP_XP);
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
		Bukkit.getPluginManager().callEvent(new PerkPlayerRemoveEvent(player,perk));
		getPlayers().get(perk).remove(player);
	}
	
//	public void removePlayer(String perkString,Player player){
//		Bukkit.getPluginManager().callEvent(new PerkPlayerRemoveEvent(player,perkString));
//		for(Perk perk : getPlayers().keySet()){
//			if(perk.getName().equalsIgnoreCase(perkString)){
//				getPlayers().get(perk).remove(player);
//				break;
//			}
//		}
//	}
	
	public boolean setPermission(String perkString,PermissionType permission){
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
		Bukkit.getPluginManager().callEvent(new PerkPlayerAddEvent(player,perk));
		
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
		if(UtilServer.getUserData().contains(player)){
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
			if(perkString.equalsIgnoreCase(perk.getName())&& (player.hasPermission( perk.getPermission().getPermissionToString())||player.hasPermission( PermissionType.PERK_ALL.getPermissionToString()))){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPlayer(Player player){
		for(Perk perk : getPlayers().keySet()){
			if((player.hasPermission(perk.getPermission().getPermissionToString())||player.hasPermission( PermissionType.PERK_ALL.getPermissionToString())))return true;
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
