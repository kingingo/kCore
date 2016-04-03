package eu.epicpvp.kcore.StatsManager;
import org.bukkit.entity.Player;

import dev.wolveringer.client.Callback;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.dataserver.protocoll.packets.PacketOutTopTen;
import dev.wolveringer.dataserver.protocoll.packets.PacketOutTopTen.RankInformation;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class Ranking {

	@Getter
	public StatsKey stats;
	@Getter
	public GameType type;
	@Getter
	private RankInformation[] ranking;
	
	public Ranking(GameType type,StatsKey stats){
		this.stats=stats;
		this.type=type;
	}
	

	public void load(){
		load(null);
	}
	
	public void load(Callback<Object> call){
		UtilServer.getClient().getTopTen(getType(), getStats()).getAsync(new Callback<PacketOutTopTen>() {
			
			@Override
			public void call(PacketOutTopTen packet) {
				if(packet != null){
					ranking=packet.getRanks();
					if(call!=null)call.call(null);
				}
			}
		});
	}
	
}