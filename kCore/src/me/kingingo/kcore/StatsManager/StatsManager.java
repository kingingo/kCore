package me.kingingo.kcore.StatsManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.MySQL.Callback;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.StatsManager.Event.PlayerStatsChangeEvent;
import me.kingingo.kcore.StatsManager.Event.PlayerStatsCreateEvent;
import me.kingingo.kcore.StatsManager.Event.PlayerStatsLoadedEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilNumber;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsManager extends kListener{

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
	private ArrayList<Ranking> rankings;
	@Getter
	@Setter
	private boolean async=false;
	private String types;
	private StatsManager statsManager;
	
	public StatsManager(JavaPlugin plugin,MySQL mysql,GameType typ){
		super(plugin,"StatsManager");
		this.statsManager=this;
		this.plugin=plugin;
		this.mysql=mysql;
		this.typ=typ;
		this.rankings=new ArrayList<>();
		CreateTable();
		
		Stats[] stats = typ.getStats();
		String tt = "";
		for(Stats s : stats){
			if(s.isMysql()){
				tt=tt+s.getTYP()+",";
			}
		}
		this.types=tt.substring(0, tt.length()-1);
	}
	
	public void loadPlayerStats(Player player){
		Stats[] stats = typ.getStats();
		
		if(isAsync()){
			asyncExistPlayer(player, new Callback(){
				
				@Override
				public void done(Object value) {
					if(value instanceof Boolean){
						if(((Boolean)value)){
							mysql.asyncQuery("SELECT "+types+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+UtilPlayer.getRealUUID(player)+"' LIMIT 1;", new Callback() {
								
								@Override
								public void done(Object value) {
									if(value instanceof ResultSet){
										try {
											ResultSet rs = (ResultSet)value;
											for(Stats s : stats){
												if(s.isMysql()){
													list.get(player).put(s, rs.getObject(s.getTYP()));
												}
											}
											Bukkit.getPluginManager().callEvent(new PlayerStatsLoadedEvent(statsManager, player));
										} catch (SQLException e) {
											Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,getMysql()));
										}
									}
								}
							});
						}
					}
				}
				
			});
		}else{
			ExistPlayer(player);
			try{
				ResultSet rs = mysql.Query("SELECT "+types+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+UtilPlayer.getRealUUID(player)+"' LIMIT 1;");
				while(rs.next()){
					for(Stats s : stats){
						if(s.isMysql()){
							list.get(player).put(s, rs.getObject(s.getTYP()));
						}
					}
				}
				rs.close();
				Bukkit.getPluginManager().callEvent(new PlayerStatsLoadedEvent(statsManager, player));
			}catch (Exception err){
				Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
			}
		}
	}
	
	public void addRanking(Ranking ranking){
		rankings.add(ranking);
	}
	
	@EventHandler
	public void Ranking(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_08)return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(Ranking ranking : rankings)ranking.load();
			}
		});
	}
	
	public void SendRankingMessage(Player player,Ranking ranking,String Zeitraum){
		if(ranking.getRanking().isEmpty())ranking.load();
		player.sendMessage("§b■■■■■■■■§6 §lPlayer Ranking | "+Zeitraum+" | Top "+ranking.getLength()+" §b■■■■■■■■");
		player.sendMessage("§b Platz | "+(ranking.getStats().getKÜRZEL().equalsIgnoreCase("elo") ? "FAME" : ranking.getStats().getKÜRZEL())+" | Player");
		for(Integer i : ranking.getRanking().keySet())player.sendMessage("§b#§6" + String.valueOf(i) + "§b | §6" + String.valueOf(ranking.getRanking().get(i).stats) + " §b|§6 " +ranking.getRanking().get(i).player);
	}
	
	public void SendRankingMessage(Player player,Ranking ranking){
		if(ranking.getRanking().isEmpty())ranking.load();
		player.sendMessage("§b■■■■■■■■§6 §lPlayer Ranking | Top "+ranking.getLength()+" §b■■■■■■■■");
		player.sendMessage("§b Platz | "+(ranking.getStats().getKÜRZEL().equalsIgnoreCase("elo") ? "FAME" : ranking.getStats().getKÜRZEL())+" | Player");
		for(Integer i : ranking.getRanking().keySet())player.sendMessage("§b#§6" + String.valueOf(i) + "§b | §6" + String.valueOf(ranking.getRanking().get(i).stats) + " §b|§6 " +ranking.getRanking().get(i).player);
	}

	public String[] getRankingMessage(Ranking ranking,String Zeitraum){
		if(ranking.getRanking().isEmpty())ranking.load();
		String[] r = new String[2+ranking.getRanking().size()];
		r[0]="§6§lPlayer Ranking | "+Zeitraum+" | Top "+ranking.getLength();
		r[1]="§b Platz | "+(ranking.getStats().getKÜRZEL().equalsIgnoreCase("elo") ? "FAME" : ranking.getStats().getKÜRZEL())+" | Player";
		int a = 1;
		for(Integer i : ranking.getRanking().keySet()){
			a++;
			r[a]="§b#§6" + String.valueOf(i) + "§b | §6" + String.valueOf(ranking.getRanking().get(i).stats) + " §b|§6 " +ranking.getRanking().get(i).player;
		}
		return r;
	}
	
	public String[] getRankingMessage(Ranking ranking){
		if(ranking.getRanking().isEmpty())ranking.load();
		String[] r = new String[2+ranking.getRanking().size()];
		r[0]="§6§lPlayer Ranking | Top "+ranking.getLength();
		r[1]="§b Platz | "+(ranking.getStats().getKÜRZEL().equalsIgnoreCase("elo") ? "FAME" : ranking.getStats().getKÜRZEL())+" | Player";
		int a = 1;
		for(Integer i : ranking.getRanking().keySet()){
			a++;
			r[a]="§b#§6" + String.valueOf(i) + "§b | §6" + String.valueOf(ranking.getRanking().get(i).stats) + " §b|§6 " +ranking.getRanking().get(i).player;
		}
		return r;
	}
	
	public HashMap<Integer,UUID> getRanking(Stats s,int i){
		HashMap<Integer,UUID> list = new HashMap<>();
	    try
	    {
	      ResultSet rs = mysql.Query("SELECT `uuid` FROM `users_"+typ.getKürzel()+"` ORDER BY "+s.getTYP()+" DESC LIMIT "+i+";");

	      int zahl = 1;
	      while (rs.next()) {
	    	  list.put(zahl, UUID.fromString(rs.getString(1)));
	    	  zahl++;
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
	    return list;
	}
	
	public void mysqlUpdate(String qry){
		if(isAsync()){
			mysql.asyncUpdate(qry);
		}else{
			mysql.Update(qry);
		}
	}
	
	public void CreateTable(){
		Stats[] stats = typ.getStats();
		String tt = "player varchar(30),UUID varchar(100),";
		for(Stats s : stats){
			tt=tt+s.getCREATE()+",";
		}
		String t = "CREATE TABLE IF NOT EXISTS users_"+typ.getKürzel()+"("+tt.substring(0, tt.length()-1)+")";
		mysqlUpdate(t);
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
					mysqlUpdate("UPDATE users_"+typ.getKürzel()+" SET "+stats.getTYP()+"='"+ ((Integer)list.get(player).get(stats)) +"' WHERE UUID='" + UtilPlayer.getRealUUID(player) + "'");
				}else if(list.get(player).get(stats) instanceof String){
					mysqlUpdate("UPDATE users_"+typ.getKürzel()+" SET "+stats.getTYP()+"='"+ ((String)list.get(player).get(stats)) +"' WHERE UUID='" + UtilPlayer.getRealUUID(player) + "'");
				}else if(list.get(player).get(stats) instanceof Double){
					mysqlUpdate("UPDATE users_"+typ.getKürzel()+" SET "+stats.getTYP()+"='"+ ((Double)list.get(player).get(stats)) +"' WHERE UUID='" + UtilPlayer.getRealUUID(player) + "'");
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
				mysqlUpdate("UPDATE users_"+typ.getKürzel()+" SET "+st.getTYP()+"='"+ ((Integer)list.get(p).get(st)) +"' WHERE UUID='" + UtilPlayer.getRealUUID(p) + "'");
			}else if(list.get(p).get(st) instanceof String){
				mysqlUpdate("UPDATE users_"+typ.getKürzel()+" SET "+st.getTYP()+"='"+ ((String)list.get(p).get(st)) +"' WHERE UUID='" + UtilPlayer.getRealUUID(p) + "'");
			}else if(list.get(p).get(st) instanceof Double){
				mysqlUpdate("UPDATE users_"+typ.getKürzel()+" SET "+st.getTYP()+"='"+ ((Double)list.get(p).get(st)) +"' WHERE UUID='" + UtilPlayer.getRealUUID(p) + "'");
			}
		}
		list.remove(p);
	}
	
	public void UpdatePlayer(Player p,Stats s,String i){
		if(!s.isMysql())return;
		ExistPlayer(p);
		mysqlUpdate("UPDATE users_"+typ.getKürzel()+" SET "+s.getTYP()+"='"+i+"' WHERE UUID='" + UtilPlayer.getRealUUID(p) + "'");
		list.get(p).remove(s);
	}
	
	public void UpdatePlayer(Player p,Stats s,int i){
		if(!s.isMysql())return;
		ExistPlayer(p);
		mysqlUpdate("UPDATE users_"+typ.getKürzel()+" SET "+s.getTYP()+"='"+i+"' WHERE UUID='" + UtilPlayer.getRealUUID(p) + "'");
		list.get(p).remove(s);
	}
	
	public void deleteEintrag(Player p){
		mysqlUpdate("DELETE FROM users_"+typ.getKürzel()+" WHERE UUID='"+UtilPlayer.getRealUUID(p)+"';");
	}
	
	public void createEintrag(Player p){
		Stats[] stats = typ.getStats();
		String tt = "player,UUID,";
		String ti = "'"+p.getName()+"','"+UtilPlayer.getRealUUID(p)+"',";
		for(Stats s : stats){
			tt=tt+s.getTYP()+",";
			ti=ti+"'0',";
		}
		String t = "INSERT INTO users_"+typ.getKürzel()+" ("+tt.substring(0, tt.length()-1)+") VALUES ("+ti.subSequence(0, ti.length()-1)+");";
		mysqlUpdate(t);
		if(!list.containsKey(p))list.put(p, new HashMap<>());
		Bukkit.getPluginManager().callEvent(new PlayerStatsCreateEvent(this,p));
		Bukkit.getPluginManager().callEvent(new PlayerStatsLoadedEvent(statsManager, p));
	}
	
	public boolean ExistPlayer(Player p){
		boolean done = false;
		if(list.containsKey(p))return true;
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `player` FROM `users_"+typ.getKürzel()+"` WHERE UUID='"+UtilPlayer.getRealUUID(p)+"'");

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
	
	public void asyncExistPlayer(Player player, Callback callback){
		if(list.containsKey(player)){
			callback.done(true);
		}else{
			mysql.asyncQuery("SELECT `player` FROM `users_"+typ.getKürzel()+"` WHERE UUID='"+UtilPlayer.getRealUUID(player)+"'", new Callback() {
				
				@Override
				public void done(Object value) {
					list.put(player, new HashMap<Stats,Object>());
					callback.done(true);
				}
			},new Callback() {
				
				@Override
				public void done(Object value) {
					createEintrag(player);
					callback.done(false);
				}
			});
		}
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
	      ResultSet rs = mysql.Query("SELECT `UUID` FROM `users_"+typ.getKürzel()+"` ORDER BY `"+s.getTYP()+"` DESC;");

	      while ((rs.next()) && (!done)) {
	        n++;
	        if (UUID.fromString(rs.getString(1)).equals(UtilPlayer.getRealUUID(p))) {
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
	
	public void getAsyncString(Stats s,Player p,Callback callback){
		if(list.containsKey(p)&&list.get(p).containsKey(s)){
			callback.done((String)list.get(p).get(s));
		}else{
			mysql.asyncGetDouble("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+UtilPlayer.getRealUUID(p)+"'", new Callback() {
				
				@Override
				public void done(Object value) {
					if(value instanceof String){
						list.get(p).put(s, ((String)value));
						callback.done((String)list.get(p).get(s));
					}
				}
			});
		}
	}
	
	public String getString(Stats s,Player p){	
		ExistPlayer(p);
		if(list.containsKey(p)&&list.get(p).containsKey(s)){
			return (String)list.get(p).get(s);
		}
		
		String i = "";
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+UtilPlayer.getRealUUID(p)+"'");
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
	
	public boolean ExistPlayer(UUID uuid){
		boolean done = false;
		if(UtilPlayer.isOnline(uuid) && list.containsKey(Bukkit.getPlayer(uuid)))return true;
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `player` FROM `users_"+typ.getKürzel()+"` WHERE UUID='"+uuid+"'");

	      while (rs.next()) {
	    	  done=true;
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		
		if(UtilPlayer.isOnline(uuid)){
			list.put(Bukkit.getPlayer(uuid), new HashMap<Stats,Object>());
			if(!done)createEintrag(Bukkit.getPlayer(uuid));
		}
		return done;
	}
	
	public int addInt(UUID uuid,int i,Stats s){
		i+=getInt(s, uuid);
		return setInt(uuid, i, s);
	}
	
	public int setInt(UUID uuid,int i,Stats s){
		ExistPlayer(uuid);
		if(UtilPlayer.isOnline(uuid)&&list.containsKey(Bukkit.getPlayer(uuid))){
			list.get(Bukkit.getPlayer(uuid)).put(s, i);
			Bukkit.getPluginManager().callEvent(new PlayerStatsChangeEvent(s,Bukkit.getPlayer(uuid)));
		}else{
			mysqlUpdate("UPDATE users_"+typ.getKürzel()+" SET "+s.getTYP()+"='"+ i +"' WHERE UUID='" + uuid + "'");
		}
		return i;
	}
	
	public Integer getInt(Stats s,UUID uuid){
		ExistPlayer(uuid);
		if(UtilPlayer.isOnline(uuid) && list.containsKey(Bukkit.getPlayer(uuid))&&list.get(Bukkit.getPlayer(uuid)).containsKey(s)){
			return (Integer)list.get(Bukkit.getPlayer(uuid)).get(s);
		}
		
		int i = -1;
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+uuid+"'");
			while(rs.next()){
				i=rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
		}
		
		if(UtilPlayer.isOnline(uuid) && i!=-1 && list.containsKey(Bukkit.getPlayer(uuid)))list.get(Bukkit.getPlayer(uuid)).put(s, i);
		
		return i;
	}
	
	public void setString(Player p,String i,Stats s){
		ExistPlayer(p);
		list.get(p).put(s, i);
		Bukkit.getPluginManager().callEvent(new PlayerStatsChangeEvent(s,p));
	}

	public void delInt(Player p,int i,Stats s){
		i-=getInt(s, p);
		setInt(p, i, s);
	}
	
	public void addInt(Player p,int i,Stats s){
		i+=getInt(s, p);
		setInt(p, i, s);
	}
	
	public void setInt(Player p,int i,Stats s){
		ExistPlayer(p);
		list.get(p).put(s, i);
		Bukkit.getPluginManager().callEvent(new PlayerStatsChangeEvent(s,p));
	}
	
	public void addDouble(Player p,double i,Stats s){
		i+=getDouble(s, p);
		setDouble(p, i, s);
	}
	
	public void delDouble(Player p,double i,Stats s){
		i-=getDouble(s, p);
		setDouble(p, i, s);
	}
	
	public void getAsyncInt(Stats s,Player p,Callback callback){
		if(list.containsKey(p)&&list.get(p).containsKey(s)){
			callback.done(UtilNumber.toInt(list.get(p).get(s)));
		}else{
			mysql.asyncGetDouble("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+UtilPlayer.getRealUUID(p)+"'", new Callback() {
				
				@Override
				public void done(Object value) {
					if(value instanceof Integer){
						list.get(p).put(s, ((Integer)value));
						callback.done(UtilNumber.toInt(list.get(p).get(s)));
					}
				}
			});
		}
	}
	
	public Integer getInt(Stats s,Player p){
		ExistPlayer(p);
		if(list.containsKey(p)&&list.get(p).containsKey(s)){
			return (Integer)list.get(p).get(s);
		}
		
		int i = -1;
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+UtilPlayer.getRealUUID(p)+"'");
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
	
	public void setDouble(Player p,double i,Stats s){
		ExistPlayer(p);
		list.get(p).put(s, i);
		Bukkit.getPluginManager().callEvent(new PlayerStatsChangeEvent(s,p));
	}
	

	public void getAsyncDouble(Stats s,Player p,Callback callback){
		if(list.containsKey(p)&&list.get(p).containsKey(s)){
			callback.done(UtilNumber.toDouble(list.get(p).get(s)));
		}else{
			mysql.asyncGetDouble("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+UtilPlayer.getRealUUID(p)+"'", new Callback() {
				
				@Override
				public void done(Object value) {
					if(value instanceof Double){
						list.get(p).put(s, ((Double)value));
						callback.done(UtilNumber.toDouble(list.get(p).get(s)));
					}
				}
			});
		}
	}
	
	public double getDouble(Stats s,Player p){
		ExistPlayer(p);
		if(list.containsKey(p)&&list.get(p).containsKey(s)){
			return UtilNumber.toDouble(list.get(p).get(s));
		}
		
		double i = -1;
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+UtilPlayer.getRealUUID(p)+"'");
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
	
	public void asyncExistPlayer(String player,Callback callback){
		mysql.asyncGetString("SELECT `player` FROM `users_"+typ.getKürzel()+"` WHERE UUID='"+UtilPlayer.getUUID(player, mysql)+"'", callback);
	}
	
	public boolean ExistPlayer(String player){
		if(list.containsKey(player))return true;
		return !mysql.getString("SELECT `player` FROM `users_"+typ.getKürzel()+"` WHERE UUID='"+UtilPlayer.getUUID(player, mysql)+"'").equalsIgnoreCase("null");
	}
	
	public void getAsyncName(UUID uuid,Callback callback){
		mysql.asyncGetString("SELECT `player` FROM `users_"+typ.getKürzel()+"` WHERE UUID='"+uuid+"'",callback);
	}
	
	public String getName(UUID uuid){
		return mysql.getString("SELECT `player` FROM `users_"+typ.getKürzel()+"` WHERE UUID='"+uuid+"'");
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
	
	public String getStringWithUUID(Stats s,UUID p){
		return getMysql().getString("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+p+"'");
	}
	
	public Double getDoubleWithUUID(Stats s,UUID p){
		return getMysql().getDouble("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+p+"'");
	}
	
	public Integer getIntWithUUID(Stats s,UUID p){
		return getMysql().getInt("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+p+"'");
	}
	
	public String getStringWithUUID(Stats s,String p){
		return getMysql().getString("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+UtilPlayer.getUUID(p, mysql)+"'");
	}
	
	public Double getDoubleWithUUID(Stats s,String p){
		return getMysql().getDouble("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+UtilPlayer.getUUID(p, mysql)+"'");
	}
	
	public Integer getIntWithUUID(Stats s,String p){
		return getMysql().getInt("SELECT "+s.getTYP()+" FROM users_"+typ.getKürzel()+" WHERE UUID= '"+UtilPlayer.getUUID(p, mysql)+"'");
	}
	
}
