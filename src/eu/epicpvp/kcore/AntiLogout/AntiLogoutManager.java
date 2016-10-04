package eu.epicpvp.kcore.AntiLogout;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;

import eu.epicpvp.datenserver.definitions.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.AntiLogout.Events.AntiLogoutAddPlayerEvent;
import eu.epicpvp.kcore.AntiLogout.Events.AntiLogoutDelPlayerEvent;
import eu.epicpvp.kcore.AntiLogout.Events.AntiLogoutQuitPlayerEvent;
import eu.epicpvp.kcore.Command.Commands.Events.PlayerHomeEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilList;
import lombok.Getter;
import lombok.Setter;

public class AntiLogoutManager extends kListener {

	@Getter
	JavaPlugin instance;
	@Getter
	AntiLogoutType typ;
	@Getter
	long time=TimeSpan.SECOND*30;
	@Getter
	HashMap<Player,Long> players = new HashMap<>();
	WorldGuardPlugin worldGuard;
	@Getter
	@Setter
	private boolean onDisable=false;
	@Getter
	@Setter
	private StatsManager stats=null;

	public AntiLogoutManager(JavaPlugin instance,AntiLogoutType typ,int sec){
		super(instance,"AntiLogoutManager");
		this.typ=typ;
		this.time=TimeSpan.SECOND*sec;
		this.instance=instance;
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	    if (plugin != null || (plugin instanceof WorldGuardPlugin)) {
	    	worldGuard=(WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	    }
	}

	@EventHandler
	public void Home(PlayerHomeEvent ev){
		if(getPlayers().containsKey(ev.getPlayer())){
			ev.setCancelled(true);
			ev.setReason(TranslationHandler.getText(ev.getPlayer(),"ANTI_LOGOUT_FIGHT"));
		}
	}

	Location loc;
	World w;
	RegionManager regionManager;
	ApplicableRegionSet set;
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()==UpdateType.SEC_2){
			for(int i = 0; i<getPlayers().size(); i++){
				if(! is(((Player)getPlayers().keySet().toArray()[i])) ){
					if(worldGuard!=null){
						loc = ((Player)getPlayers().keySet().toArray()[i]).getLocation();
						w = loc.getWorld();
						regionManager = worldGuard.getRegionManager(w);
						set = regionManager.getApplicableRegions(loc);
					    if (!set.allows(DefaultFlag.PVP)) {
					    	del(((Player)getPlayers().keySet().toArray()[i]));
					    }
					}
				}
			}
		}else if(ev.getType()==UpdateType.MIN_32){
			UtilList.CleanList(players);
		}
	}

	public void add(Player player){
		if(getPlayers().containsKey(player)){
			getPlayers().remove(player);
			getPlayers().put(player, System.currentTimeMillis());
		}else{
			getPlayers().put(player, System.currentTimeMillis());
			Bukkit.getPluginManager().callEvent(new AntiLogoutAddPlayerEvent(player,this));
			if(player.getAllowFlight()&&!player.isOp()){
				player.setAllowFlight(false);
				player.setFlying(false);
			}
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "ANTI_LOGOUT_FIGHT"));
		}
	}

	public void del(Player player){
		if(getPlayers().containsKey(player)){
			player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "ANTI_LOGOUT_FIGHT_END"));
			getPlayers().remove(player);
			Bukkit.getPluginManager().callEvent(new AntiLogoutDelPlayerEvent(player,this));
		}
	}

	public boolean is(Player player){
		if(getPlayers().containsKey(player)){
			if((getPlayers().get(player)+time) < System.currentTimeMillis()){
				del(player);
				return true;
			}
			return false;
		}
		return true;
	}

	@EventHandler
	public void Kick(PlayerKickEvent ev){
		if(!is(ev.getPlayer())&&!onDisable){
			switch(typ){
			case KILL:
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

				for(ItemStack item : ev.getPlayer().getInventory().getContents()){
					if(item!=null&&item.getType()!=Material.AIR){
						ev.getPlayer().getWorld().dropItem(ev.getPlayer().getLocation(), item);
					}
				}

				if(stats!=null){
					stats.setInt(ev.getPlayer(), stats.getInt(StatsKey.DEATHS, ev.getPlayer())+1, StatsKey.DEATHS);
				}

				ev.getPlayer().getInventory().clear();
				ev.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
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
			logMessage(ev.getPlayer().getName()+" hat sich in Kampf ausgeloggt und wurde bestraft!");
			Bukkit.getPluginManager().callEvent(new AntiLogoutQuitPlayerEvent(ev.getPlayer(),this));
		}
	}

	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		if(!is(ev.getPlayer())&&!onDisable){
			switch(typ){
			case KILL:
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

				for(ItemStack item : ev.getPlayer().getInventory().getContents()){
					if(item!=null&&item.getType()!=Material.AIR){
						ev.getPlayer().getWorld().dropItem(ev.getPlayer().getLocation(), item);
					}
				}

				if(stats!=null){
					stats.setInt(ev.getPlayer(), stats.getInt(StatsKey.DEATHS, ev.getPlayer())+1, StatsKey.DEATHS);
				}

				ev.getPlayer().getInventory().clear();
				ev.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
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
			logMessage(ev.getPlayer().getName()+" hat sich in Kampf ausgeloggt und wurde bestraft!");
			Bukkit.getPluginManager().callEvent(new AntiLogoutQuitPlayerEvent(ev.getPlayer(),this));
		}
	}

	Player v;
	Player d;
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Damge(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player&&ev.getDamager() instanceof Player&&!ev.isCancelled()){
			v = (Player)ev.getEntity();
			d = (Player)ev.getDamager();
			add(v);
			add(d);
		}
	}

}
