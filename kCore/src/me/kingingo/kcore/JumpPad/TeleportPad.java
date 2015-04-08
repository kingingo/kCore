package me.kingingo.kcore.JumpPad;

import me.kingingo.kcore.Util.UtilEffect;
import me.kingingo.kcore.Util.UtilParticle;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportPad extends JumpPad{
	
	public TeleportPad(Location from, Location to) {
		super(from, to);
		to.add(0, 1, 0);
	}

	public void doit(Player player,int i){
		player.teleport(super.to);
	}
	
}
