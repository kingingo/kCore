package eu.epicpvp.kcore.TeleportManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.TeleportManager.Events.PlayerTeleportedEvent;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilTime;
import lombok.Getter;

public class Teleporter {

	@Getter
	private Player from;
	@Getter
	private Location loc_from;
	@Getter
	private Player to;
	@Getter
	private Location loc_to;
	@Getter
	private long time=0;
	@Getter
	private boolean move=true;
	
	public Teleporter(Player from,Player to, int sec, boolean move){
		this(from,to.getLocation(),sec,move);
		this.to=to;
	}
	
	public Teleporter(Player from,Player to, int sec){
		this(from,to.getLocation(),sec,true);
	}
	
	public Teleporter(Player from,Location loc_to){
		this(from,loc_to,0,true);
	}
	
	public Teleporter(Player from,Location loc_to,boolean move){
		this(from,loc_to,0,move);
	}
	
	public Teleporter(Player from,Location loc_to,int sec){
		this(from,loc_to,sec,true);
	}
	
	public Teleporter(Player from,Player to,Location loc_to,int sec){
		this(from,loc_to,sec,true);
		this.to=to;
	}
	
	public Teleporter(Player from,Player to,Location loc_to,int sec,boolean move){
		this(from,loc_to,sec,move);
		this.to=to;
	}
	
	public Teleporter(Player from,Player to){
		this(from,to.getLocation(),0,true);
	}
	
	public Teleporter(Player from,Player to, boolean move){
		this(from,to,0,move);
	}
	
	public Teleporter(Player from,Location loc_to, int sec, boolean move){
		this.from=from;
		this.loc_from=from.getLocation();
		this.loc_to=loc_to;
		this.move=move;
		this.time=TimeSpan.SECOND*sec;
	}
	
	public static boolean hasMoved(Teleporter teleporter){
		if(teleporter.isMove()){
			if(teleporter.getLoc_from().getBlockX() == teleporter.getFrom().getLocation().getBlockX()
					&&teleporter.getLoc_from().getBlockY() == teleporter.getFrom().getLocation().getBlockY()
					&&teleporter.getLoc_from().getBlockZ() == teleporter.getFrom().getLocation().getBlockZ()){
				return false;
			}else{
				
				if(Math.abs(teleporter.getLoc_from().getX()-teleporter.getFrom().getLocation().getX())<0.6
						&&Math.abs(teleporter.getLoc_from().getY()-teleporter.getFrom().getLocation().getY())<0.6
						&&Math.abs(teleporter.getLoc_from().getZ()-teleporter.getFrom().getLocation().getZ())<0.6){
					return false;
				}else{
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean TeleportDo(){
		if(!from.isOnline()){
			from=null;
			loc_from=null;
			to=null;
			loc_to=null;
			time=0;
			return true;
		}else{
			if(time!=0){
				if(hasMoved(this)){
					from=null;
					loc_from=null;
					to=null;
					loc_to=null;
					time=0;
					return true;
				}
				
				if(getTime() > System.currentTimeMillis()){
					from.sendMessage(TranslationManager.getText(from, "PREFIX")+TranslationManager.getText(from, "TELEPORT_VERZ§GERUNG",UtilTime.formatMili(getTime()-System.currentTimeMillis())));
					return false;
				}

				loc_from=null;
				time=0;
			}
			
			if(getLoc_to()!=null){
				from.leaveVehicle();
				from.setVelocity(new Vector(0,0,0));
				
				from.teleport(getLoc_to(), TeleportCause.PLUGIN);
				from.sendMessage(TranslationManager.getText(from, "PREFIX")+TranslationManager.getText(from, "TELEPORT"));

				Bukkit.getPluginManager().callEvent(new PlayerTeleportedEvent(this));
				from=null;
				loc_from=null;
				to=null;
				loc_to=null;
				time=0;
				return true;
			}else{
				if(to==null||from==null){
					if(getLoc_to()==null){
						System.out.println("[TeleportManager] LOC_TO == NULL");
					}
					
					if(from==null){
						System.out.println("[TeleportManager] FROM == NULL");
					}
				}else{
					from.sendMessage(TranslationManager.getText(from, "PREFIX")+
					TranslationManager.getText(from, "PLAYER_IS_OFFLINE",to.getName()));
				}
				from=null;
				loc_from=null;
				to=null;
				loc_to=null;
				time=0;
				return true;
			}
		}
	}
}
