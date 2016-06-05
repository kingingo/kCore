package eu.epicpvp.kcore.Listener.SkinCatcherListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.Callback;
import dev.wolveringer.skin.Skin;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.Events.PlayerLoadPermissionEvent;
import eu.epicpvp.kcore.UpdateAsync.UpdateAsyncType;
import eu.epicpvp.kcore.UpdateAsync.Event.UpdateAsyncEvent;
import eu.epicpvp.kcore.Util.UtilSkin;
import lombok.Getter;

public class SkinCatcherListener extends kListener{

	private long time;
	@Getter
	private HashMap<UUID, String> tabnames;
	@Getter
	private HashMap<UUID,Skin> skins;
	private HashMap<UUID,Long> skins_time;
	
	public SkinCatcherListener(JavaPlugin instance, long time) {
		super(instance, "SkinCatcherListener");
		this.time=time;
		this.skins=new HashMap<>();
		this.skins_time=new HashMap<>();
		this.tabnames=new HashMap<>();
		UtilSkin.setCatcher(this);
	}

	ArrayList<UUID> delete;
	@EventHandler
	public void asncUpdater(UpdateAsyncEvent ev){
		if(ev.getType() == UpdateAsyncType.MIN_16){
			if(delete==null)delete=new ArrayList<>();
			delete.clear();
			for(UUID s : skins_time.keySet()){
				if(skins_time.get(s) < System.currentTimeMillis()){
					delete.add(s);
				}
			}
			
			for(UUID s : delete){
				skins_time.remove(s);
				tabnames.remove(s);
				skins.remove(s);
			}
			delete.clear();
		}
	}

	@EventHandler
	public void prefix_save(PlayerLoadPermissionEvent ev){
		tabnames.put(ev.getPlayer().getUniqueId(), ev.getPermissionPlayer().getGroups().get(0).getPrefix());
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		if(skins_time.containsKey( ev.getPlayer().getName() )){
			skins_time.remove(ev.getPlayer().getName());
		}else{
			UtilSkin.loadSkin(new Callback<Skin>() {
				
				@Override
				public void call(Skin data, Throwable exception) {
					skins.put(ev.getPlayer().getUniqueId(), data);
				}
			}, ev.getPlayer().getName());
		}
		skins_time.put(ev.getPlayer().getUniqueId(), System.currentTimeMillis()+time);
	}
}
