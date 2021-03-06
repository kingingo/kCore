package eu.epicpvp.kcore.Kit;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Disguise.DisguiseManager;
import eu.epicpvp.kcore.Disguise.DisguiseType;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilItem;
import lombok.Getter;

public class Kit extends PerkData{

	@Getter
	private String name;
	@Getter
	private ItemStack item;
	@Getter
	private String[] description;
	@Getter
	private Perk[] perks;
	@Getter
	private KitType type;
	@Getter
	private int coins_preis;
	@Getter
	private int gems_preis;
	@Getter
	private PermissionType permission;
	@Getter
	private DisguiseType disguise=null;
	@Getter
	private DisguiseManager dmanager;
	@Getter
	private ItemStack[] items;
	
	public Kit(String name,String[] desc,ItemStack item,ItemStack[] items,PermissionType permission,DisguiseType disguise,DisguiseManager dmanager,KitType type,int coins_preis,int gems_preis,Perk[] perks){
		this.name=name;
		this.type=type;
		this.items=items;
		this.permission=permission;
		this.coins_preis=coins_preis;
		this.gems_preis=gems_preis;
		this.disguise=disguise;
		this.dmanager=dmanager;
		int i;
		try{
		if(KitType.KAUFEN==type||KitType.KAUFEN_COINS==type||KitType.KAUFEN_GEMS==type){
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
		i=1;
		
		if(KitType.KAUFEN==type||KitType.KAUFEN_COINS==type||KitType.KAUFEN_GEMS==type){
			if(coins_preis>0){
				this.description[i]="§eCoins: "+coins_preis;
				i++;
			}
			if(gems_preis>0){
				this.description[i]="§aGems: "+gems_preis;
				i++;
			}
			this.description[i]=" ";
			i++;
		}else{
			this.description[1]=" ";
			i=2;
		}
		
		for(String s : desc){
			this.description[i]=s;
			i++;
		}
			
		}catch(ArrayIndexOutOfBoundsException e){
			System.err.println("[Kit] Name: "+name);
			e.printStackTrace();
		}
		
		this.item=UtilItem.Item(item, getDescription(), getName());
		this.perks=perks;
	}
	
	public Kit(String Name,String[] desc,ItemStack item,ItemStack[] items,PermissionType permission,KitType type,int coins_preis ,Perk[] perks){
		this(Name,desc,item,items,permission,null,null,type,coins_preis,0,perks);
	}
	
	public Kit(String Name,String[] desc,ItemStack item,PermissionType permission,DisguiseType disguise,DisguiseManager dmanager,KitType type,int coins_preis,Perk[] perks){
		this(Name,desc,item,null,permission,disguise,dmanager,type,coins_preis,0,perks);
	}
	
	public Kit(String Name,String[] desc,ItemStack item,PermissionType permission,KitType type,int coins_preis ,Perk[] perks){
		this(Name,desc,item,null,permission,null,null,type,coins_preis,0,perks);
	}
	
	public Kit(String Name,String[] desc,ItemStack item,ItemStack[] items,PermissionType permission,KitType type,int coins_preis,int gems_preis,Perk[] perks){
		this(Name,desc,item,items,permission,null,null,type,coins_preis,gems_preis,perks);
	}
	
	public Kit(String Name,String[] desc,ItemStack item,PermissionType permission,DisguiseType disguise,DisguiseManager dmanager,KitType type,int coins_preis,int gems_preis,Perk[] perks){
		this(Name,desc,item,null,permission,disguise,dmanager,type,coins_preis,gems_preis,perks);
	}
	
	public Kit(String Name,String[] desc,ItemStack item,PermissionType permission,KitType type,int coins_preis,int gems_preis,Perk[] perks){
		this(Name,desc,item,null,permission,null,null,type,coins_preis,gems_preis,perks);
	}
	
	public void disguise(HashMap<Player,String> list){
		if(getDisguise()==null)return;
		for(Perk perk : getPlayers().keySet()){
			for(Player p : getPlayers().get(perk)){
				if(!list.containsKey(p))continue;
				getDmanager().disguise(p,getDisguise(),new Object[]{list.get(p)+p.getName()});
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
				getDmanager().disguise(p,getDisguise(),new Object[]{p.getName()});
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
