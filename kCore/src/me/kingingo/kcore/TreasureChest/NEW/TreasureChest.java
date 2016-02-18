package me.kingingo.kcore.TreasureChest.NEW;

import java.util.Set;

import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Util.UtilFile;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.Util.UtilWorldEdit;
import me.kingingo.kcore.kConfig.kConfig;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;

public class TreasureChest extends kListener{

	private kConfig config;
	private JavaPlugin instance;
	
	public TreasureChest(JavaPlugin instance){
		super(instance,"TreasureChest");
		this.config=new kConfig(UtilFile.getYMLFile(instance, "TreasureChest"));
		this.instance=instance;
		UtilServer.getCommandHandler().register(CommandTreasureChest.class, new CommandTreasureChest(this));
	}

	public void d(Player player){
		

		LocalSession localSession = UtilWorldEdit.getWorldEditPlugin().getSession(player);
		try {
			Region region = localSession.getSelection(BukkitUtil.getLocalWorld(player.getWorld()));
		    
			for(Vector v : region){
				
			}
		} catch (IncompleteRegionException e) {
			e.printStackTrace();
		}
	}
}
