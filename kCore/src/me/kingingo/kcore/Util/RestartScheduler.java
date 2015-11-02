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
	}
	
	public void broadcast(String name,Object input){
		System.out.println(Language.getText(name, input));
		UtilServer.broadcastLanguage(name, input);
	}
	
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		start--;
		for(Player p : UtilServer.getPlayers())UtilDisplay.displayTextBar(p, Language.getText(p, "RESTART_IN",start));
		
		switch(start){
		case 30:
			broadcast("RESTART_IN",start);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist on");
			break;
		case 25:
			broadcast("RESTART_IN",start);
			for(Player p : UtilServer.getPlayers())UtilBG.sendToServer(p,instance);
			break;
		case 23:
			if(UtilServer.getPlayers().size()!=0&&i<5){
				start=26;
				i++;
			}
			break;
		case 20:
			if(stats!=null)stats.SaveAllData();	
			if(gilden!=null)gilden.AllUpdateGilde();
			if(userData!=null)userData.saveAllConfigs();
			break;
		case 10:broadcast("RESTART_IN",start);break;
		case 5:
			for(World world : Bukkit.getWorlds())world.save();
			break;
		case 4:broadcast("RESTART_IN",start);break;
		case 3:broadcast("RESTART_IN",start);break;
		case 2:broadcast("RESTART_IN",start);break;
		case 1:broadcast("RESTART_IN",start);break;
		case 0: 
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist off");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			break;
		}
	}
	
}
