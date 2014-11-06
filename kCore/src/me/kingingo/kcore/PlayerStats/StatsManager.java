package me.kingingo.kcore.PlayerStats;

import java.sql.ResultSet;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.PlayerStats.Event.PlayerStatsChangeEvent;
import me.kingingo.kcore.PlayerStats.Event.PlayerStatsCreateEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsManager implements Listener{

	@Getter
	private JavaPlugin plugin;
	@Getter
	private MySQL mysql;
	@Getter
	private GameType typ;
	@Getter
	private HashMap<Player,HashMap<Stats,Object>> list = new HashMap<>(); 
	@Getter
	@Setter
	private boolean onDisable=false;
	HashMap<Integer,String> ranking = new HashMap<>();
	
	public StatsManager(JavaPlugin plugin,MySQL mysql,GameType typ){
		this.plugin=plugin;
		this.mysql=mysql;
		this.typ=typ;
		CreateTable();
	}
	
	@EventHandler
	public void Ranking(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_08)return;
		ranking.clear();
		try{
		     ResultSet rs = getMysql().Query("SELECT `kills`,`player` FROM `users_"+getTyp().getKürzel()+"` ORDER BY kills DESC LIMIT 10;");

		      int zahl = 1;
		      
		      while (rs.next()) {
		        ranking.put(zahl, "§b#§a" + String.valueOf(zahl) + "§b | §a" + String.valueOf(rs.getInt(1)) + " §b|§a " + rs.getString(2));
		        zahl++;
		      }

		      rs.close();
		 } catch (Exception err) {
		      System.out.println("MySQL-Error: " + err.getMessage());
		 }
	}
	
	public void Ranking(Player p){
		if(ranking.isEmpty()){
			Bukkit.getPluginManager().registerEvents(this, plugin);
			try{
			     ResultSet rs = getMysql().Query("SELECT `kills`,`player` FROM `users_"+getTyp().getKürzel()+"` ORDER BY kills DESC LIMIT 10;");

			      int zahl = 1;
			      
			      while (rs.next()) {
			        ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§6 " + rs.getString(2));
			        zahl++;
			      }

			      rs.close();
			 } catch (Exception err) {
			      System.out.println("MySQL-Error: " + err.getMessage());
			 }
		}
		p.sendMessage("§b■■■■■■■■§6 §lPlayer Ranking | Top 10 §b■■■■■■■■");
		p.sendMessage("§b Place | Kills | Player");
		for(Integer i : ranking.keySet())p.sendMessage(ranking.get(i));
	}
	
	public HashMap<Integer,String> getRanking(Stats s,int i){
		HashMap<Integer,String> list = new HashMap<>();
	    try
	    {
	      ResultSet rs = mysql.Query("SELECT `player` FROM `users_"+typ.getKürzel()+"` ORDER BY "+s.getTYP()+" DESC LIMIT "+i+";");

	      int zahl = 1;
	      while (rs.next()) {
	    	  list.put(zahl, rs.getString(1));
	    	  zahl++;
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
	    return list;
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
	
	public double getKDR(int k,int d){
		double kdr = (double)k/(double)d;
		kdr = kdr * 100;
		kdr = Math.round(kdr);
		kdr = kdr / 100;
		return kdr;
	}
	
	public void SaveAllData(){
		for(Player player : list.keySet()){
			for(Stats stats : list.get(player).keySet()){
				if(!stats.isMysql())continue;
				if(list.get(player).get(stats) instanceof Integer){
					mysql.Update("UPDATE users_"+typ.getKürzel()+" SET "+stats.getTYP()+"='"+ ((Integer)list.get(player).get(stats)) +"' WHERE player='" + player.getName() + "'");
				}else if(list.get(player).get(stats) instanceof String){
					mysql.Update("UPDATE users_"+typ.getKürzel()+" SET "+stats.getTYP()+"='"+ ((String)list.get(player).get(stats)) +"' WHERE player='" + player.getName() + "'");
				}else if(list.get(player).get(stats) instanceof Double){
					mysql.Update("UPDATE users_"+typ.getKürzel()+" SET "+stats.getTYP()+"='"+ ((Double)list.get(player).get(stats)) +"' WHERE player='" + player.getName() + "'");
				}
			}
		}
		list.clear();
	}
	
	public void SaveAllPlayerData(Player p){
		if(isOnDisable())return;
		if(!list.containsKey(p))return;
		if(list.get(p).isEmpty())return;
		for(Stats st : list.get(p).keySet()){
			if(!st.isMysql())continue;
			if(list.get(p).get(st) instanceof Integer){
				mysql.Update("UPDATE users_"+typ.getKürzel()+" SET "+st.getTYP()+"='"+ ((Integer)list.get(p).get(st)) +"' WHERE player='" + p.getName() + "'");
			}else if(list.get(p).get(st) instanceof String){
				mysql.Update("UPDATE users_"+typ.getKürzel()+" SET "+st.getTYP()+"='"+ ((String)list.get(p).get(st)) +"' WHERE player='" + p.getName() + "'");
			}else if(list.get(p).get(st) instanceof Double){
				mysql.Update("UPDATE users_"+typ.getKürzel()+" SET "+st.getTYP()+"='"+ ((Double)list.get(p).get(st)) +"' WHERE player='" + p.getName() + "'");
			}
		}
		list.remove(p);
	}
	
	public void UpdatePlayer(Player p,Stats s,String i){
		if(!s.isMysql())return;
		ExistPlayer(p);
		mysql.Update("UPDATE users_"+typ.getKürzel()+" SET "+s.getTYP()+"='"+i+"' WHERE player='" + p.getName() + "'");
		list.get(p).remove(s);
	}
	
	public void UpdatePlayer(Player p,Stats s,int i){
		if(!s.isMysql())return;
		ExistPlayer(p);
		mysql.Update("UPDATE users_"+typ.getKürzel()+" SET "+s.getTYP()+"='"+i+"' WHERE player='" + p.getName() + "'");
		list.get(p).remove(s);
	}
	
	public void createEintrag(Player p){
		Stats[] stats = typ.getStats();
		String tt = "player,UUID,";
		String ti = "'"+p.getName()+"','"+p.getUniqueId()+"',";
		for(Stats s : stats){
			tt=tt+s.getTYP()+",";
			ti=ti+"'0',";
		}
		String t = "INSERT INTO users_"+typ.getKürzel()+" ("+tt.substring(0, tt.length()-1)+") VALUES ("+ti.subSequence(0, ti.length()-1)+");";
		mysql.Update(t);
		Bukkit.getPluginManager().callEvent(new PlayerStatsCreateEvent(this,p));
	}
	
	public boolean ExistPlayer(Player p){
		boolean done = false;
		if(list.containsKey(p))return true;
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `player` FROM `users_"+typ.getKürzel()+"` WHERE player='"+p.getName()+"'");

	      while (rs.next()) {
	    		  done=true;
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		
		list.put(p, new HashMap<Stats,Object>());
		if(!done)createEintrag(p);
		return done;
	}
	
	public Integer getRank(Stats s,Player p){
		ExistPlayer(p);
		if(list.containsKey(p)&&list.get(p).containsKey(Stats.RANKING)){
			return (int)list.get(p).get(Stats.RANKING);
		}
		
		boolean done = false;
		int n = 0;
		
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `player` FROM `users_"+typ.getKürzel()+"` ORDER BY `"+s.getTYP()+"` DESC;");

	      while ((rs.next()) && (!done)) {
	        n++;
	        if (rs.getString(1).equalsIgnoreCase(p.getName())) {
	          done = true;
	        }
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		
		if(n!=-1)list.get(p).put(Stats.RANKING, n);
	    return n;
	}
	
	public String getString(Stats s,Player p){	
		ExistPlayer(p);
		if(list.containsKey(p)&&list.get(p).containsKey(s)){
			return (String)list.get(p).get(s);
		}
		
		String i = "";
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE player= '"+p.getName()+"'");
			while(rs.next()){
				i=rs.getString(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
		}
		
		list.get(p).put(s, i);
		return i;
	}
	
	public void setString(Player p,String i,Stats s){
		ExistPlayer(p);
		list.get(p).put(s, i);
		Bukkit.getPluginManager().callEvent(new PlayerStatsChangeEvent(s,p));
	}
	
	public void setInt(Player p,int i,Stats s){
		ExistPlayer(p);
		list.get(p).put(s, i);
		Bukkit.getPluginManager().callEvent(new PlayerStatsChangeEvent(s,p));
	}
	
	public void setDouble(Player p,double i,Stats s){
		ExistPlayer(p);
		list.get(p).put(s, i);
		Bukkit.getPluginManager().callEvent(new PlayerStatsChangeEvent(s,p));
	}
	
	public Double getDouble(Stats s,Player p){
		ExistPlayer(p);
		if(list.containsKey(p)&&list.get(p).containsKey(s)){
			return (Double)list.get(p).get(s);
		}
		
		double i = -1;
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE player= '"+p.getName()+"'");
			while(rs.next()){
				i=rs.getDouble(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
		}
		
		if(i!=-1)list.get(p).put(s, i);
		
		return i;
	}
	
	public Integer getInt(Stats s,Player p){
		ExistPlayer(p);
		if(list.containsKey(p)&&list.get(p).containsKey(s)){
			return (Integer)list.get(p).get(s);
		}
		
		int i = -1;
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE player= '"+p.getName()+"'");
			while(rs.next()){
				i=rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
		}
		
		if(i!=-1)list.get(p).put(s, i);
		
		return i;
	}
	
	public Integer getRankWithString(Stats s,String p){
		boolean done = false;
		int n = 0;
		
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `player` FROM `users_"+typ.getKürzel()+"` ORDER BY `"+s.getTYP()+"` DESC;");

	      while ((rs.next()) && (!done)) {
	        n++;
	        if (rs.getString(1).equalsIgnoreCase(p)) {
	          done = true;
	        }
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		
	    return n;
	}
	
	public boolean ExistPlayer(String p){
		boolean done = false;
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `player` FROM `users_"+typ.getKürzel()+"` WHERE player='"+p+"'");
	      while (rs.next()) {
	    		  done=true;
	      }
	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		return done;
	}
	
	public String getStringWithString(Stats s,String p){
		return getMysql().getString("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE player= '"+p+"'");
	}
	
	public Double getDoubleWithString(Stats s,String p){
		return getMysql().getDouble("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE player= '"+p+"'");
	}
	
	public Integer getIntWithString(Stats s,String p){
		return getMysql().getInt("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE player= '"+p+"'");
	}
	
}
