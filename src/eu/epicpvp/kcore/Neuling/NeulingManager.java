package eu.epicpvp.kcore.Neuling;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Neuling.Events.NeulingEvent;
import eu.epicpvp.kcore.Neuling.Events.NeulingSchutzEndEvent;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.TimeSpan;
import lombok.Getter;

public class NeulingManager extends kListener{

	@Getter
	private long time = TimeSpan.MINUTE*20;
	@Getter
	private JavaPlugin instance;
	@Getter
	private HashMap<Player,Long> players = new HashMap<Player,Long>();
	
	public NeulingManager(JavaPlugin instance,CommandHandler cmd,int min){
		super(instance,"[NeulingManager]");
		this.time=TimeSpan.MINUTE*min;
		this.instance=instance;
		cmd.register(CommandNeuling.class, new CommandNeuling(this));
	}
	
	public void add(Player player){
		if(getPlayers().containsKey(player))return;
		getPlayers().put(player, System.currentTimeMillis());
		Bukkit.getPluginManager().callEvent(new NeulingEvent(player,this));
	}
	
	public void del(Player player){
		if(getPlayers().containsKey(player)){
			getPlayers().remove(player);
			Bukkit.getPluginManager().callEvent(new NeulingSchutzEndEvent(player,this));
			if(player.isOnline())player.sendMessage(TranslationHandler.getText(player, "PREFIX")+TranslationHandler.getText(player, "NEULING_END"));
		}
	}
	
	Player e;
	@EventHandler
	public void Damage(EntityDamageEvent ev){
		if(getPlayers().isEmpty())return;
		if(ev.getEntity() instanceof Player){
			e = (Player)ev.getEntity();
			if(getPlayers().containsKey(e)){
				ev.setCancelled(true);
			}
		}
	}
	
	Player v;
	Player a;
	@EventHandler(priority=EventPriority.HIGH)
	public void DamageByEntity(EntityDamageByEntityEvent ev){
		if(getPlayers().isEmpty())return;
		if(ev.getEntity() instanceof Player){
			v = (Player)ev.getEntity();
			
			if(getPlayers().containsKey(v)){
				ev.setCancelled(true);
				if(ev.getDamager() instanceof Player){
					((Player)ev.getDamager()).sendMessage(TranslationHandler.getText(((Player)ev.getDamager()), "PREFIX")+TranslationHandler.getText(((Player)ev.getDamager()), "NEULING_SCHUTZ", v.getName()));
				}
			}
			
			if(ev.getDamager() instanceof Player){
				a=(Player)ev.getDamager();
				if(getPlayers().containsKey(a)){
					ev.setCancelled(true);
					a.sendMessage(TranslationHandler.getText(a, "PREFIX")+TranslationHandler.getText(a, "NEULING_SCHUTZ_YOU"));
				}
			}else if(ev.getDamager() instanceof Projectile){
				if(((Projectile)ev.getDamager()).getShooter() instanceof Player){
					a=(Player)((Projectile)ev.getDamager()).getShooter();
					if(getPlayers().containsKey(a)){
						ev.setCancelled(true);
						a.sendMessage(TranslationHandler.getText(a, "PREFIX")+TranslationHandler.getText(a, "NEULING_SCHUTZ_YOU"));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void Pickup(PlayerPickupItemEvent ev){
		if(getPlayers().containsKey(ev.getPlayer())){
			ev.getPlayer().sendMessage(TranslationHandler.getText(ev.getPlayer(), "PREFIX")+TranslationHandler.getText(ev.getPlayer(), "NEULING_SCHUTZ_YOU"));
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void Drop(PlayerDropItemEvent ev){
		if(getPlayers().containsKey(ev.getPlayer())){
			ev.getPlayer().sendMessage(TranslationHandler.getText(ev.getPlayer(), "PREFIX")+TranslationHandler.getText(ev.getPlayer(), "NEULING_SCHUTZ_YOU"));
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		if(getPlayers().isEmpty())return;
		for(int i = 0; i<getPlayers().size(); i++){
			if( (getPlayers().get(((Player)getPlayers().keySet().toArray()[i]))+getTime()) < System.currentTimeMillis() ){
				del( ((Player)getPlayers().keySet().toArray()[i]) );
			}
		}
	}
	
}
