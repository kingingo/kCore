package eu.epicpvp.kcore.Listener.MoneyListener;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import eu.epicpvp.kcore.Events.ServerMessageEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsChangeEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilScoreboard;
import eu.epicpvp.kcore.Util.UtilServer;

public class MoneyListener extends kListener{
	
	private StatsManager stats;

	public MoneyListener(StatsManager stats) {
		super(stats.getInstance(), "MoneyListener");
		this.stats=stats;
		UtilServer.setMoneyListener(this);
	}
	
	public void update(String player, StatsKey key, int value){
		if(key!=StatsKey.GEMS&&key!=StatsKey.COINS)throw new NullArgumentException("StatsKey is not Gems or Coins!? ("+key.getMySQLName()+")");
		LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player);
		
		if(UtilPlayer.isOnline(player)){
			stats.add(loadedplayer.getPlayerId(), key, value);
		}else{
			loadedplayer.getServer().getAsync(new Callback<String>() {
				
				@Override
				public void call(String server) {
					if(server==null){
						stats.loadPlayer(loadedplayer, new Callback<Integer>() {
							@Override
							public void call(Integer playerId) {
								stats.add(playerId, key, value);
								stats.save(playerId);
							}
						});
					}else{
						DataBuffer buffer = new DataBuffer();
						buffer.writeByte((key==StatsKey.GEMS ? 1 : 2));
						buffer.writeInt(loadedplayer.getPlayerId());
						buffer.writeInt(value);
						
						UtilServer.getClient().sendServerMessage(server, "money", buffer);
					}
				}
			});
		}
	}
	
	@EventHandler
	public void statsChange(PlayerStatsChangeEvent ev){
		if(ev.getStats() == StatsKey.GEMS || ev.getStats() == StatsKey.COINS){
			if(UtilPlayer.isOnline(ev.getPlayerId())){
				Player player = UtilPlayer.searchExact(ev.getPlayerId());
				
				if(player.getScoreboard()!=null
						&&player.getScoreboard().getObjective(DisplaySlot.SIDEBAR)!=null){
					Score score = UtilScoreboard.searchScore(player.getScoreboard(), ev.getStats().getMySQLName());
					
					if(score!=null){
						int scoreId = score.getScore()-1;
						UtilScoreboard.resetScore(player.getScoreboard(), scoreId, DisplaySlot.SIDEBAR);
						UtilScoreboard.setScore(player.getScoreboard(), (ev.getStats()==StatsKey.GEMS ? "§a" : "§c")+"§r§7"+ev.getManager().getInt(player, ev.getStats()), DisplaySlot.SIDEBAR, scoreId);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void message(ServerMessageEvent ev){
		if(ev.getChannel().equalsIgnoreCase("money")){
			StatsKey key = (ev.getBuffer().readByte() == 1 ? StatsKey.GEMS : StatsKey.COINS);
			int playerId = ev.getBuffer().readInt();
			int value = ev.getBuffer().readInt();
			
			if(stats.isLoaded(playerId)){
				stats.add(playerId, key, value);
			}else{
				stats.loadPlayer(playerId, new Callback<Integer>(){

					@Override
					public void call(Integer playerId) {
						stats.add(playerId, key, value);
						stats.save(playerId);
					}
					
				});
			}
		}
	}
	
}
