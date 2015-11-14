package me.kingingo.kcore.ParticleManager;

import java.util.HashMap;

import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.ParticleManager.Particle.Particle;
import me.kingingo.kcore.ParticleManager.Particle.ParticlePicture;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class ParticleManager extends kListener{

	public HashMap<String,Particle> particles;
	public HashMap<Player,String> players;
	
	public ParticleManager(JavaPlugin instance) {
		super(instance, "ParticleManager");
		this.players=new HashMap<>();
		this.particles=new HashMap<>();
	}
	
	public void addParticle(Particle particle){
		this.particles.put(particle.getName(), particle);
	}
	
	public  void removeParticle(String particle){
		this.particles.remove(particle);
	}

	public boolean addPlayer(Player player,String particle){
		if(this.particles.containsKey(particle)){
			this.players.put(player, particle);
			return true;
		}
		return false;
	}
	
	public void delPlayer(Player player){
		this.particles.remove(player);
	}
	
	Player player;
	@EventHandler
	public void ParticleUpdater(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FAST)return;
		if(this.players==null||this.particles==null)return;
		if(this.players.isEmpty()||this.particles.isEmpty())return;
		
		for(int i = 0; i<this.players.size(); i++){
			player=(Player)this.players.keySet().toArray()[i];
			if(player.isOnline()){
				if(this.particles.containsKey(this.players.get(player))){
					if(this.particles.get(this.players.get(player)) instanceof ParticlePicture){
						((ParticlePicture)this.particles.get(this.players.get(player))).display(player);
					}
				}else{
					this.particles.remove(this.players.get(player));
				}
			}else{
				this.players.remove(player);
			}
		}
	}
}
