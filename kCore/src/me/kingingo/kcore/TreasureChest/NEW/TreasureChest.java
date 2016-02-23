package me.kingingo.kcore.TreasureChest.NEW;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.TreasureChest.NEW.Templates.Building;
import me.kingingo.kcore.TreasureChest.NEW.TreasureItems.TreasureItem;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilFile;
import me.kingingo.kcore.kConfig.kConfig;

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
		this.sessions=new HashMap<>();
		setData(this.item,template);
		loadTemplate(new File(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"templates"+File.separator+template+".dat"));
		loadItems();
	}
	
	public void start(Player player){
		this.sessions.put(player, new TreasureChestSession(player, building));
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
	
	public void loadItems(){
		if(this.config.contains("TreasureChest.items")){
			for(String s : this.config.getPathList("TreasureChest.items").keySet()){
				items.add(new TreasureItem(this.config.getItemStack(s+".item"), this.config.getInt(s+".nenner"), this.config.getInt(s+".amount")));
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
