package me.kingingo.kcore.ParticleManager.Particle;

import org.bukkit.entity.Player;

import me.kingingo.kcore.Util.UtilParticle;

public class ParticleSpray extends Particle{

	public ParticleSpray(String name, UtilParticle type) {
		super(name, type);
	}

	@Override
	public void display(Player player) {
		getType().display(25, 25, 25, 0, 25, player.getLocation(), 100);
	}

}
