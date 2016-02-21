package me.kingingo.kcore.TreasureChest.NEW;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.TreasureChest.NEW.TreasureItems.TreasureItem;
import me.kingingo.kcore.Util.UtilFile;
import me.kingingo.kcore.Util.UtilNumber;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.Util.UtilWorldEdit;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.Region;

public class TreasureChestManager extends kListener{

	private JavaPlugin instance;
	private HashMap<String,ArrayList<BlockVector>> templates;
	private HashMap<String,TreasureChest> treasurechests;
	
	public TreasureChestManager(JavaPlugin instance){
		super(instance,"TreasureChest");
		this.instance=instance;
		this.templates=new HashMap<>();
		this.treasurechests=new HashMap<>();
		UtilServer.getCommandHandler().register(CommandTreasureChest.class, new CommandTreasureChest(this));
		loadTemplates();
	}
	
	public boolean addTreasureChest(ItemStack item, String treasureName){
		if(this.treasurechests.containsKey(treasureName)){
			this.treasurechests.put(treasureName, new TreasureChest(instance, item, treasureName));
			return true;
		}
		return false;
	}
	
	public void loadTemplate(File file){
		Log("Load Template "+file.getName());
		try {
			this.templates.put(file.getName(), new ArrayList<>());
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			String[] split;
		    while((line=br.readLine()) != null){
		    	split = line.split(";");
		    	this.templates.get(file.getName()).add(new BlockVector(UtilNumber.toInt(split[0]), UtilNumber.toInt(split[1]), UtilNumber.toInt(split[2]), UtilNumber.toInt(split[3]), UtilNumber.toByte(split[4])));
		    }
		    
		    in.close();
		    br.close();
		    fstream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadTemplates(){
	    new File(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"templates").mkdirs();
		ArrayList<File> files = UtilFile.loadFiles(new File(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"templates"), ".dat");

		for(File file : files){
			loadTemplate(file);
		}	
	}
	
	public boolean placeTemplate(Location loc,String name){
		if(!name.endsWith(".dat"))name+=".dat";
		
		if(this.templates.containsKey(name)){
			for(BlockVector v : this.templates.get(name)){
				v.placeBlock(loc);
			}
			return true;
		}
		return false;
	}
	
	public boolean saveTemplate(Player player,String name){
		LocalSession localSession = UtilWorldEdit.getWorldEditPlugin().getSession(player);
		if(localSession!=null){
			try {
				UtilFile.deleteFile(new File(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"templates"),name+".dat");
				Region region = localSession.getSelection(BukkitUtil.getLocalWorld(player.getWorld()));
			    Vector vplayer = new Vector(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
			    FileWriter fstream= new FileWriter(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"templates"+File.separator+name+".dat");
				BufferedWriter out = new BufferedWriter(fstream); 
			    
				for(Vector v : region){
					if(player.getWorld().getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getTypeId()!=0){
						out.write((v.getBlockX()-vplayer.getBlockX())+";"+(v.getBlockY()-vplayer.getBlockY())+";"+(v.getBlockZ()-vplayer.getBlockZ())+";"+player.getWorld().getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getTypeId()+";"+player.getWorld().getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getData());
						out.write("\n");
					}
				}
				
				out.close();
				fstream.close();
			} catch (IncompleteRegionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.templates.remove(name+".dat");
			loadTemplate(new File(UtilFile.getPluginFolder(instance)+File.separator+"tc"+File.separator+"templates"+File.separator+name+".dat"));
			
			return true;
		}
		return false;
	}
}
