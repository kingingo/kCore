package me.kingingo.kcore.PlayerStats;

import java.sql.ResultSet;
import java.util.HashMap;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsManager{

	JavaPlugin plugin;
	MySQL mysql;
	GameType typ;
	HashMap<Player,HashMap<Stats,Object>> list = new HashMap<>(); 
	
	public StatsManager(JavaPlugin plugin,MySQL mysql,GameType typ){
		this.plugin=plugin;
		this.mysql=mysql;
		this.typ=typ;
		CreateTable();
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
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
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
		for(Player p : list.keySet()){
			for(Stats s : list.get(p).keySet()){
				if(list.get(p).get(s) instanceof Integer){
					UpdatePlayer(p, s,((Integer)list.get(p).get(s)) );
				}else if(list.get(p).get(s) instanceof String){
					UpdatePlayer(p, s,((String)list.get(p).get(s)) );
				}
			}
		}
	}
	
	public void SaveAllPlayerData(Player p){
		for(Stats s : list.get(p).keySet()){
			if(list.get(p).get(s) instanceof Integer){
				UpdatePlayer(p, s,((Integer)list.get(p).get(s)) );
			}else if(list.get(p).get(s) instanceof String){
				UpdatePlayer(p, s,((String)list.get(p).get(s)) );
			}
		}
	}
	
	public void UpdatePlayer(Player p,Stats s,String i){
		ExistPlayer(p);
		mysql.Update("UPDATE users_"+typ.getKürzel()+" SET "+s.getTYP()+"='"+i+"' WHERE player='" + p.getName() + "'");
	}
	
	public void UpdatePlayer(Player p,Stats s,int i){
		ExistPlayer(p);
		mysql.Update("UPDATE users_"+typ.getKürzel()+" SET "+s.getTYP()+"='"+i+"' WHERE player='" + p.getName() + "'");
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
		System.err.println("ADD: "+t);
		mysql.Update(t);
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
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
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
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
	    }
		
		list.get(p).put(Stats.RANKING, n);
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
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
		}
		
		list.get(p).put(s, i);
		return i;
	}
	
	public void setString(Player p,String i,Stats s){
		ExistPlayer(p);
		list.get(p).put(s, i);
	}
	
	public void setInt(Player p,int i,Stats s){
		ExistPlayer(p);
		list.get(p).put(s, i);
	}
	
	public Integer getInt(Stats s,Player p){
		ExistPlayer(p);
		if(list.containsKey(p)&&list.get(p).containsKey(s)){
			return (Integer)list.get(p).get(s);
		}
		
		int i = 0;
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE player= '"+p.getName()+"'");
			while(rs.next()){
				i=rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
		}
		
		list.get(p).put(s, i);
		return i;
	}
	
	public Integer getIntWithString(Stats s,String p){
		int i = 0;
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE player= '"+p+"'");
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
