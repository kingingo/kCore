package me.kingingo.kcore.Weapon;

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class Bullet {

	@Getter
	@Setter
	private Weapon weapon;
	@Getter
	@Setter
	private Snowball snowball;
	@Getter
	@Setter
	private Location start_loc;
	@Getter
	@Setter
	private Vector vector;
	
	public Bullet(Player player,Weapon weapon,Vector vector){
		this.snowball=player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
		this.snowball.setShooter(player);
		this.snowball.setVelocity(vector);
		this.weapon=weapon;
		this.start_loc=player.getEyeLocation();
		this.vector=vector;
	}
	
	public void remove(){
		weapon=null;
		snowball=null;
		start_loc=null;
		vector=null;
	}
	
}
