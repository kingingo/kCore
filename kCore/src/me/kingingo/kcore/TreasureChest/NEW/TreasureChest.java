package me.kingingo.kcore.TreasureChest.NEW;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.TreasureChest.NEW.TreasureItems.TreasureItem;
import me.kingingo.kcore.Util.UtilFile;
import me.kingingo.kcore.kConfig.kConfig;

public class TreasureChest extends kListener{

	private ItemStack item;
	private int amount;
	private String name;
	private kConfig config;
	private ArrayList<TreasureItem> items;
	
	public TreasureChest(JavaPlugin instance, String configName){
		this(instance,new kConfig(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"chests"+File.separator+configName+".yml"));
	}
	
	public TreasureChest(JavaPlugin instance,kConfig config) {
		super(instance, "TreasureChest:"+config.getName());
		this.config=config;
		this.name=config.getName();
		
		this.item=this.config.getItemStack("TreasureChest.item");
		this.amount=this.config.getInt("TreasureChest.amount");
	}
	
	public TreasureChest(JavaPlugin instance,ItemStack item,String name) {
		super(instance, "TreasureChest:"+name);
		new File(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"chests").mkdirs();
		this.config=new kConfig(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"chests"+File.separator+name+".yml");
		this.item=item;
		this.name=name;
		setData(this.item);
	}
	
	public void setData(ItemStack item){
		this.config.setItemStack("TreasureChest.item", item);
		this.config.set("TreasureChest.amount", 0);
		this.config.save();
	}
	
	public boolean removeItem(int id){
		this.config.set("TreasureChest."+id, null);
		return true;
	}

	public int addItem(ItemStack item, int nenner){
		this.config.setItemStack("TreasureChest."+this.amount+".item",item);
		this.config.set("TreasureChest."+this.amount+".nenner",nenner);
		this.config.set("TreasureChest."+this.amount+".amount",0);
		
		this.items.add(new TreasureItem(item, 0, nenner));
		
		this.amount++;
		this.config.set("TreasureChest.amount", this.amount);
		this.config.save();
		return (this.amount-1);
	}

}
