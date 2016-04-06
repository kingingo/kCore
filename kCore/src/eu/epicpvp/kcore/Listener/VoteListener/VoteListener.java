package eu.epicpvp.kcore.Listener.VoteListener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.connection.PacketListener;
import dev.wolveringer.dataserver.protocoll.packets.Packet;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Packets.PacketPlayerVote;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class VoteListener extends kListener{

	@Getter
	private ArrayList<String> players;
	@Getter
	private Callback<String> callback;
	@Getter
	private JavaPlugin instance;
	
	public VoteListener(JavaPlugin instance,boolean offline, Callback<String> callback) {
		super(instance, "VoteListener");
		this.instance=instance;
		this.callback=callback;
		if(offline)this.players=new ArrayList<>();
		
		if(Bukkit.getPluginManager().getPlugin("Votifier")!=null){
			new VotifierListener(this);
		}
		
		PacketPlayerVote.register();
		UtilServer.getClient().getHandle().getHandlerBoss().addListener(new PacketListener() {
			
			@Override
			public void handle(Packet packet) {
				if(packet instanceof PacketPlayerVote){
					PacketPlayerVote vote = (PacketPlayerVote)packet;
					
					if(getPlayers()!=null){
						if(!UtilPlayer.isOnline(vote.getPlayername())){
							getPlayers().add(vote.getPlayername());
							return;
						}
					}
					callback.call(vote.getPlayername());
				}
			}
		});
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		if(this.players!=null){
			if(this.players.contains(ev.getPlayer().getName().toLowerCase())){
				this.players.remove(ev.getPlayer().getName().toLowerCase());
				callback.call(ev.getPlayer().getName());
			}
		}
	}
}

