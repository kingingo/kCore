package me.kingingo.kcore.Util;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.AntiLogout.AntiLogoutManager;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class RestartScheduler implements Listener{
	private int restart=13;
	private int start=35;
	private JavaPlugin instance;
	@Setter
	@Getter
	private StatsManager stats;
	@Setter
	@Getter
	private GildenManager gilden;
	@Getter
	@Setter
	private UserDataConfig userData;
	@Getter
	@Setter
	private Coins coins;
	@Getter
	@Setter
	private Gems gems;
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
		if(anti!=null)anti.setOnDisable(true);
		if(coins!=null)coins.SaveAll();
		if(gems!=null)gems.SaveAll();
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
				if(stats!=null)stats.SaveAllData();	
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
