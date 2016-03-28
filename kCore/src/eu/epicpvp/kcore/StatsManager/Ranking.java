package eu.epicpvp.kcore.StatsManager;
import java.sql.ResultSet;
import java.util.HashMap;

import org.bukkit.Bukkit;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.StatsManager.Event.RankingUpdateEvent;
import lombok.Getter;

public class Ranking {

	@Getter
	public StatsKey stats;
	@Getter
	public long time;
	private MySQL mysql;
	@Getter
	private HashMap<Integer,PlayerRank> ranking;
	@Getter
	private int length;
	private StatsManager statsManager;
	
	public Ranking(MySQL mysql,StatsManager statsManager,StatsKey stats,long time,int length){
		this.stats=stats;
		this.ranking= new HashMap<>();
		this.time=time;
		this.length=length;
		this.mysql=mysql;
		this.statsManager=statsManager;
	}
	
	public void load(){
		if(ranking.isEmpty()){
			try{
				 ResultSet rs = mysql.Query("SELECT `"+getStats().getMySQLName()+"`,`player` FROM `users_"+statsManager.getType().getTyp()+"`"+(getTime()==-1?"":"WHERE time<'"+(System.currentTimeMillis()+(time/2))+"' AND time>'"+(System.currentTimeMillis()-(time/2))+"'")+" ORDER BY "+getStats().getMySQLName()+" DESC LIMIT "+getLength()+";");

			      int zahl = 1;

			      while (rs.next()) {
			    	if(ranking.containsKey(zahl)){
			    		ranking.get(zahl).player=rs.getString(2);
			    		if(stats.getType() == int.class){
				    		ranking.get(zahl).stats=Math.round(rs.getInt(1));
			    		}else if(stats.getType() == double.class){
				    		ranking.get(zahl).stats=Math.round(rs.getDouble(1));
			    		}
			    	}else{
			    		if(stats.getType() == int.class){
					        ranking.put(zahl, new PlayerRank(rs.getString(2),Math.round(rs.getInt(1))));
			    		}else if(stats.getType() == double.class){
					        ranking.put(zahl, new PlayerRank(rs.getString(2),Math.round(rs.getDouble(1))));
			    		}
			    	}
			        zahl++;
			      }
			      rs.close();
			      Bukkit.getPluginManager().callEvent(new RankingUpdateEvent(statsManager,this));
			 } catch (Exception err) {
			      System.out.println("MySQL-Error: " + err.getMessage());
			 }
		}
	}
	
}