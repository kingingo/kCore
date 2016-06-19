package eu.epicpvp.kcore.Util;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.function.FlatRegionFunction;
import com.sk89q.worldedit.function.FlatRegionMaskingFilter;
import com.sk89q.worldedit.function.biome.BiomeReplace;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.visitor.FlatRegionVisitor;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.Regions;
import com.sk89q.worldedit.world.biome.BaseBiome;

public class UtilWorldEdit {

	private static EditSession editSession=null ;
	
	public static WorldEditPlugin getWorldEditPlugin(){
		 return (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
	}
	
	public static void removePlate(){
		if(editSession!=null){
			editSession.undo(editSession);
		}
	}
	
	private static void loadArea(World world, File file,Vector origin) throws DataException, IOException, MaxChangedBlocksException{
	    CuboidClipboard cc = null;
		try {
			cc = CuboidClipboard.loadSchematic(file);
		} catch (com.sk89q.worldedit.world.DataException e) {
			e.printStackTrace();
		}
		
	    cc.paste(editSession, origin, true);
	    cc=null;
	}
	
	public static void simulateThaw(Location location, int size){
		if(editSession==null)editSession=new EditSession(BukkitUtil.getLocalWorld(location.getWorld()), 999999999);
		
		try {
			editSession.thaw(new Vector(location.getX(),location.getY(), location.getZ()), size);
		} catch (MaxChangedBlocksException e) {
			e.printStackTrace();
		}
	}
	
	public static void simulateSnow(Location location, int size){
		if(editSession==null)editSession=new EditSession(BukkitUtil.getLocalWorld(location.getWorld()), 999999999);
		
		try {
			editSession.simulateSnow(new Vector(location.getX(),location.getY(), location.getZ()), size);
		} catch (MaxChangedBlocksException e) {
			e.printStackTrace();
		}
	}
	
	public static void pastePlate(Location l,File file){
		if(editSession==null)editSession=new EditSession(BukkitUtil.getLocalWorld(l.getWorld()), 999999999);
		Vector v = new Vector(l.getX(), l.getY(), l.getZ());
		try {
			loadArea(l.getWorld(), file, v);
		} catch (MaxChangedBlocksException e) {
			e.printStackTrace();
		} catch (DataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		v=null;
	}
	
	
	public static void setBiome(Location ecke1,Location ecke2,Biome biome){
		if(editSession==null)editSession=new EditSession(BukkitUtil.getLocalWorld(ecke1.getWorld()), 999999999);
		Mask mask = editSession.getMask();
		Mask2D mask2d = mask != null ? mask.toMask2D() : null;
		Region region = new CuboidRegion(BukkitUtil.getLocalWorld(ecke1.getWorld()),new Vector(ecke1.getBlockX(), ecke1.getBlockY(), ecke1.getBlockZ()),new Vector(ecke2.getBlockX(), ecke2.getBlockY(), ecke2.getBlockZ()) );
		
		FlatRegionFunction replace = new BiomeReplace(editSession, new BaseBiome(CraftBlock.biomeToBiomeBase(biome).id));
		if (mask2d != null) replace = new FlatRegionMaskingFilter(mask2d, replace);
		
		FlatRegionVisitor visitor = new FlatRegionVisitor(Regions.asFlatRegion(region), replace);
		try {
			Operations.completeLegacy(visitor);
		    System.err.print("[UtilWorldEdt] Biomes were changed in " + visitor.getAffected() + " columns. You may have to rejoin your game (or close and reopen your world) to see a change.");
		} catch (MaxChangedBlocksException e) {
			e.printStackTrace();
		}
	}
}
