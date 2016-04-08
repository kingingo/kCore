package eu.epicpvp.kcore.Listener.SkinCatcherListener;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.Callback;
import dev.wolveringer.skin.Skin;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.UpdateAsync.UpdateAsyncType;
import eu.epicpvp.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import eu.epicpvp.kcore.Util.UtilSkin;
import lombok.Getter;

public class SkinCatcherListener extends kListener{

	private long time;
	@Getter
	private HashMap<String,Skin> skins;
	private HashMap<String,Long> skins_time;
	
	public SkinCatcherListener(JavaPlugin instance, long time) {
		super(instance, "SkinCatcherListener");
		this.time=time;
		this.skins=new HashMap<>();
		this.skins_time=new HashMap<>();
		UtilSkin.setCatcher(this);
	}

	ArrayList<String> delete;
	@EventHandler
	public void asncUpdater(UpdateAsyncEvent ev){
		if(ev.getType() == UpdateAsyncType.MIN_16){
			if(delete==null)delete=new ArrayList<>();
			delete.clear();
			for(String s : skins_time.keySet()){
				if(skins_time.get(s) < System.currentTimeMillis()){
					delete.add(s);
				}
			}
			
			for(String s : delete)skins_time.remove(s);
			delete.clear();
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		if(skins_time.containsKey( ev.getPlayer().getName() )){
			skins_time.remove(ev.getPlayer().getName());
		}else{
			UtilSkin.loadSkin(new Callback<Skin>() {
				
				@Override
				public void call(Skin data) {
					skins.put(ev.getPlayer().getName(), data);
				}
			}, ev.getPlayer().getName());
		}
		skins_time.put(ev.getPlayer().getName(), System.currentTimeMillis()+time);
	}
}
