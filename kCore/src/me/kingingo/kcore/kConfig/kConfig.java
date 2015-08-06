package me.kingingo.kcore.kConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilNumber;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Preconditions;

public class kConfig extends YamlConfiguration{

	private File configFile;
	 
	public kConfig(File configFile){
		this.configFile=configFile;
		try {
			load(configFile);
		} catch (FileNotFoundException e) {
			System.err.println("[EpicPvP] Config "+configFile.getPath()+" erstellt.");
		} catch (IOException e) {
			System.err.println("[EpicPvP] IOException:");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			System.err.println("[EpicPvP] InvalidConfigurationException:");
			e.printStackTrace();
		}
	}
	
	public Map<String, Object> getPathList(String path){
	    if (isConfigurationSection(path)){
	      return getConfigurationSection(path).getValues(false);
	    }
	    return new HashMap();
	}
	
	public ItemStack getItemStack(String path){
		try {
			return UtilInv.itemStackFromBase64(getString(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setItemStack(String path,ItemStack item){
		set(path, UtilInv.itemStackToBase64(item));
	}
	
	public ItemStack[] getItemStackArray(String path){
		try {
			return UtilInv.itemStackArrayFromBase64(getString(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setItemStackArray(String path,ItemStack[] items){
		set(path, UtilInv.itemStackArrayToBase64(items));
	}
	
	public Inventory getInventory(String path){
		try {
			return UtilInv.fromBase64(getString(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setInventory(String path,Inventory inventory){
		set(path, UtilInv.toBase64(inventory));
	}
	
	public void setLocation(String path,Location location){
		set(path+".world", location.getWorld().getName());
		set(path+".x", location.getX());
		set(path+".y", location.getY());
		set(path+".z", location.getZ());
		set(path+".pitch", location.getPitch());
		set(path+".yaw", location.getYaw());
	}
	
	public void save(){
		try {
			super.save(getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Location getLocation(String path){
		String world = getString(path+".world");
		if(Bukkit.getWorld(world)!=null){
			Location loc = new Location(Bukkit.getWorld(world),getDouble(path+".x"),getDouble(path+".y"),getDouble(path+".z"));
			loc.setYaw(getFloat(path+".yaw"));
			loc.setPitch(getFloat(path+".pitch"));
			return loc;
		}
		return null;
	}
	
	public File getFile(){
		return configFile;
	}

	public float getFloat(String key){
		Object obj = get(key);
		return (obj instanceof Number) ? UtilNumber.toFloat(obj) : null;
	}

}
