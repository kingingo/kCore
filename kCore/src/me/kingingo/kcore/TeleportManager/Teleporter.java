package me.kingingo.kcore.TeleportManager;

import lombok.Getter;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

public class Teleporter {

	@Getter
	private Player from;
	@Getter
	private Location loc_from;
	@Getter
	private Player player_to;
	@Getter
	private Location loc_to;
	@Getter
	private long time=0;
	
	double dif_x;
	double dif_y;
	double dif_z;
	
	public Teleporter(Player from,Player to){
		this.from=from;
		this.player_to=to;
	}
	
	public Teleporter(Player from,Player to,int sec){
		this.from=from;
		this.loc_from=from.getLocation();
		this.player_to=to;
		this.time=System.currentTimeMillis()+TimeSpan.SECOND*sec;
	}
	
	public Teleporter(Player from,Location to,int sec){
		this.from=from;
		this.loc_from=from.getLocation();
		this.loc_to=to;
		this.time=System.currentTimeMillis()+TimeSpan.SECOND*sec;
	}
	
	public Teleporter(Player from,Location to){
		this.from=from;
		this.loc_to=to;
	}
	
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
	
	public boolean TeleportDo(){
		if(!from.isOnline()){
			from=null;
			loc_from=null;
			player_to=null;
			loc_to=null;
			time=0;
			dif_x=0;
			dif_y=0;
			dif_z=0;
			return true;
		}else{
			if(time!=0){
				
				if(hasMoved(this.loc_from,from)){
					from=null;
					loc_from=null;
					player_to=null;
					loc_to=null;
					time=0;
					dif_x=0;
					dif_y=0;
					dif_z=0;
					return true;
				}
				
				if(getTime() > System.currentTimeMillis()){
					from.sendMessage(Language.getText(from, "PREFIX")+Language.getText(from, "TELEPORT_VERZÖGERUNG",UtilTime.formatMili(getTime()-System.currentTimeMillis())));
					return false;
				}

				loc_from=null;
				time=0;
			}
			
			if(getTo()!=null){
				from.leaveVehicle();
				from.setVelocity(new Vector(0,0,0));
				
				from.teleport(getTo(), TeleportCause.PLUGIN);
				from.sendMessage(Language.getText(from, "PREFIX")+Language.getText(from, "TELEPORT"));
				
				from=null;
				loc_from=null;
				player_to=null;
				loc_to=null;
				time=0;
				dif_x=0;
				dif_y=0;
				dif_z=0;
				return true;
			}else{
				from.sendMessage(Language.getText(from, "PREFIX")+Language.getText(from, "PLAYER_IS_OFFLINE",player_to.getName()));
				from=null;
				loc_from=null;
				player_to=null;
				loc_to=null;
				time=0;
				dif_x=0;
				dif_y=0;
				dif_z=0;
				return true;
			}
		}
	}
	
	public Location getTo(){
		if(this.loc_to!=null)return loc_to;
		if(this.player_to!=null){
			if(this.player_to.isOnline()){
				return this.player_to.getLocation();
			}
		}
		return null;
	}
	
}
