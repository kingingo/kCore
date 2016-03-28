package eu.epicpvp.kcore.TreasureChest.NEW;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.TreasureChest.NEW.Templates.Building;
import eu.epicpvp.kcore.Util.UtilFile;
import eu.epicpvp.kcore.Util.UtilServer;

public class TreasureChestManager extends kListener{

	private JavaPlugin instance;
	private HashMap<String, TreasureChest> chests;
	
	public TreasureChestManager(JavaPlugin instance){
		super(instance,"TreasureChest");
		this.instance=instance;
		this.chests=new HashMap<>();
		UtilServer.getCommandHandler().register(CommandTreasureChest.class, new CommandTreasureChest(this));
		loadTreasureChests();
	}
	
	public TreasureChest getChest(String treasureName){
		if(this.chests.containsKey(treasureName.toLowerCase())){
			return this.chests.get(treasureName);
		}
		return null;
	}
	
	public void loadTreasureChests(){
		ArrayList<File> files = UtilFile.loadFiles(new File(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"chests"),".yml");
		
		for(File file : files){
			System.out.println("FILE: "+file.getName());
			this.chests.put(file.getName().toLowerCase().substring(0, file.getName().length()-(".yml".length())), new TreasureChest(instance, file));
		}
	}
	
	public void addBuilding(Player player, String building){
		Building.save(player, instance, building);
	}
	
	public boolean removeTreasureChest(String treasureName){
		if(this.chests.containsKey(treasureName.toLowerCase())){
			this.chests.get(treasureName.toLowerCase()).getConfig().getFile().delete();
			this.chests.remove(treasureName.toLowerCase());
			return true;
		}
		return false;
	}
	
	public boolean addTreasureChest(ItemStack item,String template, String treasureName){
		if(!chests.containsKey(treasureName.toLowerCase())){
			this.chests.put(treasureName.toLowerCase(), new TreasureChest(instance, template, item, treasureName));
			return true;
		}
		return false;
	}
}