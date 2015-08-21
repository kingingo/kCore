package me.kingingo.kcore.Weapon;

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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Weapon implements Listener{

	@Getter
	@Setter
	private WeaponType Typ;
	
	@Getter
	@Setter
	private ItemStack Ammo;
	
	@Setter
	private ItemStack Weapon;
	
	@Getter
	@Setter
	private WeaponManager manager;
	
	@Getter
	@Setter
	private String NAME = "§7Barret ";
	
	@Getter
	@Setter
	private String RELOAD = " §c<-= Reload =->";
	
	@Getter
	@Setter
	private String NORMAL = " §6<< SHOT | AMMO >>";
	
	@Getter
	@Setter
	private int Shot=1; //PROJECTILE PRO SCHUSS
	
	@Getter
	@Setter
	private double abweichung=0.1; //ABWEICHUNG
	
	@Getter
	@Setter
	private long Delay=TimeSpan.SECOND*1; //SHOT DELAY SEC
	
	@Getter
	@Setter
	private long ReloadDelay=TimeSpan.SECOND*3; //RELOAD DAY SEC
	
	@Getter
	@Setter
	private int MaxInLauf=10;
	
	@Getter
	@Setter
	private List<String> Lore;
	
	@Getter
	@Setter
	double distance=20.0; //Schussweite
	
	@Getter
	@Setter
	private int damage = 1; //HERZEN PRO SCHUSS
	
	private HashMap<Player,Long> delay_list = new HashMap<>();
	private HashMap<Player,Long> reload_list = new HashMap<>();
	
	private ItemMeta im;
	
	public Weapon(WeaponManager manager,WeaponType typ,ItemStack Ammo,ItemStack Weapon){
		this.Typ=typ;
		this.Ammo=Ammo;
		this.Weapon=Weapon;
		this.Lore = Weapon.getItemMeta().getLore();
		this.manager=manager;
		update();
		Bukkit.getPluginManager().registerEvents(this, manager.getInstance());
	}
	
	public int getAmmo(String n){
		try{
			if(!n.contains(" | "))return 0;
			if(!n.contains(">>"))return 0;
			return Integer.valueOf( n.split(" >>")[0].split(" ")[5] );
		}catch(NumberFormatException e){
			return 0;
		}
	}
	
	public int getShot(String n){
		try{
			if(!n.contains(" | "))return 0;
			if(!n.contains("<<"))return 0;	
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
		im.setDisplayName(s);
		i.setItemMeta(im);
		return i;
	}
	
	public ItemStack setWeaponToNormal(ItemStack i,Player p){
		int ammo = UtilInv.AnzahlInInventory(p, Ammo.getTypeId(), UtilInv.GetData(Ammo) );
		int lauf=0;
		if(ammo >= MaxInLauf){
			lauf=MaxInLauf;
			ammo=ammo-MaxInLauf;
		}else{
			lauf=ammo;
			ammo=0;
		}
		return setWeaponToNormal(i, ammo, lauf);
	}
	
	public void setWeaponToReload(Player player,ItemStack i){
		im = i.getItemMeta();
		im.setDisplayName(getRELOAD());
		i.setItemMeta(im);
		reload_list.put(player, ReloadDelay+System.currentTimeMillis());
	}
	
	ItemStack search;
	@EventHandler
	public void Reload(UpdateEvent ev){
		if(UpdateType.FAST!=ev.getType())return;
		if(reload_list.isEmpty())return;
		
		for(int i = 0; i < reload_list.size(); i++){
			if(((Player)reload_list.keySet().toArray()[i]).isOnline()){
				if(((Long)reload_list.values().toArray()[i]) < System.currentTimeMillis()){
					search=UtilInv.searchInventoryItem(((Player)reload_list.keySet().toArray()[i]), getWeapon().getType(), getRELOAD());
					if(search!=null){
						setWeaponToNormal(search, ((Player)reload_list.keySet().toArray()[i]));
					}
					reload_list.remove(i);
				}
			}else{
				reload_list.remove(i);
			}
		}
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
	
	Bullet b;
	Vector v;
	int r;
	Vector loc1;
	int a;
	int s;
	@EventHandler
	public void Shoot(PlayerInteractEvent ev){
		if(UtilEvent.isAction(ev, ActionType.R)&&ev.getPlayer().getItemInHand()!=null){
			if(ev.getPlayer().getItemInHand().hasItemMeta()&&ev.getPlayer().getItemInHand().getItemMeta().hasDisplayName()&&ev.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains(getNAME())){
				if(delay_list.containsKey(ev.getPlayer()) && delay_list.get(ev.getPlayer()) > System.currentTimeMillis()){
					return;
				}
				s = getShot(ev.getPlayer().getItemInHand().getItemMeta().getDisplayName());
				
				if(s<=0){
					setWeaponToReload(ev.getPlayer(),ev.getPlayer().getItemInHand());
					return;
				}
				
				s--;
				delay_list.put(ev.getPlayer(), System.currentTimeMillis()+Delay);
				UtilInv.remove(ev.getPlayer(), getAmmo().getType(), (byte)getAmmo().getDurability(), 1);
				a = getAmmo(ev.getPlayer().getItemInHand().getItemMeta().getDisplayName());
				setWeaponToNormal(ev.getPlayer().getItemInHand(), a, s);
				ev.getPlayer().updateInventory();
				
				if(UtilPlayer.isZoom(ev.getPlayer())){
					v = ev.getPlayer().getEyeLocation().getDirection().multiply(5);
					for(int i = 0; i<Shot; i++){
						b=new Bullet(ev.getPlayer(),this,v);
						getManager().getBullets().put(b.getSnowball().getEntityId(),b);
					}
				}else{
					
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
						b=new Bullet(ev.getPlayer(),this,v);
						
						getManager().getBullets().put(b.getSnowball().getEntityId(),b);
					}
				}
				
			}
		}else if(UtilEvent.isAction(ev, ActionType.L)&&ev.getPlayer().getItemInHand()!=null){
			if(ev.getPlayer().getItemInHand().hasItemMeta()&&ev.getPlayer().getItemInHand().getItemMeta().hasDisplayName()&&ev.getPlayer().getItemInHand().getItemMeta().getDisplayName().contains(getNAME())){
				if(UtilPlayer.isZoom(ev.getPlayer())){
					ev.getPlayer().removePotionEffect(PotionEffectType.SLOW);
				}else{
					ev.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,120*20,3));
				}
			}
		}
	}
	
}
