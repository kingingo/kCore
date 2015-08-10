package me.kingingo.kcore.AACHack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.AntiLogout.AntiLogoutManager;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Packet.Packets.BUNGEECORD_BANNED;
import me.kingingo.kcore.Packet.Packets.BUNGEECORD_ZEITBAN;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import me.konsolas.aac.api.HackType;
import me.konsolas.aac.api.PlayerViolationEvent;
import me.konsolas.aac.api.PlayerViolationKickEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class AACHack extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private MySQL mysql;
	@Getter
	private PacketManager packetManager;
	@Setter
	@Getter
	private AntiLogoutManager antiLogoutManager;
	private Date MyDate;
	private SimpleDateFormat df2;
	@Getter
	private String server;
	
	public AACHack(String server,MySQL mysql,PacketManager packetManager) {
		super(mysql.getInstance(), "AACHack");
		if(Bukkit.getPluginManager().getPlugin("AAC")==null){
			Log("Das Plugin AAC fehlt!!!");
			return;
		}
		this.instance=mysql.getInstance();
		this.server=server;
		this.mysql=mysql;
		this.packetManager=packetManager;
		this.MyDate= new Date();
		this.df2= new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		this.df2.setTimeZone(TimeZone.getDefault());
		this.df2.format(MyDate);
	
		getMysql().Update("CREATE TABLE IF NOT EXISTS AAC_HACK(name varchar(30),ip varchar(30),uuid varchar(100),server varchar(30),time varchar(30),hackType varchar(30),violations int)");
		Log("AACHack System aktiviert");
	}
	
	int anzahl=0;
	@EventHandler
	public void onPlayerViolationKick(PlayerViolationKickEvent ev){
		if(!ev.isCancelled()){
			Date now = new GregorianCalendar().getTime();
			getMysql().Update("INSERT INTO AAC_HACK (name,ip,uuid,server,time,hackType,violations) VALUES ('"+ev.getPlayer().getName().toLowerCase()+"','"+ev.getPlayer().getAddress().getAddress().getHostAddress()+"','"+UtilPlayer.getRealUUID(ev.getPlayer())+"','"+server+"','"+df2.format(now)+"','"+ev.getHackType().getName()+"','"+ev.getViolations()+"');");
			
			if(getAntiLogoutManager()!=null&&ev.getHackType()!=HackType.SPAM)getAntiLogoutManager().del(ev.getPlayer());
			
			if(ev.getHackType()==HackType.FLY
				||	ev.getHackType()==HackType.FASTBOW
				|| ev.getHackType()==HackType.FIGHTSPEED
				|| ev.getHackType()==HackType.FORCEFIELD){
				anzahl=getMysql().getInt("SELECT COUNT(*) FROM AAC_HACK WHERE hackType='"+ev.getHackType().getName()+"' AND name='"+ev.getPlayer().getName().toLowerCase()+"' AND ip='"+ev.getPlayer().getAddress().getAddress().getHostAddress()+"' AND uuid='"+UtilPlayer.getRealUUID(ev.getPlayer())+"'");
				
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
	
	@EventHandler
    public void onPlayerViolation(PlayerViolationEvent ev) {
		
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
		
		Date now = new GregorianCalendar().getTime();
		getMysql().Update("INSERT INTO BG_ZEITBAN (name, nameip,name_uuid,banner,bannerip,banner_uuid,date,time,reason, aktiv) VALUES ('" + banned.getName().toLowerCase() + "', '" + banned.getAddress().getAddress().getHostAddress() + "','"+UtilPlayer.getRealUUID(banned)+"', 'AACHack', 'AACHack', 'AACHack','" + df2.format(now) + "','"+time+"', '" + reason + "', 'true')");
		getPacketManager().SendPacket("BG", new BUNGEECORD_ZEITBAN(banned.getName(), "AntiHack", reason, String.valueOf(ti) ,typ));
		UtilServer.sendTeamMessage("§cDer Spieler §e"+banned.getName()+"§c wurde von §eAntiHack§c für "+ ti +" "+typ.toUpperCase()+" Gesperrt Grund: §e"+reason, packetManager);
	}
	
	private void setBan(int lvl,Player banned,String reason){
		Date now = new GregorianCalendar().getTime();
		getMysql().Update("INSERT INTO BG_BAN (name, nameip,name_uuid,banner,bannerip,banner_uuid,time,reason,level,aktiv) VALUES ('" + banned.getName().toLowerCase() + "', '" + banned.getAddress().getAddress().getHostAddress() + "', '"+UtilPlayer.getRealUUID(banned)+"','AACHack', 'AACHack', 'AACHack', '" + df2.format(now) + "', '" + reason + "','"+lvl+"', 'true')");
		getPacketManager().SendPacket("BG", new BUNGEECORD_BANNED(banned.getName(), "AntiHack", reason));
		UtilServer.sendTeamMessage("§cDer Spieler §e"+banned.getName()+"§c wurde von §eAntiHack§c Permanent Gesperrt Grund:§e "+reason, packetManager);
	}
}
