package eu.epicpvp.kcore.Listener.VoteListener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.VotifierEvent;

import dev.wolveringer.client.connection.ClientType;
import eu.epicpvp.kcore.Packets.PacketPlayerVote;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

class VotifierListener implements Listener{
	
	@Getter
	private VoteListener voteListener;
	
	public VotifierListener(VoteListener voteListener) {
		if(Bukkit.getPluginManager().getPlugin("Votifier")==null)return;
		Bukkit.getPluginManager().registerEvents(this, voteListener.getInstance());
		this.voteListener=voteListener;
	}
	
	@EventHandler
	public void vote(VotifierEvent ev){
		PacketPlayerVote packet = new PacketPlayerVote(ev.getVote().getUsername());
		UtilServer.getClient().sendPacket(ClientType.OTHER, packet);
		
		if(getVoteListener().getPlayers()!=null){
			if(!UtilPlayer.isOnline(ev.getVote().getUsername())){
				getVoteListener().getPlayers().add(ev.getVote().getUsername());
				return;
			}
		}
		if(getVoteListener().getCallback()!=null)getVoteListener().getCallback().call(ev.getVote().getUsername());
		
	}
}