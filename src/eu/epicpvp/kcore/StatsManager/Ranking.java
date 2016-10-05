package eu.epicpvp.kcore.StatsManager;
import eu.epicpvp.dataserver.protocoll.packets.PacketOutTopTen;
import eu.epicpvp.dataserver.protocoll.packets.PacketOutTopTen.RankInformation;
import eu.epicpvp.datenclient.client.Callback;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameType;
import eu.epicpvp.datenserver.definitions.dataserver.gamestats.StatsKey;
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
		if(UtilServer.getClient().getHandle().isConnected()){
			UtilServer.getClient().getTopTen(getType(), getStats()).getAsync(new Callback<PacketOutTopTen>() {

				@Override
				public void call(PacketOutTopTen packet, Throwable exception) {
					if(packet != null){
						ranking=packet.getRanks();
						if(call!=null)call.call(null,null);
					}
				}
			});
		}
	}

}
