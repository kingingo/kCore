package eu.epicpvp.kcore.kConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilTime;

public class kConfig extends YamlConfiguration{

	private File configFile;
	
	public kConfig(String configFile){
		this(new File(configFile));
	}
	
	public kConfig(File configFile){
		this.configFile=configFile;
		try {
			load(configFile);
		} catch (FileNotFoundException e) {
			System.err.println("[ClashMC] Config "+configFile.getPath()+" erstellt.");
		} catch (IOException e) {
			System.err.println("[ClashMC] IOException:");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			System.err.println("[ClashMC] InvalidConfigurationException:");
			e.printStackTrace();
		}
	}
	
	public Map<String, Object> getPathList(String path){
	    if (isConfigurationSection(path)){
	      return getConfigurationSection(path).getValues(false);
	    }
	    return new HashMap();
	}
	
	public ArrayList<ItemStack> getItemStackList(String path){
		ArrayList<ItemStack> items = new ArrayList<>();
		
			List<String> list = getStringList(path);
		
			for(String s : list){
				try {
					items.add(UtilInv.itemStackFromBase64(s));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		return items;
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
	
	public Creature getCreature(String path){
		Creature c = (Creature)getEntity(path);
		
		c.getEquipment().setArmorContents(getItemStackArray(path+".equipment.armorcontents"));
		c.getEquipment().setItemInHand(getItemStack(path+".equipment.hand"));
		
		return c;
	}
	
	public void setCreature(String path,Creature creature){
		setEntity(path, creature);
		setItemStackArray(path+".equipment.armorcontents", creature.getEquipment().getArmorContents());
		setItemStack(path+".equipment.hand", creature.getEquipment().getItemInHand());
	}
	
	public Entity getEntity(String path){
		Location location = getLocation(path+".location");
		if(location != null){
			EntityType type = EntityType.fromId(getInt(path+".type"));
			if(type!=null){
				Entity entity = location.getWorld().spawnEntity(location, type);
				entity.setCustomName(getString(path+".custname"));
				entity.setCustomNameVisible(getBoolean(path+".isCustomVisible"));
				
				return entity;
			}
		}
		return null;
	}
	
	public void setEntity(String path,Entity entity){
		set(path+".custname", entity.getCustomName());
		set(path+".isCustomVisible", entity.isCustomNameVisible());
		set(path+".type", entity.getType().getTypeId());
		setLocation(path+".location", entity.getLocation());
	}
	
	private Location StringToLocation(String loc){
		String[] s = loc.split(";");
		Location l = new Location(Bukkit.getWorld(s[5]),Double.valueOf(s[0]),Double.valueOf(s[1]),Double.valueOf(s[2]));
		l.setYaw(Float.valueOf(s[3]));
		l.setPitch(Float.valueOf(s[4]));
		return l;
	}
	
	private String locationToString(Location loc){
		return loc.getX()+";"+loc.getY()+";"+loc.getZ()+";"+loc.getYaw()+";"+loc.getPitch()+";"+loc.getWorld().getName();
	}
	
	public List<Location> getLocationList(String path){
		ArrayList<Location> locs = new ArrayList<>();
		List<String> l = getStringList(path);
		for(String s : l)locs.add(StringToLocation(s));
		return locs;
	}
	
	public void setLocationList(String path,List<Location> locs){
		ArrayList<String> s = new ArrayList<>();
		
		for(Location l : locs)s.add(locationToString(l));
		set(path, s);
	}
	
	public void setLocationList(String path,Location[] locs){
		ArrayList<String> s = new ArrayList<>();
		
		for(Location l : locs)s.add(locationToString(l));
		set(path, s);
	}
	
	public void setLocation(String path,Location location){
		set(path+".world", location.getWorld().getName());
		set(path+".x", location.getX());
		set(path+".y", location.getY());
		set(path+".z", location.getZ());
		set(path+".pitch", location.getPitch());
		set(path+".yaw", location.getYaw());
		set(path+".Date", UtilTime.now());
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
