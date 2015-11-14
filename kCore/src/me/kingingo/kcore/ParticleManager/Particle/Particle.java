package me.kingingo.kcore.ParticleManager.Particle;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.kingingo.kcore.PacketAPI.kPacket;
import me.kingingo.kcore.Util.UtilParticle;

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
