package me.kingingo.kcore.Kit;

import java.util.ArrayList;

import lombok.Getter;
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
	
	public Kit(JavaPlugin instance,String Name,ItemStack item,Permission permission,KitType type,int preis,Perk[] perks){
		this.Name=Name;
		this.type=type;
		this.permission=permission;
		this.preis=preis;
		int i;
		
		if(KitType.PREMIUM!=type&&KitType.STARTER!=type){
			i=4;
		}else{
			i=2;
		}
		
		for(Perk perk : perks){
			Bukkit.getPluginManager().registerEvents(perk, instance);
			perk.setKit(this);
			i=i+perk.description.length;
		}
		
		this.description=new String[i];
		this.description[0]=getType().getName();
		this.description[1]=" ";
		
		if(KitType.PREMIUM!=type&&KitType.STARTER!=type){
			this.description[2]="�ePreis: "+preis;
			this.description[3]=" ";
			i=4;
		}else{
			i=2;
		}
			
		
		for(Perk perk : perks){
			for(String s : perk.getDescription()){
				this.description[i]=s;
				i++;
			}
		}
		
		this.item=UtilItem.Item(item, getDescription(), getName());
		this.perks=perks;
	}
	
	public Kit(JavaPlugin instance,String Name,ItemStack item,Permission permission,KitType type,String[] description,Perk[] perks){
		this.Name=Name;
		this.type=type;
		this.permission=permission;
		this.description=description;
		this.item=UtilItem.Item(item, getDescription(), getName());
		for(Perk perk : perks){
			Bukkit.getPluginManager().registerEvents(perk, instance);
			perk.setKit(this);
		}
		this.perks=perks;
	}
	
	public void addPlayer(Player p){
		getPlayers().add(p);
	}
	
	public void removePlayer(Player p){
		getPlayers().remove(p);
	}
	
	public boolean hasPlayer(Player p){
		return getPlayers().contains(p);
	}
	
}
