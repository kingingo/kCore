package me.kingingo.kcore.PlayerStats;

import java.sql.ResultSet;

import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsManager {

	JavaPlugin plugin;
	MySQL mysql;
	GameType typ;
	
	public StatsManager(JavaPlugin plugin,MySQL mysql,GameType typ){
		this.plugin=plugin;
		this.mysql=mysql;
		this.typ=typ;
	}
	
	public void CreateTable(){
		Stats[] stats = typ.getStats();
		String tt = "player varchar(30),UUID varchar(100),";
		for(Stats s : stats){
			tt=tt+s.getCREATE()+",";
		}
		String t = "CREATE TABLE IF NOT EXISTS users_"+typ.getKürzel()+"("+tt.substring(0, tt.length()-1)+")";
		mysql.Update(t);
	}
	
	public String getString(Stats s,Player p){
		String i = "";
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM users"+typ.getKürzel()+" WHERE player= '"+p.getName().toLowerCase()+"'");
			while(rs.next()){
				i=rs.getString(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
		}
		return i;
	}
	
	public Integer getInt(Stats s,Player p){
		int i = 0;
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM users"+typ.getKürzel()+" WHERE player= '"+p.getName().toLowerCase()+"'");
			while(rs.next()){
				i=rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
		}
		return i;
	}
	
}
