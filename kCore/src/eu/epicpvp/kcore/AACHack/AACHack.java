package eu.epicpvp.kcore.AACHack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.ClientWrapper;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.client.connection.PacketListener;
import dev.wolveringer.dataserver.protocoll.packets.Packet;
import eu.epicpvp.kcore.AntiLogout.AntiLogoutManager;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Packets.PacketAACReload;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import lombok.Setter;
import me.konsolas.aac.api.AACAPIProvider;
import me.konsolas.aac.api.HackType;
import me.konsolas.aac.api.PlayerViolationCommandEvent;

public class AACHack extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private MySQL mysql;
	@Setter
	@Getter
	private AntiLogoutManager antiLogoutManager;
	private Date MyDate;
	private SimpleDateFormat df2;
	@Getter
	private String server;
	private ClientWrapper client;

	public AACHack(String server,MySQL mysql,ClientWrapper client) {
		super(mysql.getInstance(), "AACHack");
		if(Bukkit.getPluginManager().getPlugin("AAC")==null){
			logMessage("Das Plugin AAC fehlt!!!");
			return;
		}
		this.instance=mysql.getInstance();
		this.server=server;
		this.mysql=mysql;
		this.client=client;
		this.MyDate= new Date();
		this.df2= new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		this.df2.setTimeZone(TimeZone.getDefault());
		this.df2.format(MyDate);
	
		
		UtilServer.getClient().getHandle().getHandlerBoss().addListener(new PacketListener() {
			
			@Override
			public void handle(Packet packet) {
				if(packet instanceof PacketAACReload){
					AACAPIProvider.getAPI().reloadAAC();
				}
			}
		});
		
		getMysql().Update("CREATE TABLE IF NOT EXISTS AAC_HACK(playerId int,ip varchar(30),server varchar(30),timestamp timestamp,hackType varchar(30),violations int)");
		logMessage("AACHack System aktiviert");
	}
	
	int anzahl=0;
	@EventHandler
	public void onPlayerViolationKick(PlayerViolationCommandEvent ev){
		if(ev.getCommand().contains("kick")){
			if(!ev.isCancelled()){
				getMysql().Update("INSERT INTO AAC_HACK (playerId,ip,server,hackType,violations) VALUES ('"+UtilPlayer.getPlayerId(ev.getPlayer())+"','"+ev.getPlayer().getAddress().getAddress().getHostAddress()+"','"+server+"','"+ev.getHackType().getName()+"','0');");
				
				if(getAntiLogoutManager()!=null&&ev.getHackType()!=HackType.SPAM)getAntiLogoutManager().del(ev.getPlayer());
				
				if(ev.getHackType()==HackType.FLY
					||	ev.getHackType()==HackType.FASTBOW
					|| ev.getHackType()==HackType.FIGHTSPEED
					|| ev.getHackType()==HackType.FORCEFIELD){
					anzahl=getMysql().getInt("SELECT COUNT(*) FROM AAC_HACK WHERE hackType='"+ev.getHackType().getName()+"' AND playerId='"+UtilPlayer.getPlayerId(ev.getPlayer())+"'");
					
					if(anzahl>=5){
						String type="";
						int a = 0;
						if(anzahl<=15){
							a=anzahl*2;
							type="min";
						}else if(anzahl<=20){
							a=anzahl;
							type="std";
						}else{
							a=anzahl/2;
							type="tag";
						}
						
						setZeitBan(ev.getPlayer(), a, type, ev.getHackType().getName());
						ev.setCancelled(true);
					}
				}
			}
		}
	}
	
	private void setZeitBan(Player banned,int ti,String typ,String reason){
		long time=0;
		
		if(typ.equalsIgnoreCase("sec")){
			long t = 1000 * ti;
			time = System.currentTimeMillis() + t;
		}else if(typ.equalsIgnoreCase("min")){
			long t = 60000 * ti;
			time = System.currentTimeMillis() + t;
		}else if(typ.equalsIgnoreCase("std")){
			long t = 3600000 * ti;
			time = System.currentTimeMillis() + t;
		}else if(typ.equalsIgnoreCase("tag")){
			long t = 86400000 * ti;
			time = System.currentTimeMillis() + t;
		}
		
		LoadedPlayer loadedplayer = client.getPlayerAndLoad( banned.getName() );
		loadedplayer.banPlayer(banned.getAddress().getHostName(), "AAC", "AAC", null, -1, time, reason);
		UtilServer.sendTeamMessage("§cDer Spieler §e"+banned.getName()+"§c wurde von §eAntiHack§c für "+ ti +" "+typ.toUpperCase()+" Gesperrt Grund: §e"+reason);
	}
	
	private void setBan(int lvl,Player banned,String reason){
		LoadedPlayer loadedplayer = client.getPlayerAndLoad( banned.getName() );
		loadedplayer.banPlayer(banned.getAddress().getHostName(), "AAC", "AAC", null, -1, -1, reason);
		UtilServer.sendTeamMessage("§cDer Spieler §e"+banned.getName()+"§c wurde von §eAntiHack§c Permanent Gesperrt Grund:§e "+reason);
	}
}
