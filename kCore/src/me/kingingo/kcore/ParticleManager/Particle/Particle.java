package me.kingingo.kcore.ParticleManager.Particle;

import lombok.Getter;
import me.kingingo.kcore.Util.UtilParticle;

import org.bukkit.entity.Player;

public abstract class Particle {

	@Getter
	private String name;
	@Getter
	private UtilParticle type;
	
	public Particle(String name,UtilParticle type){
		this.name=name;
		this.type=type;
	}
	
	public abstract void display(Player player);
}
