package me.kingingo.kcore.Kit;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Disguise.DisguiseManager;
import me.kingingo.kcore.Disguise.DisguiseType;
import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Kit.Perks.Event.KitHasPlayerEvent;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Kit {

	@Getter
	String Name;
	@Getter
	ItemStack item;
	@Getter
	String[] description;
	@Getter
	ArrayList<Player> players = new ArrayList<>();
	@Getter
	Perk[] perks;
	@Getter
	KitType type;
	@Getter
	int preis;
	@Getter
	Permission permission;
	@Getter
	DisguiseType disguise=null;
	@Getter
	DisguiseManager dmanager;
	
	public Kit(String Name,ItemStack item,Permission permission,DisguiseType disguise,DisguiseManager dmanager,KitType type,int preis,Perk[] perks){
		this.Name=Name;
		this.type=type;
		this.permission=permission;
		this.preis=preis;
		this.disguise=disguise;
		this.dmanager=dmanager;
		int i;
		
		if(KitType.PREMIUM!=type&&KitType.STARTER!=type){
			i=4;
		}else{
			i=2;
		}
		
		for(Perk perk : perks){
			perk.setKit(this);
			i=i+perk.description.length;
		}
		
		this.description=new String[i];
		this.description[0]=getType().getName();
		this.description[1]=" ";
		
		if(KitType.PREMIUM!=type&&KitType.STARTER!=type){
			this.description[1]="§ePreis: "+preis;
			this.description[2]=" ";
			i=3;
		}else{
			i=2;
		}
			
		
		for(Perk perk : perks){
			for(String s : perk.getDescription()){
				this.description[i]=s;
				i++;
			}
		}
		
		this.item=UtilItem.addEnchantmentGlow( UtilItem.Item(item, getDescription(), getName()) );
		this.perks=perks;
	}
	
	public Kit(String Name,ItemStack item,Permission permission,KitType type,int preis,Perk[] perks){
		this.Name=Name;
		this.type=type;
		this.permission=permission;
		this.preis=preis;
		int i;
		
		if(KitType.PREMIUM!=type&&KitType.STARTER!=type&&KitType.ADMIN!=type){
			i=4;
		}else{
			i=2;
		}
		
		for(Perk perk : perks){
			perk.setKit(this);
			i=i+perk.description.length;
		}
		
		this.description=new String[i];
		this.description[0]=getType().getName();
		this.description[1]=" ";
		
		if(KitType.PREMIUM!=type&&KitType.STARTER!=type){
			this.description[1]="§ePreis: "+preis;
			this.description[2]=" ";
			i=3;
		}else{
			i=2;
		}
			
		
		for(Perk perk : perks){
			for(String s : perk.getDescription()){
				this.description[i]=s;
				i++;
			}
		}
		
		this.item=UtilItem.addEnchantmentGlow( UtilItem.Item(item, getDescription(), getName()) );
		this.perks=perks;
	}
	
	public void disguise(HashMap<Player,String> list){
		if(getDisguise()==null)return;
		for(Player p : getPlayers()){
				if(!list.containsKey(p))continue;
				DisguiseBase d = DisguiseType.newDisguise(p, getDisguise(), new Object[]{list.get(p)+p.getName()});
				getDmanager().disguise(d);
		}
	}
	
	public void disguise(){
		if(getDisguise()==null)return;
		for(Player p : getPlayers()){
				DisguiseBase d = DisguiseType.newDisguise(p, getDisguise(), new Object[]{p.getName()});
				getDmanager().disguise(d);
		}
	}
	
	public void addPlayer(Player p){
		getPlayers().add(p);
	}
	
	public void removePlayer(Player p){
		if(getDisguise()!=null){
			getDmanager().undisguise(p);
		}
		getPlayers().remove(p);
	}
	
	public boolean hasPlayer(Perk perk,Player p){
		KitHasPlayerEvent e = new KitHasPlayerEvent(perk,p,this);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled())return false;
		return getPlayers().contains(p);
	}
	
}
