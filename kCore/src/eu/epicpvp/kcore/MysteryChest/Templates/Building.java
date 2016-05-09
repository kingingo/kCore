package eu.epicpvp.kcore.MysteryChest.Templates;

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
import java.util.Collections;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.Region;

import eu.epicpvp.kcore.MysteryChest.MysteryChestManager;
import eu.epicpvp.kcore.Util.UtilFile;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilParticle;
import eu.epicpvp.kcore.Util.UtilWorldEdit;
import eu.epicpvp.kcore.kListen.BlockVectorComparable;

public class Building {
	private ArrayList<BlockVector> blocks;
	private ArrayList<BlockVector> chests;
	
	public Building(File file){
		this.blocks=new ArrayList<>();
		this.chests=new ArrayList<>();
		load(file);
		InToOut();
	}
	
	public BlockState nextBlock(Location location, int i){
		if(!this.blocks.isEmpty()&&this.blocks.size()>i){
			return this.blocks.get(i).placeBlock(location);
		}
		return null;
	}
	
	public void nextChest(Location location,ArrayList<BlockState> blocks){
		for(BlockVector v : chests){
			UtilParticle.SMOKE_NORMAL.display(0.25F, 0.25F, 0.25F, 0, 20, v.getLocation(location), 25);
			blocks.add(v.placeBlock(location));
		}
	}
	
	public void OutToIn(){
		ArrayList<BlockVectorComparable> new_blocks = new ArrayList<>();

		BlockVector center=new BlockVector(0,0,0, 7, (byte)0);
		for(BlockVector v : blocks){
			new_blocks.add(new BlockVectorComparable(v, center.distance(v)));
		}
		
		Collections.sort(new_blocks, BlockVectorComparable.DESCENDING);
		
		blocks.clear();
		for(BlockVectorComparable v : new_blocks){
			blocks.add(v.getVector());
		}
	}
	
	public void InToOut(){
		ArrayList<BlockVectorComparable> new_blocks = new ArrayList<>();
		
		BlockVector center=new BlockVector(0,0,0, 7, (byte)0);
		for(BlockVector v : blocks){
			new_blocks.add(new BlockVectorComparable(v, center.distance(v)));
		}
		
		Collections.sort(new_blocks, BlockVectorComparable.ASCENDING);
		
		blocks.clear();
		for(BlockVectorComparable v : new_blocks){
			blocks.add(v.getVector());
		}
	}
	
	public void shuffle(){
		Collections.shuffle(blocks);
	}
	
	public void load(File file){
		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			String[] split;
		    while((line=br.readLine()) != null){
		    	split = line.split(";");
		    	if(UtilNumber.toInt(split[3])!=Material.CHEST.getId()){
		    		this.blocks.add(new BlockVector(UtilNumber.toInt(split[0]), UtilNumber.toInt(split[1]), UtilNumber.toInt(split[2]), UtilNumber.toInt(split[3]), UtilNumber.toByte(split[4])));	
				}else{
					this.chests.add(new BlockVector(UtilNumber.toInt(split[0]), UtilNumber.toInt(split[1]), UtilNumber.toInt(split[2]), UtilNumber.toInt(split[3]), UtilNumber.toByte(split[4])));	
				}
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

	public static boolean save(Player player,JavaPlugin instance,String name){
		LocalSession localSession = UtilWorldEdit.getWorldEditPlugin().getSession(player);
		if(localSession!=null){
			try {
				UtilFile.deleteFile(MysteryChestManager.templatePath,name+".dat");
				Region region = localSession.getSelection(BukkitUtil.getLocalWorld(player.getWorld()));
			    Vector vplayer = new Vector(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
			    FileWriter fstream= new FileWriter(new File(MysteryChestManager.templatePath,name+".dat"));
				BufferedWriter out = new BufferedWriter(fstream); 
			    
				for(Vector v : region){
					out.write((v.getBlockX()-vplayer.getBlockX())+";"+(v.getBlockY()-vplayer.getBlockY())+";"+(v.getBlockZ()-vplayer.getBlockZ())+";"+player.getWorld().getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getTypeId()+";"+player.getWorld().getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getData());
					out.write("\n");
				}
				
				out.close();
				fstream.close();
			} catch (IncompleteRegionException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}
