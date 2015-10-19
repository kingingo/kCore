package me.kingingo.kcore.Util;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;

public class UtilWorldEdit {

	private static EditSession editSession=null ;
	
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
	
	public static void pastePlate(Location l,File file){
		if(editSession==null)editSession=new EditSession(new BukkitWorld(l.getWorld()), 999999999);
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
	
}
