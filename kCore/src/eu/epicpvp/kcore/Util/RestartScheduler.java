package eu.epicpvp.kcore.Util;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.AntiLogout.AntiLogoutManager;
import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import lombok.Getter;
import lombok.Setter;

public class RestartScheduler implements Listener{
	private int restart=13;
	private int start=35;
	private JavaPlugin instance;
	@Setter
	@Getter
	private StatsManager money;
	@Setter
	@Getter
	private StatsManager stats;
	@Setter
	@Getter
	private GildenManager gilden;
	@Getter
	@Setter
	private UserDataConfig userData;
	@Setter
	@Getter
	private AntiLogoutManager anti;
	private int i=0;
	
	public RestartScheduler(JavaPlugin instance){
		this.instance=instance;
	}
	
	public void start(){
		if(gilden!=null)gilden.setOnDisable(true);
		if(stats!=null)stats.setOnDisable(true);
		if(money!=null)money.setOnDisable(true);
		if(anti!=null)anti.setOnDisable(true);
		Bukkit.getPluginManager().registerEvents(this, instance);
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist on");
	}
	
	public void broadcast(String name,Object input){
		System.out.println(Language.getText(name, input));
		UtilServer.broadcastLanguage(name, input);
	}
	
	Title title;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		
		if(start>=-1){
			start--;
			
			if(title==null){
				this.title=new Title("§cServer Restarting in", "");
				this.title.setStayTime(20*60*2);
			}
			title.setSubtitle("§7"+UtilTime.formatSeconds(start));
			
			if(start>0){
				for(Player p : UtilServer.getPlayers()){
					title.send(p);
				}
			}
			
			switch(start){
			case 30:broadcast("RESTART_IN",start);break;
			case 25:broadcast("RESTART_IN",start);break;
			case 20:broadcast("RESTART_IN",start);break;
			case 15:broadcast("RESTART_IN",start);break;
			case 10:broadcast("RESTART_IN",start);break;
			case 9:broadcast("RESTART_IN",start);break;
			case 8:broadcast("RESTART_IN",start);break;
			case 7:broadcast("RESTART_IN",start);break;
			case 6:broadcast("RESTART_IN",start);break;
			case 5:broadcast("RESTART_IN",start);break;
			case 4:broadcast("RESTART_IN",start);break;
			case 3:broadcast("RESTART_IN",start);break;
			case 2:broadcast("RESTART_IN",start);break;
			case 1:broadcast("RESTART_IN",start);break;
			case 0:broadcast("RESTART_IN",start);break;
			case -1:
				if( UtilServer.getPlayers().size()>0 ){
					for(Player player : UtilServer.getPlayers())UtilBG.sendToServer(player, this.instance);
					start=-1;
				}
				break;
			}
		}else{
			restart--;
			switch(restart){
			case 10:
				if(stats!=null)stats.saveAll();
				if(money!=null)money.saveAll();
				if(gilden!=null)gilden.AllUpdateGilde();
				if(userData!=null)userData.saveAllConfigs();
				break;
			case 5:for(World world : Bukkit.getWorlds())world.save();break;
			case 0:
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist off");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
				break;
			}
		}
		
	}
	
}
