package eu.epicpvp.kcore.newGilde;

import org.bukkit.entity.Player;

import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.client.futures.StatsResponseFuture;
import dev.wolveringer.gilde.GildManager;
import dev.wolveringer.gilde.GildSection;
import dev.wolveringer.gilde.GildeType;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class GildeHandler extends kListener{

	@Getter
	private GildManager gilde;
	private GildeType type;
	
	public GildeHandler(GildeType type){
		super(UtilServer.getPluginInstance(),"GildeHandler");
		this.type=type;
		this.gilde=new GildManager(UtilServer.getClient());
//		UtilServer.setGildeHandler(this);
	}
	
	public GildeType getType(){
		return type;
	}
	
	public boolean areGildeFriends(Player m1, Player m2){
		return areGildeFriends(UtilPlayer.getPlayerId(m1), UtilPlayer.getPlayerId(m2));
	}
	
	public boolean areGildeFriends(int m1, int m2){
		GildSection sec1 = getSection( m1 );
		
		if(sec1!=null){
			return sec1.getPlayers().contains(m2);
		}
		return false;
	}
	
	public boolean hasGilde(Player player){
		return hasGilde(UtilPlayer.getPlayerId(player));
	}
	
	public boolean hasGilde(int playerId){
		return (getSection(playerId)!=null);
	}
	
	public GildSection getSection(LoadedPlayer player){
		return getSection(player.getPlayerId());
	}
	
	public GildSection getSection(Player player){
		return getSection(UtilPlayer.getPlayerId(player));
	}
	
	public GildSection getSection(int playerId){
		GildSection section = getGilde().getGildeSync(UtilServer.getClient().getPlayer(playerId), getType()).getSelection(getType());
		
		if(section.isActive()){
			return section;
		}else{
			return null;
		}
	}
	
}
