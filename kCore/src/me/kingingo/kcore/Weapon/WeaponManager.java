package me.kingingo.kcore.Weapon;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.Material;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class WeaponManager extends kListener{

	@Getter
	private HashMap<Integer,Bullet> bullets = new HashMap<>();
	@Getter
	private ArrayList<Weapon> weapons = new ArrayList<>();
	@Getter
	private JavaPlugin instance;
	
	public WeaponManager(JavaPlugin instance){
		super(instance,"WeaponManager");
		this.instance=instance;
	}
	
	public Weapon createWeapon(WeaponType type,Material ammo,Material weapon){
		Weapon w = new Weapon(this,type,new ItemStack(ammo),new ItemStack(weapon));
		getWeapons().add(w);
		return w;
	}
	
	Snowball d_sw;
	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev){
		if(ev.getDamager() instanceof Snowball){
			d_sw=(Snowball)ev.getDamager();
			if(getBullets().containsKey(d_sw.getEntityId())){
				ev.setDamage(getBullets().get(d_sw.getEntityId()).getWeapon().getDamage());
				getBullets().remove(d_sw.getEntityId());
			}
		}
	}
	
	double distance;
	Bullet b;
	@EventHandler
	public void Shoot(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FAST)return;
		for(int i = 0 ; i<getBullets().size(); i++){
			b=(Bullet)getBullets().get(i);
			if(b.getSnowball()==null||b.getSnowball().getShooter()==null||b.getSnowball().isDead()){
				b.remove();
				getBullets().remove(i);
			}else{
				distance=b.getSnowball().getLocation().distance(b.getStart_loc());
				if(distance>=b.getWeapon().getDistance()){
					b.remove();
					getBullets().remove(i);
				}else{
					b.getSnowball().setVelocity(b.getVector());
				}
			}
		}
	}
}
