package me.kingingo.kcore.ParticleManager.Particle;

import me.kingingo.kcore.Util.UtilParticle;

import org.bukkit.entity.Player;

public class ParticleSpray extends Particle{

	public ParticleSpray(String name, UtilParticle type) {
		super(name, type);
	}

	@Override
	public void display(Player player) {
		getType().display(25, 25, 25, 0, 25, player.getLocation(), 100);
	}

}
