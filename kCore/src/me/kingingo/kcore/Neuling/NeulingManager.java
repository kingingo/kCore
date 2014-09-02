package me.kingingo.kcore.Neuling;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Neuling.Events.NeulingEvent;
import me.kingingo.kcore.Neuling.Events.NeulingSchutzEndEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.TimeSpan;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NeulingManager {

	@Getter
	private long time = TimeSpan.MINUTE*20;
	@Getter
	private JavaPlugin instance;
	@Getter
	private HashMap<Player,Long> players = new HashMap<Player,Long>();
	
	public NeulingManager(JavaPlugin instance,int min){
		this.time=TimeSpan.MINUTE*min;
		this.instance=instance;
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
			if(player.isOnline())player.sendMessage(Text.PREFIX.getText()+Text.NEULING_END.getText());
		}
	}
	
	Player e;
	@EventHandler
	public void Damage(EntityDamageEvent ev){
		if(ev.getEntity() instanceof Player){
			e = (Player)ev.getEntity();
			if(getPlayers().containsKey(e)){
				ev.setCancelled(true);
			}
		}
	}
	
	Player v;
	@EventHandler
	public void DamageByEntity(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player){
			v = (Player)ev.getEntity();
			if(getPlayers().containsKey(v)){
				ev.setCancelled(true);
				if(ev.getDamager() instanceof Player){
					((Player)ev.getDamager()).sendMessage(Text.PREFIX.getText()+Text.NEULING_SCHUTZ.getText(v.getName()));
				}
			}
		}
	}
	
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		if(getPlayers().isEmpty())return;
		for(int i = 0; i<getPlayers().size(); i++){
			if( (getPlayers().get(i)+getTime()) < System.currentTimeMillis() ){
				del( ((Player)getPlayers().keySet().toArray()[i]) );
			}
		}
	}
	
}
