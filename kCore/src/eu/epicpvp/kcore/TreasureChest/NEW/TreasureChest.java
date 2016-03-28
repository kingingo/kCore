package eu.epicpvp.kcore.TreasureChest.NEW;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.TreasureChest.NEW.Templates.Building;
import eu.epicpvp.kcore.TreasureChest.NEW.TreasureItems.TreasureItem;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilFile;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class TreasureChest extends kListener{
	
	private ItemStack item;
	private int amount;
	private String name;
	@Getter
	private kConfig config;
	private ArrayList<TreasureItem> items;
	private HashMap<Player,TreasureChestSession> sessions;
	private Building building;
	
	public TreasureChest(JavaPlugin instance, String configName){
		this(instance,new File(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"chests"+File.separator+configName+".yml"));
	}
	
	public TreasureChest(JavaPlugin instance, File configFile){
		this(instance,new kConfig(configFile));
	}
	
	public TreasureChest(JavaPlugin instance,kConfig config) {
		super(instance, "TreasureChest:"+config.getName());
		this.config=config;
		this.name=config.getName();
		this.sessions=new HashMap<>();
		this.items=new ArrayList<>();
		this.item=this.config.getItemStack("TreasureChest.item");
		this.amount=this.config.getInt("TreasureChest.amount");
		loadTemplate(new File(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"templates"+File.separator+this.config.getString("TreasureChest.template")+".dat"));
		loadItems();
	}
	
	public TreasureChest(JavaPlugin instance,String template,ItemStack item,String name) {
		super(instance, "TreasureChest:"+name);
		new File(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"chests").mkdirs();
		this.config=new kConfig(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"chests"+File.separator+name+".yml");
		this.item=item;
		this.name=name;
		this.items=new ArrayList<>();
		this.sessions=new HashMap<>();
		setData(this.item,template);
		loadTemplate(new File(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"templates"+File.separator+template+".dat"));
		loadItems();
	}
	
	public void start(Player player){
		this.sessions.put(player, new TreasureChestSession(player, building,randomItems()));
	}
	
	public void loadTemplate(File file){
		building=new Building(file);
	}
	
	public void setData(ItemStack item,String template){
		this.config.setItemStack("TreasureChest.item", item);
		this.config.set("TreasureChest.amount", 0);
		this.config.set("TreasureChest.template", template);
		this.config.save();
	}
	
	public TreasureItem[] randomItems(){
		TreasureItem[] its = new TreasureItem[4];
		ArrayList<TreasureItem> list = new ArrayList<>();
		
		for(TreasureItem item : items){
			if(item.getAmount() == 0){
				list.add(item);
			}else{
				item.setAmount(item.getAmount()+1);
			}
		}
		
		for(int i = 0; i<4 ; i++){
			its[i]=list.get(UtilMath.r(list.size()));
			
			its[i].setAmount(its[i].getAmount()+1);
			list.remove(its[i]);
		}
		
		return its;
	}
	
	public void loadItems(){
		if(this.config.contains("TreasureChest.items")){
			for(String s : this.config.getPathList("TreasureChest.items").keySet()){
				items.add(new TreasureItem(this.config.getItemStack("TreasureChest.items."+s+".item"), this.config.getInt("TreasureChest.items."+s+".nenner"), this.config.getInt("TreasureChest.items."+s+".amount")));
			}
		}
	}
	
	public boolean removeItem(int id){
		this.config.set("TreasureChest.items."+id, null);
		return true;
	}
	
	public int addItem(ItemStack item, int nenner){
		this.config.setItemStack("TreasureChest.items."+this.amount+".item",item);
		this.config.set("TreasureChest.items."+this.amount+".nenner",nenner);
		this.config.set("TreasureChest.items."+this.amount+".amount",0);
		
		this.items.add(new TreasureItem(item, 0, nenner));
		
		this.amount++;
		this.config.set("TreasureChest.amount", this.amount);
		this.config.save();
		return (this.amount-1);
	}
	

	@EventHandler
	public void interact(PlayerInteractEvent ev){
		if(sessions.containsKey(ev.getPlayer())){
			if(ev.getAction() == Action.RIGHT_CLICK_BLOCK){
				if(ev.getClickedBlock().getType() == Material.CHEST){
					ev.setCancelled(true);
					sessions.get(ev.getPlayer()).drop(ev.getClickedBlock());
				}
			}
		}
	}
	
	ArrayList<TreasureChestSession> remove;
	@EventHandler
	public void updater(UpdateEvent ev){
		if(ev.getType() == UpdateType.TICK && !sessions.isEmpty()){
			if(remove==null)remove=new ArrayList<>();
			for(TreasureChestSession session : sessions.values()){
				if(!session.next()){
					remove.add(session);
				}
			}
			
			if(!remove.isEmpty()){
				for(TreasureChestSession session : remove){
					sessions.remove(session.getPlayer());
					session.remove();
				}
				remove.clear();
			}
		}
	}
}
