package me.kingingo.kcore.AntiLogout;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;

import lombok.Getter;
import me.kingingo.kcore.AntiLogout.Events.AntiLogoutAddPlayerEvent;
import me.kingingo.kcore.AntiLogout.Events.AntiLogoutDelPlayerEvent;
import me.kingingo.kcore.AntiLogout.Events.AntiLogoutQuitPlayerEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.TimeSpan;

public class AntiLogoutManager implements Listener {

	@Getter
	JavaPlugin instance;
	@Getter
	AntiLogoutType typ;
	@Getter
	long time=TimeSpan.SECOND*30;
	@Getter
	HashMap<Player,Long> players = new HashMap<>();
	WorldGuardPlugin worldGuard;
	
	public AntiLogoutManager(JavaPlugin instance,AntiLogoutType typ,int sec){
		this.typ=typ;
		this.time=TimeSpan.SECOND*sec;
		this.instance=instance;
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	    if (plugin != null || (plugin instanceof WorldGuardPlugin)) {
	    	worldGuard=(WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	    }
		Bukkit.getPluginManager().registerEvents(this, getInstance());
	}
	
	Location loc;
	World w;
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC_2)return;
		for(int i = 0; i<getPlayers().size(); i++){
			if(is( ((Player)getPlayers().keySet().toArray()[i]) )){
				if(worldGuard!=null){
					loc = ((Player)getPlayers().keySet().toArray()[i]).getLocation();
					w = loc.getWorld();
					RegionManager regionManager = worldGuard.getRegionManager(w);
				    ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
				    if (!set.allows(DefaultFlag.PVP)) {
				    	del(((Player)getPlayers().keySet().toArray()[i]));
				    }
				}
			}
		}
	}
	
	public void add(Player player){
		if(getPlayers().containsKey(player))return;
		getPlayers().put(player, System.currentTimeMillis());
		Bukkit.getPluginManager().callEvent(new AntiLogoutAddPlayerEvent(player,this));
	}
	
	public void del(Player player){
		if(getPlayers().containsKey(player)){
			getPlayers().remove(player);
			Bukkit.getPluginManager().callEvent(new AntiLogoutDelPlayerEvent(player,this));
		}
	}
	
	public boolean is(Player player){
		if(getPlayers().containsKey(player)){
			if((getPlayers().get(player)+time) < System.currentTimeMillis()){
				return true;
			}
			del(player);
		}
		return false;
	}
	
	@EventHandler
	public void Kick(PlayerKickEvent ev){
		if(getPlayers().containsKey(ev.getPlayer())){
			getPlayers().remove(ev.getPlayer());
		}
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		if(is(ev.getPlayer())){
			switch(typ){
			case KILL:
				ev.getPlayer().setHealth(0);
				break;
			default:
				if(ev.getPlayer().getInventory().getHelmet()!=null){
					ev.getPlayer().getWorld().dropItem(ev.getPlayer().getLocation(), ev.getPlayer().getInventory().getHelmet());
					ev.getPlayer().getInventory().setHelmet(null);
				}
				if(ev.getPlayer().getInventory().getChestplate()!=null){
					ev.getPlayer().getWorld().dropItem(ev.getPlayer().getLocation(), ev.getPlayer().getInventory().getChestplate());
					ev.getPlayer().getInventory().setChestplate(null);
				}
				if(ev.getPlayer().getInventory().getLeggings()!=null){
					ev.getPlayer().getWorld().dropItem(ev.getPlayer().getLocation(), ev.getPlayer().getInventory().getLeggings());
					ev.getPlayer().getInventory().setLeggings(null);
				}
				if(ev.getPlayer().getInventory().getBoots()!=null){
					ev.getPlayer().getWorld().dropItem(ev.getPlayer().getLocation(), ev.getPlayer().getInventory().getBoots());
					ev.getPlayer().getInventory().setBoots(null);
				}
			}
			Bukkit.getPluginManager().callEvent(new AntiLogoutQuitPlayerEvent(ev.getPlayer(),this));
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Damge(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player&&!ev.isCancelled()){
			Player v = (Player)ev.getEntity();
			if(getPlayers().containsKey(v))getPlayers().remove(v);
			getPlayers().put(v, System.currentTimeMillis());
		}
	}
	
}
