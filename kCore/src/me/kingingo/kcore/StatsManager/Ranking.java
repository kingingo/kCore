package me.kingingo.kcore.StatsManager;

import java.sql.ResultSet;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Listener.kListener;

public class Ranking extends kListener{

	@Getter
	public Stats stats;
	@Getter
	public long time;
	private StatsManager statsManager;
	@Getter
	private HashMap<Integer,PlayerRank> ranking;
	@Getter
	private int length;
	
	public Ranking(StatsManager statsManager,Stats stats,long time,int length){
		super(statsManager.getMysql().getInstance(),"Ranking:"+stats.getKÜRZEL());
		this.stats=stats;
		this.ranking= new HashMap<>();
		this.time=time;
		this.length=length;
		this.statsManager=statsManager;
	}
	
	public void load(){
		if(ranking.isEmpty()){
			try{
			     ResultSet rs = statsManager.getMysql().Query("SELECT `"+getStats().getTYP()+"`,`player` FROM `users_"+statsManager.getTyp().getKürzel()+"`"+(getTime()==-1?"":"WHERE time<'"+(System.currentTimeMillis()+(time/2))+"' AND time>'"+(System.currentTimeMillis()-(time/2))+"'")+" ORDER BY "+getStats().getTYP()+" DESC LIMIT "+getLength()+";");

			      int zahl = 1;
			      
			      while (rs.next()) {
			    	if(ranking.containsKey(zahl)){
			    		ranking.get(zahl).player=rs.getString(2);
			    		
			    		if(stats.getCREATE().contains("int")){
				    		ranking.get(zahl).stats=rs.getInt(1);
			    		}else if(stats.getCREATE().contains("double")){
				    		ranking.get(zahl).stats=rs.getDouble(1);
			    		}
			    	}else{
			    		if(stats.getCREATE().contains("int")){
					        ranking.put(zahl, new PlayerRank(rs.getString(2),rs.getInt(1)));
			    		}else if(stats.getCREATE().contains("double")){
					        ranking.put(zahl, new PlayerRank(rs.getString(2),rs.getDouble(1)));
			    		}
			    	}
			        zahl++;
			      }

			      rs.close();
			 } catch (Exception err) {
			      System.out.println("MySQL-Error: " + err.getMessage());
			 }
		}
	}
	
}
