package me.kingingo.kcore.Weapon;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.kListener;
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
	private HashMap<Integer,Weapon> bullets = new HashMap<>();
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
				ev.setDamage(getBullets().get(d_sw.getEntityId()).getDamage());
				getBullets().remove(d_sw.getEntityId());
			}
		}
	}
	
	@EventHandler
	public void Shoot(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_64)return;
		
	}
	
	@EventHandler
	public void BulletsClearer(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_64)return;
		bullets.clear();
	}
	
}
