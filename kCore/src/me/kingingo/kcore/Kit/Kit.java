package me.kingingo.kcore.Kit;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Disguise.DisguiseManager;
import me.kingingo.kcore.Disguise.DisguiseType;
import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Kit.Perks.Event.PerkHasPlayerEvent;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit extends PerkData{

	@Getter
	String Name;
	@Getter
	ItemStack item;
	@Getter
	String[] description;
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
	@Getter
	ItemStack[] items;
	
	public Kit(String Name,String[] desc,ItemStack item,ItemStack[] items,Permission permission,DisguiseType disguise,DisguiseManager dmanager,KitType type,int preis,Perk[] perks){
		this.Name=Name;
		this.type=type;
		this.items=items;
		this.permission=permission;
		this.preis=preis;
		this.disguise=disguise;
		this.dmanager=dmanager;
		int i;
		try{
		if(KitType.PREMIUM!=type&&KitType.STARTER!=type){
			i=4;
		}else{
			i=2;
		}
		
		for(Perk perk : perks){
			perk.setPerkData(this);
			getPlayers().put(perk, new ArrayList<Player>());
		}
		i=i+desc.length;
		
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
		
		for(String s : desc){
			this.description[i]=s;
			i++;
		}
			
		}catch(ArrayIndexOutOfBoundsException e){
			System.err.println("[Kit] Name: "+Name);
			e.printStackTrace();
		}
		
		this.item=UtilItem.Item(item, getDescription(), getName());
		this.perks=perks;
	}
	
	public Kit(String Name,String[] desc,ItemStack item,ItemStack[] items,Permission permission,KitType type,int preis,Perk[] perks){
		this.Name=Name;
		this.type=type;
		this.permission=permission;
		this.items=items;
		this.preis=preis;
		int i;
		try{
		if(KitType.PREMIUM!=type&&KitType.STARTER!=type){
			i=4;
		}else{
			i=2;
		}
		
		for(Perk perk : perks){
			perk.setPerkData(this);
			getPlayers().put(perk, new ArrayList<Player>());
		}
		i=i+desc.length;
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
			
		
		for(String s : desc){
			this.description[i]=s;
			i++;
		}
		
		}catch(ArrayIndexOutOfBoundsException e){
			System.err.println("[Kit] Name: "+Name);
			e.printStackTrace();
		}
		
		this.item=UtilItem.Item(item, getDescription(), getName());
		this.perks=perks;
	}
	
	public Kit(String Name,String[] desc,ItemStack item,Permission permission,DisguiseType disguise,DisguiseManager dmanager,KitType type,int preis,Perk[] perks){
		this.Name=Name;
		this.type=type;
		this.permission=permission;
		this.preis=preis;
		this.disguise=disguise;
		this.dmanager=dmanager;
		int i;
		try{
		if(KitType.PREMIUM!=type&&KitType.STARTER!=type){
			i=4;
		}else{
			i=2;
		}
		
		for(Perk perk : perks){
			perk.setPerkData(this);
			getPlayers().put(perk, new ArrayList<Player>());
		}
		i=i+desc.length;
		
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
			
		
		for(String s : desc){
				this.description[i]=s;
				i++;
		}
		
		}catch(ArrayIndexOutOfBoundsException e){
			System.err.println("[Kit] Name: "+Name);
			e.printStackTrace();
		}
		
		this.item=UtilItem.Item(item, getDescription(), getName());
		this.perks=perks;
	}
	
	public Kit(String Name,String[] desc,ItemStack item,Permission permission,KitType type,int preis,Perk[] perks){
		this.Name=Name;
		this.type=type;
		this.permission=permission;
		this.preis=preis;
		int i;
		try{
		if(KitType.PREMIUM!=type&&KitType.STARTER!=type){
			i=4;
		}else{
			i=2;
		}
		
		for(Perk perk : perks){
			perk.setPerkData(this);
			getPlayers().put(perk, new ArrayList<Player>());
		}
		i=i+desc.length;
		
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
		
		for(String s : desc){
			this.description[i]=s;
			i++;
		}
		
		}catch(ArrayIndexOutOfBoundsException e){
			System.err.println("[Kit] Name: "+Name);
			e.printStackTrace();
		}
		
		this.item=UtilItem.Item(item, getDescription(), getName());
		this.perks=perks;
	}
	
	public void disguise(HashMap<Player,String> list){
		if(getDisguise()==null)return;
		for(Perk perk : getPlayers().keySet()){
			for(Player p : getPlayers().get(perk)){
				if(!list.containsKey(p))continue;
				DisguiseBase d = DisguiseType.newDisguise(p, getDisguise(), new Object[]{list.get(p)+p.getName()});
				getDmanager().disguise(p,d);
			}
		}
	}
	
	public void StartGame(HashMap<Player,String> list){
		setItems();
		disguise(list);
	}
	
	public void StartGame(){
		setItems();
		disguise();
	}
	
	public void setItems(){
		if(items==null)return;
		for(Perk perk : getPlayers().keySet()){
			for(Player p : getPlayers().get(perk)){
				for(ItemStack i : items){
					p.getInventory().addItem(i.clone());
				}
			}
		}
	}
	
	public void disguise(){
		if(getDisguise()==null)return;
		for(Perk perk : getPlayers().keySet()){
			for(Player p : getPlayers().get(perk)){
				DisguiseBase d = DisguiseType.newDisguise(p, getDisguise(), new Object[]{p.getName()});
				getDmanager().disguise(p,d);
			}
		}
	}
	
	public void addPlayer(Player p){
		for(Perk perk : getPlayers().keySet()){
			getPlayers().get(perk).add(p);
		}
	}
	
	public void removePlayer(Player p){
		if(getDisguise()!=null){
			getDmanager().undisguise(p);
		}
		for(Perk perk : getPlayers().keySet())getPlayers().get(perk).remove(p);
	}
	
}
