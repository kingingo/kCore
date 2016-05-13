package eu.epicpvp.kcore.MysteryBox;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.nbt.NBTTagCompound;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MysteryBox.Templates.Building;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsChangedEvent;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsLoadedEvent;
import eu.epicpvp.kcore.Util.UtilFile;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class MysteryBoxManager extends kListener{

	public static File chestPath;
	public static File templatePath;
	
	@Getter
	private JavaPlugin instance;
	@Getter
	private HashMap<String, MysteryBox> chests;
	private StatsManager statsManager;
	@Getter
	private ArrayList<Location> blocked;
	
	public MysteryBoxManager(JavaPlugin instance){
		super(instance,"MysteryBoxManager");
		this.instance=instance;
		this.chestPath=new File(UtilFile.getPluginFolder(instance)+File.separator+"MysteryBox"+File.separator+"Chests");
		this.templatePath=new File(UtilFile.getPluginFolder(instance)+File.separator+"MysteryBox"+File.separator+"Templates");
		this.chestPath.mkdirs();
		this.templatePath.mkdirs();
		this.statsManager=StatsManagerRepository.getStatsManager(GameType.PROPERTIES);
		this.statsManager.setForceSave(true);
		this.statsManager.setAutoLoad(true);
		
		this.chests=new HashMap<>();
		this.blocked=new ArrayList<>();
		UtilServer.getCommandHandler().register(CommandMysteryBox.class, new CommandMysteryBox(this));
		loadChests();
		UtilServer.setMysteryBoxManager(this);
	}
	
	public boolean isBlocked(Location location){
		for(Location c : getBlocked()){
			if(c.distanceSquared(location) < 8){
				return true;
			}
		}
		return false;
	}
	
	public void addAmount(Player player, int amount, String chest){
		setAmount(UtilPlayer.getPlayerId(player), amount+getAmount(UtilPlayer.getPlayerId(player), chest), chest);
	}
	
	public void setAmount(Player player, int amount, String chest){
		setAmount(UtilPlayer.getPlayerId(player), amount, chest);
	}
	
	public void setAmount(int playerId, int amount, String chest){
		NBTTagCompound nbt = this.statsManager.getNBTTagCompound(playerId, StatsKey.PROPERTIES);
		nbt.setInt("MysteryBox"+chest, amount);
		try {
			this.statsManager.setNBTTagCompound(playerId, nbt, StatsKey.PROPERTIES);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getAmount(Player player, String chest){
		return getAmount(UtilPlayer.getPlayerId(player), chest);
	}
	
	public int getAmount(int playerId, String chest){
		NBTTagCompound nbt = this.statsManager.getNBTTagCompound(playerId, StatsKey.PROPERTIES);
		
		if(nbt.hasKey("MysteryBox"+chest)){
			return nbt.getInt("MysteryBox"+chest);
		}
		return 0;
	}
	
	public MysteryBox getChest(String treasureName){
		if(this.chests.containsKey(treasureName)){
			return this.chests.get(treasureName);
		}
		return null;
	}
	
	public void loadChests(){
		ArrayList<File> files = UtilFile.loadFiles(chestPath,".yml");
		
		MysteryBox chest;
		for(File file : files){
			chest=new MysteryBox(this, file.getName().replaceAll(".yml", ""));
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
			MysteryBox chest = MysteryBox.createChest(this, template, item, treasureName);
			this.chests.put(treasureName, chest);
			return true;
		}
		return false;
	}
}