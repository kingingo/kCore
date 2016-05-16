package eu.epicpvp.kcore.ArchivmentsHandler;

import org.bukkit.event.Listener;

import dev.wolveringer.dataserver.gamestats.StatsKey;
import lombok.Getter;

@Getter
public class Achievement implements Listener{

	private String name;
	private String[] description;
	private int profit=0;
	private int progress;
	private StatsKey key;
	
	public Achievement(String name,String[] description, int profit, int progress){
		this(name,description,profit,progress,StatsKey.MONEY);
	}
	
	public Achievement(String name,String[] description,int profit, int progress, StatsKey key){
		this.name=name;
		this.description=description;
		this.profit=profit;
		this.progress=progress;
		this.key=key;
	}
	
}
