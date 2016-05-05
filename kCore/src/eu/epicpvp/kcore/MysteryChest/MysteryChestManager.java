package eu.epicpvp.kcore.MysteryChest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MysteryChest.Templates.Building;
import eu.epicpvp.kcore.Util.UtilFile;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class MysteryChestManager extends kListener{

	public static File chestPath;
	public static File templatePath;
	
	@Getter
	private JavaPlugin instance;
	private HashMap<String, MysteryChest> chests;
	
	public MysteryChestManager(JavaPlugin instance){
		super(instance,"MysteryChestManager");
		this.instance=instance;
		this.chestPath=new File(UtilFile.getPluginFolder(instance)+File.separator+"MysteryChest"+File.separator+"Chests");
		this.templatePath=new File(UtilFile.getPluginFolder(instance)+File.separator+"MysteryChest"+File.separator+"Templates");
		this.chestPath.mkdirs();
		this.templatePath.mkdirs();
		
		this.chests=new HashMap<>();
		UtilServer.getCommandHandler().register(CommandMysteryChest.class, new CommandMysteryChest(this));
		
		loadChests();
	}
	
	public MysteryChest getChest(String treasureName){
		if(this.chests.containsKey(treasureName)){
			return this.chests.get(treasureName);
		}
		return null;
	}
	
	public void loadChests(){
		ArrayList<File> files = UtilFile.loadFiles(chestPath,".yml");
		
		MysteryChest chest;
		for(File file : files){
			chest=new MysteryChest(this, file.getName().replaceAll(".yml", ""));
			this.chests.put(chest.getName(), chest);
		}
	}
	
	public void addBuilding(Player player, String building){
		Building.save(player, instance, building);
	}
	
	public boolean removeChest(String treasureName){
		if(this.chests.containsKey(treasureName)){
			this.chests.get(treasureName).getConfig().getFile().delete();
			this.chests.remove(treasureName);
			return true;
		}
		return false;
	}
	
	public boolean addChest(ItemStack item,String template, String treasureName){
		if(!chests.containsKey(treasureName)){
			MysteryChest chest = MysteryChest.createChest(this, template, item, treasureName);
			this.chests.put(treasureName, chest);
			return true;
		}
		return false;
	}
}