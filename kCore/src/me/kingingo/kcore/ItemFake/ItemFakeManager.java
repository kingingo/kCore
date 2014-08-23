package me.kingingo.kcore.ItemFake;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.ItemFake.Events.ItemFakeCreateEvent;
import me.kingingo.kcore.ItemFake.Events.ItemFakeDestroyEvent;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemFakeManager implements Listener {

	@Getter
	private ArrayList<ItemFake> list = new ArrayList<>();
	@Getter
	private JavaPlugin instance;
	@Getter
	private Hologram hm;
	@Getter
	@Setter
	private double distance;
	@Getter
	private HashMap<Player,ArrayList<NameTagMessage>> players = new HashMap<>();
	private NameTagMessage ntm;
	private ArrayList<NameTagMessage> ntmlist;
	
	public ItemFakeManager(JavaPlugin instance,Hologram hm){
		this.instance=instance;
		this.hm=hm;
		Bukkit.getPluginManager().registerEvents(this, instance);
	}
	
	@EventHandler
	public void RemoveHoloGram(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC_2)return;
		for(Player p : getPlayers().keySet()){
			ntmlist = getPlayers().get(p);
			if(ntmlist.isEmpty())continue;
			for(int i = 0; i < ntmlist.size(); ++i) {
				if(ntmlist.get(i).getLocation().distance(p.getLocation()) > getDistance()){
					ntmlist.get(i).clear(p);
					ntmlist.remove(i);
				}
			}
			
		}
	}
	
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FAST)return;
		for(ItemFake i : list ){
			if(!i.getItemStack().hasItemMeta())continue;
			if(!i.getItemStack().getItemMeta().hasDisplayName())continue;
			for(Player p : UtilServer.getPlayers()){
				if(!i.getLocation().getWorld().getName().equalsIgnoreCase(p.getWorld().getName()))continue;
				if(i.getLocation().distance(p.getLocation()) <= getDistance()){
					ntm = hm.sendText(p, i.getLocation().add(0,1,0), i.getItemStack().getItemMeta().getDisplayName());
					if(!players.containsKey(p))players.put(p, new ArrayList<NameTagMessage>());
					players.get(p).add(ntm);
				}
			}
		}
	}
	
	@EventHandler
	public void Create(ItemFakeCreateEvent ev){
		list.add(ev.getItemfake());
	}
	
	@EventHandler
	public void Remove(ItemFakeDestroyEvent ev){
		list.remove(ev.getItemfake());
	}
	
}
