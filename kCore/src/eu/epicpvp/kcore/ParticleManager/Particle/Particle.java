package eu.epicpvp.kcore.ParticleManager.Particle;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Util.UtilParticle;
import lombok.Getter;

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
