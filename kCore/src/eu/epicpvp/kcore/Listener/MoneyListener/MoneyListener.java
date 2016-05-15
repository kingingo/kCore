package eu.epicpvp.kcore.Listener.MoneyListener;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.dataserver.protocoll.DataBuffer;
import dev.wolveringer.nbt.NBTTagCompound;
import eu.epicpvp.kcore.Events.ServerMessageEvent;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsChangedEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilScoreboard;
import eu.epicpvp.kcore.Util.UtilServer;

public class MoneyListener extends kListener{
	
	private StatsManager money;
	private StatsManager properties;

	public MoneyListener(StatsManager money) {
		super(money.getInstance(), "MoneyListener");
		this.money=money;
		this.properties=StatsManagerRepository.getStatsManager(GameType.PROPERTIES);
		this.properties.setForceSave(true);
		this.properties.setAutoLoad(true);;
		UtilServer.setMoneyListener(this);
	}
	

	public void update(String player, StatsKey key, int value){
		update(player,key,"",value);
	}
	
	public void update(String player, StatsKey key,String path, int value){
		if(key==StatsKey.GEMS||key==StatsKey.COINS||key==StatsKey.MYSTERY_SHARPS){
			LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player);
			
			if(UtilPlayer.isOnline(player)){
				money.add(loadedplayer.getPlayerId(), key, value);
				logMessage("Add "+loadedplayer.getName()+" "+value+""+key.getMySQLName()+" to his/her Account!");
			}else{
				loadedplayer.getServer().getAsync(new Callback<String>() {
					
					@Override
					public void call(String server) {
						if(server==null){
							money.loadPlayer(loadedplayer, new Callback<Integer>() {
								@Override
								public void call(Integer playerId) {
									money.add(playerId, key, value);
									money.save(playerId);
									logMessage("Add "+loadedplayer.getName()+" "+value+""+key.getMySQLName()+" to his/her Account!");
								}
							});
						}else{
							DataBuffer buffer = new DataBuffer();
							buffer.writeByte((key==StatsKey.GEMS ? 1 : 2));
							buffer.writeInt(loadedplayer.getPlayerId());
							buffer.writeInt(value);
							buffer.writeString(path);

							logMessage("Send to "+server+" the Server!");
							UtilServer.getClient().sendServerMessage(server, "money", buffer);
						}
					}
				});
			}
		}else if(key == StatsKey.PROPERTIES){
			LoadedPlayer loadedplayer = UtilServer.getClient().getPlayerAndLoad(player);
			
			if(UtilPlayer.isOnline(player)){
				NBTTagCompound nbt = properties.getNBTTagCompound(loadedplayer.getPlayerId(),key);
				nbt.setInt(path, nbt.getInt(path)+value);
				try {
					properties.setNBTTagCompound(loadedplayer.getPlayerId(), nbt, StatsKey.PROPERTIES);
				} catch (Exception e) {
					e.printStackTrace();
				}
				logMessage("Add "+loadedplayer.getName()+" "+path+" "+value+" "+key.getMySQLName()+" to his/her Account!");
			}else{
				loadedplayer.getServer().getAsync(new Callback<String>() {
					
					@Override
					public void call(String server) {
						if(server==null){
							properties.loadPlayer(loadedplayer, new Callback<Integer>() {
								@Override
								public void call(Integer playerId) {
									NBTTagCompound nbt = properties.getNBTTagCompound(loadedplayer.getPlayerId(),key);
									nbt.setInt(path, nbt.getInt(path)+value);
									try {
										properties.setNBTTagCompound(loadedplayer.getPlayerId(), nbt, StatsKey.PROPERTIES);
									} catch (Exception e) {
										e.printStackTrace();
									}
									logMessage("Add "+loadedplayer.getName()+" "+path+" "+value+" "+key.getMySQLName()+" to his/her Account!");
								}
							});
						}else{
							DataBuffer buffer = new DataBuffer();
							buffer.writeByte(key.ordinal());
							buffer.writeInt(loadedplayer.getPlayerId());
							buffer.writeInt(value);
							buffer.writeString(path);

							logMessage("Send to "+server+" the Server!");
							UtilServer.getClient().sendServerMessage(server, "money", buffer);
						}
					}
				});
			}
		}else{
			throw new NullArgumentException("StatsKey is not Gems or Coins!? ("+key.getMySQLName()+")");
		}
	}
	
	@EventHandler
	public void moneyChange(PlayerStatsChangedEvent ev){
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
			StatsKey key = StatsKey.values()[ev.getBuffer().readByte()];
			int playerId = ev.getBuffer().readInt();
			int value = ev.getBuffer().readInt();
			String path = ev.getBuffer().readString();
			
			if(key==StatsKey.GEMS || StatsKey.COINS == key || StatsKey.MYSTERY_SHARPS == key){
				if(money.isLoaded(playerId)){
					money.add(playerId, key, value);
					logMessage("Add "+playerId+" "+value+" "+key.getMySQLName()+" to his/her Account!");
				}else{
					money.loadPlayer(playerId, new Callback<Integer>(){

						@Override
						public void call(Integer playerId) {
							money.add(playerId, key, value);
							money.save(playerId);
							logMessage("Add "+playerId+" "+value+" "+key.getMySQLName()+" to his/her Account!");
						}
						
					});
				}
			}else if(key == StatsKey.PROPERTIES){

				if(properties.isLoaded(playerId)){
					NBTTagCompound nbt = properties.getNBTTagCompound(playerId,key);
					nbt.setInt(path, nbt.getInt(path)+value);
					try {
						properties.setNBTTagCompound(playerId, nbt, StatsKey.PROPERTIES);
					} catch (Exception e) {
						e.printStackTrace();
					}
					logMessage("Add "+playerId+" "+path+" "+value+" "+key.getMySQLName()+" to his/her Account!");
				}else{
					properties.loadPlayer(playerId, new Callback<Integer>(){

						@Override
						public void call(Integer playerId) {
							NBTTagCompound nbt = properties.getNBTTagCompound(playerId, StatsKey.PROPERTIES);
							nbt.setInt(path, value+nbt.getInt(path));
							try {
								properties.setNBTTagCompound(playerId, nbt, StatsKey.PROPERTIES);
							} catch (Exception e) {
								e.printStackTrace();
							}
							logMessage("Add "+playerId+" "+path+" "+value+""+key.getMySQLName()+" to his/her Account!");
						}
						
					});
				}
			}
		}
	}
	
}
