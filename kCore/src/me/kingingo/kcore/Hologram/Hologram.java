package me.kingingo.kcore.Hologram;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Hologram implements Listener{
	
	@Getter
	private HashMap<NameTagMessage, Integer> list = new HashMap<NameTagMessage,Integer>();
	@Setter
	@Getter
	private boolean protocollib=false;
	
	public Hologram(JavaPlugin plugin){
		if(Bukkit.getPluginManager().getPlugin("ProtocolLib")==null)this.protocollib=true;
		if(!this.protocollib)Bukkit.getPluginManager().registerEvents(this, plugin);
		System.out.println("PROTOCOL: "+this.protocollib);
	}
	
	HashMap<NameTagMessage,Integer> clone;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC)return;
		if(this.protocollib)return;
		if(this.list.isEmpty())return;
		clone=(HashMap<NameTagMessage,Integer>)list.clone();
		for(NameTagMessage message : clone.keySet()){
			if(this.clone.get(message)==-1);
			if(this.clone.get(message)>0){
				this.list.put(message, this.clone.get(message)-1);
			}else{
				this.list.remove(message);
				for(Player p : UtilServer.getPlayers())message.clear(p);
			}
		}
	}
	
	public void RemoveText(Player p){
		for(NameTagMessage message : list.keySet()){
			message.clear(p);
		}
	}
	
	public void RemoveAllText(){
		for(Player p : UtilServer.getPlayers()){
			for(NameTagMessage message : list.keySet()){
				message.clear(p);
			}
		}
		list.clear();
	}
	
	//--
	public void sendText(final Player p, Location loc,int time, String... msg) {
		if(this.protocollib)return;
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			message.sendToPlayer(p, loc);
			list.put(message, time);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	public void sendText(final Player p, Location loc, String... msg) {
		if(this.protocollib)return;
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			message.sendToPlayer(p, loc);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	public void sendText(final Player p, Location loc,int time, String msg) {
		if(this.protocollib)return;
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			message.sendToPlayer(p, loc);
			list.put(message, time);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	public void sendText(final Player p, Location loc, String msg) {
		if(this.protocollib)return;
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			message.sendToPlayer(p, loc);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	//--

	public void sendTextAll(Location loc, String... msg) {
		if(this.protocollib)return;
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			for (Player ps : Bukkit.getOnlinePlayers())
				message.sendToPlayer(ps, loc);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	public void sendTextAll(Location loc, String msg) {
		if(this.protocollib)return;
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			for (Player ps : Bukkit.getOnlinePlayers())
				message.sendToPlayer(ps, loc);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	public void sendTextAll(Location loc,int time, String... msg) {
		if(this.protocollib)return;
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			list.put(message, time);
			for (Player ps : Bukkit.getOnlinePlayers())
				message.sendToPlayer(ps, loc);
			
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
	public void sendTextAll(Location loc,int time, String msg) {
		if(this.protocollib)return;
		try {
			final NameTagMessage message = new NameTagMessage(msg);
			list.put(message, time);
			for (Player ps : Bukkit.getOnlinePlayers())
				message.sendToPlayer(ps, loc);
		} catch (Exception error) {
			error.printStackTrace();
		}
	}
	
}
