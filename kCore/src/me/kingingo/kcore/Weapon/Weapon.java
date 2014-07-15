package me.kingingo.kcore.Weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilEvent;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilMath;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Weapon.Event.WeaponAddPlayerEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Weapon implements Listener{

	@Getter
	@Setter
	WeaponTyp Typ;
	
	@Getter
	@Setter
	ItemStack Ammo;
	
	@Getter
	@Setter
	ItemStack Weapon;
	
	@Getter
	@Setter
	JavaPlugin instance;
	
	@Getter
	@Setter
	String NAME = "§7Barret ";
	
	@Getter
	@Setter
	String RELOAD = " §c<-= Reload =->";
	
	@Getter
	@Setter
	String NORMAL = " §6<< SHOT | AMMO >>";
	
	@Getter
	@Setter
	int Shot=1; //PROJECTILE PRO SCHUSS
	
	@Getter
	@Setter
	double abweichung=0.1; //ABWEICHUNG
	
	@Getter
	@Setter
	long Delay=TimeSpan.SECOND*1; //SHOT DELAY SEC
	
	@Getter
	@Setter
	long ReloadDelay=TimeSpan.SECOND*3; //RELOAD DAY SEC
	
	@Getter
	@Setter
	int MaxInLauf=10;
	
	@Getter
	@Setter
	List<String> Lore;
	
	@Getter
	@Setter
	int damage = 1; //HERZEN PRO SCHUSS
	
	HashMap<Player,Long> list = new HashMap<>();
	
	ItemMeta im;
	
	public Weapon(JavaPlugin instance,WeaponTyp typ,ItemStack Ammo,ItemStack Weapon,long ReloadDelay, long Delay,int MaxInLauf,double abweichung,int Shot,int damage,String NAME){
		this.Typ=typ;
		this.Ammo=Ammo;
		this.Weapon=Weapon;
		this.Lore = Weapon.getItemMeta().getLore();
		this.instance=instance;
		this.ReloadDelay=ReloadDelay;
		this.Delay=Delay;
		this.NAME=NAME;
		this.MaxInLauf=MaxInLauf;
		this.abweichung=abweichung;
		this.Shot=Shot;
		this.damage=damage;
		update();
		Bukkit.getPluginManager().registerEvents(this, getInstance());
	}
	
	public int getAmmo(String n){
		try{
			if(!n.contains(" | "))return 0;
			if(!n.contains(">>"))return 0;
			return Integer.valueOf( n.split(" >")[0].split(" ")[1] );
		}catch(NumberFormatException e){
			return 0;
		}
	}
	
	public int getShot(String n){
		try{
			if(!n.contains(" | "))return 0;
			if(!n.contains("<<"))return 0;	
			System.out.println("M:"+n.split("< ")[1]);
			System.out.println("M1:"+n.split("< ")[1].split(" ")[0]);
			return Integer.valueOf( n.split("< ")[1].split(" ")[0] );
		}catch(NumberFormatException e){
			return 0;
		}
	}
	
	public ItemStack getWeapon(){
		return Weapon.clone();
	}
	
	public void update(){
		im = Weapon.getItemMeta();
		im.setDisplayName(getNAME()+getNORMAL());
		im.setLore(Lore);
		Weapon.setItemMeta(im);
	}
	
	public ItemStack setWeaponToNormal(ItemStack i,int ammo,int lauf){
		im = i.getItemMeta();
		String s = getNAME()+getNORMAL();
		s=s.replaceAll("SHOT", String.valueOf(lauf));
		s=s.replaceAll("AMMO", String.valueOf(ammo));
		System.out.println("NAME: "+s);
		im.setDisplayName(s);
		i.setItemMeta(im);
		return i;
	}
	
	public ItemStack setWeaponToNormal(ItemStack i,Player p){
		int ammo = UtilInv.AnzahlInInventory(p, Ammo.getTypeId());
		int lauf=0;
		if(ammo >= MaxInLauf){
			lauf=MaxInLauf;
			ammo=ammo-MaxInLauf;
		}else{
			lauf=ammo;
			ammo=0;
		}
		System.out.println("AMMO:"+ammo+" / LAUF:"+lauf);
		return setWeaponToNormal(i, ammo, lauf);
	}
	
	public void setWeaponToReload(ItemStack i){
		im = i.getItemMeta();
		im.setDisplayName(getRELOAD());
		i.setItemMeta(im);
	}
	
	@EventHandler
	public void Reload(UpdateEvent ev){
		if(UpdateType.SEC!=ev.getType())return;
		
	}
	
	public Vector Location(Location l){
		double pitch = ((l.getPitch() + 90) * Math.PI) / 180;
		double yaw  = ((l.getYaw() + 90)  * Math.PI) / 180;
		
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.sin(pitch) * Math.sin(yaw);
		double z = Math.cos(pitch);
		
		Vector vector = new Vector(x, z, y);
		return vector;
	}
	
	@EventHandler
	public void Give(WeaponAddPlayerEvent ev){
		if(ev.getWeapon()==this){
			if(ev.getPos()==-1){
				ev.getPlayer().getInventory().addItem(setWeaponToNormal(getWeapon(), ev.getPlayer()));
			}else{
				ev.getPlayer().getInventory().setItem(ev.getPos(), setWeaponToNormal(getWeapon(), ev.getPlayer()));
			}
		}
	}
	
	@EventHandler
	public void Shoot(PlayerInteractEvent ev){
		if(UtilEvent.isAction(ev, ActionType.R)&&ev.getPlayer().getItemInHand()!=null){
			if(ev.getPlayer().getItemInHand().hasItemMeta()&&ev.getPlayer().getItemInHand().getItemMeta().hasDisplayName()&&ev.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains(getNAME())){
				if(list.containsKey(ev.getPlayer()) && list.get(ev.getPlayer()) > System.currentTimeMillis()){
					return;
				}
				int s = getShot(ev.getPlayer().getItemInHand().getItemMeta().getDisplayName());
				
				if(s<=0){
					setWeaponToReload(ev.getPlayer().getItemInHand());
					return;
				}
				
				s--;
				list.put(ev.getPlayer(), System.currentTimeMillis()+Delay);
				UtilInv.remove(ev.getPlayer(), getAmmo().getType(), (byte)getAmmo().getDurability(), 1);
				int a = getAmmo(ev.getPlayer().getItemInHand().getItemMeta().getDisplayName());
				setWeaponToNormal(ev.getPlayer().getItemInHand(), a, s);
				ev.getPlayer().updateInventory();
				
				if(UtilPlayer.isZoom(ev.getPlayer())){
					Vector v = ev.getPlayer().getEyeLocation().getDirection().multiply(10);
					Snowball sw;
					for(int i = 0; i<Shot; i++){
						sw = ev.getPlayer().getWorld().spawn(ev.getPlayer().getEyeLocation(),Snowball.class);
						sw.setVelocity(v);
						sw.setShooter(ev.getPlayer());
					}
				}else{
					Snowball sw;
					int r;
					Vector loc1;
					
					for(int i = 0; i<Shot;i++){
						r = UtilMath.RandomInt(6, 0);
						loc1 = Location(ev.getPlayer().getEyeLocation());
						switch(r){
						case 1:
							loc1.setX(loc1.getX()+abweichung);
							break;
						case 2:
							loc1.setX(loc1.getX()-abweichung);
							break;
						case 3:
							loc1.setZ(loc1.getZ()+abweichung);
							break;
						case 4:
							loc1.setZ(loc1.getZ()-abweichung);
							break;
						case 5:
							loc1.setY(loc1.getY()+abweichung);
							break;
						case 6:
							loc1.setY(loc1.getY()-abweichung);
							break;
						}
						Vector v = loc1.multiply(10);
						sw = ev.getPlayer().getWorld().spawn(ev.getPlayer().getEyeLocation(),Snowball.class);
						sw.setVelocity(v);
						sw.setShooter(ev.getPlayer());
					}
				}
				
			}
		}
	}
	
}
