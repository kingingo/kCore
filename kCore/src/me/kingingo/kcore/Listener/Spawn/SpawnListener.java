package me.kingingo.kcore.Listener.Spawn;

import java.io.File;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Listener.Spawn.Commands.CommandSetSpawn;
import me.kingingo.kcore.Listener.Spawn.Commands.CommandSpawn;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilFile;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnListener extends kListener{
	
	@Getter
	private CommandHandler cmdHandler;
	@Getter
	private PermissionManager permManager;
	@Getter
	private HashMap<Player,Long> teleport = new HashMap<>();
	@Getter
	private HashMap<Player,Location> teleport_loc = new HashMap<>();
	@Getter
	private World world;

	public SpawnListener(JavaPlugin instance,World world,PermissionManager permManager,CommandHandler cmdHandler) {
		super(instance, "SpawnListener");
		this.cmdHandler=cmdHandler;
		this.permManager=permManager;
		this.world=world;
	
		this.cmdHandler.register(CommandSpawn.class, new CommandSpawn(this));
		this.cmdHandler.register(CommandSetSpawn.class, new CommandSetSpawn(this));
		
		if(UtilFile.existPath(new File(world.getName()+File.separator+"spawn.dat"))){
			String[] list = UtilFile.loadFile(new File(world.getName()+File.separator+"spawn.dat"));
			world.setSpawnLocation(Integer.valueOf(list[0]), Integer.valueOf(list[1]), Integer.valueOf(list[2]));
		}else{
			world.setSpawnLocation(0, 90, 0);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void Respawn(PlayerRespawnEvent ev){
		ev.setRespawnLocation(ev.getRespawnLocation().getWorld().getSpawnLocation());
	}
	
	Player p;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FAST)return;
		if(teleport.isEmpty())return;
		for(int i = 0; i < teleport.size(); i++){
			p=((Player)teleport.keySet().toArray()[i]);
			
			if(teleport.get(p) <= System.currentTimeMillis()){
				
				if(!p.isOnline()){
					teleport.remove(p);
					teleport_loc.remove(p);
					continue;
				}
				
				if(!hasMoved(teleport_loc.get(p), p)){
					TeleportToSpawn(p);
				}else{
					p.sendMessage(Text.PREFIX.getText()+Text.GILDE_TELEPORT_CANCELLED.getText());
				}
				teleport.remove(p);
				teleport_loc.remove(p);
			}else{
				p.sendMessage(Text.PREFIX.getText()+Text.GILDE_HOME.getText( UtilTime.formatMili( (teleport.get(p)-System.currentTimeMillis()) ) ));
			}
		}
	}
	
	double dif_x;
	double dif_y;
	double dif_z;
	public boolean hasMoved(Location loc,Player player){
		if(loc.getBlockX() == player.getLocation().getBlockX()&&loc.getBlockY() == player.getLocation().getBlockY()&&loc.getBlockZ() == player.getLocation().getBlockZ()){
			return false;
		}else{
			dif_x=Math.abs(loc.getX()-player.getLocation().getX());
			dif_y=Math.abs(loc.getY()-player.getLocation().getY());
			dif_z=Math.abs(loc.getZ()-player.getLocation().getZ());
			
			if(dif_x<0.6&&dif_y<0.6&&dif_z<0.6){
				return false;
			}else{
				return true;
			}
		}
	}
	
	public void TeleportToSpawn(Player p){
		p.teleport(world.getSpawnLocation());
		p.sendMessage(Text.PREFIX.getText()+Text.SPAWN_TELEPORT.getText());
	}

}
