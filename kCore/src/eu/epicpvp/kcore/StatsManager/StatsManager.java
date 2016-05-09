package eu.epicpvp.kcore.StatsManager;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.ClientWrapper;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.dataserver.protocoll.packets.PacketInStatsEdit.Action;
import dev.wolveringer.dataserver.protocoll.packets.PacketInStatsEdit.EditStats;
import dev.wolveringer.gamestats.Statistic;
import dev.wolveringer.nbt.NBTCompressedStreamTools;
import dev.wolveringer.nbt.NBTTagCompound;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Listener.MoneyListener.MoneyListener;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsChangedEvent;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsLoadedEvent;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsSetEvent;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;
import lombok.Setter;

public class StatsManager extends kListener {

	@Getter
	private JavaPlugin instance;
	@Getter
	private GameType type;
	@Getter
	private Map<Integer, Map<StatsKey, StatsObject>> players = new HashMap<>();
	private ClientWrapper client;
	private ArrayList<Ranking> rankings = new ArrayList<>();
	@Getter
	@Setter
	private boolean onDisable = false;
	private Map<Integer, ArrayList<Callback<Integer>>> loading = new HashMap<>();
	private ArrayList<String> loadplayers = new ArrayList<>();
	@Getter
	@Setter
	private boolean forceSave = false;
	@Getter
	@Setter
	private boolean autoLoad = false;

	public StatsManager(JavaPlugin instance, ClientWrapper client, GameType type) {
		super(instance, "StatsManager");
		this.instance = instance;
		this.type = type;
		this.client = client;
		if(type==GameType.Money){
			setForceSave(true);
			new MoneyListener(this);
		}
		StatsManagerRepository.addStatsManager(this);
	}

	public boolean isLoaded(Player player) {
		return isLoaded(UtilPlayer.getPlayerId(player));
	}

	public boolean isLoaded(int playerId) {
		return players.containsKey(playerId);
	}

	public void addRanking(Ranking ranking) {
		ranking.load();
		rankings.add(ranking);
	}


	@EventHandler(priority=EventPriority.MONITOR)
	public void join(PlayerJoinEvent ev) {
		if(isAutoLoad())loadPlayer(ev.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void quit(PlayerQuitEvent ev) {
		this.loadplayers.remove(ev.getPlayer().getName());
		save(ev.getPlayer());
		int playerId = UtilPlayer.getPlayerId(ev.getPlayer());
		ArrayList<Callback<Integer>> callbacks = this.loading.remove(playerId);
		if (callbacks != null) {
			callbacks.clear();
		}
		this.players.remove(playerId);
	}
	
	@EventHandler
	public void forceSave(PlayerStatsChangedEvent ev){
		if(isForceSave()){
			save(ev.getPlayerId());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void loading(PlayerStatsLoadedEvent ev) {
		ArrayList<Callback<Integer>> callbacks = this.loading.get(ev.getPlayerId());
		if (callbacks != null) {
			for (Callback<Integer> call : new ArrayList<>(callbacks))
				call.call(ev.getPlayerId());
		}
	}

	@EventHandler
	public void Ranking(UpdateEvent ev) {
		if (ev.getType() != UpdateType.MIN_08)
			return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (Ranking ranking : rankings)
					ranking.load();
			}
		}).start();
	}

	public void SendRankingMessage(Player player, Ranking ranking) {
		if(ranking.getRanking() ==null){
			ranking.load(new Callback<Object>() {
				@Override
				public void call(Object obj) {
					StatsManager.this.SendRankingMessage(player, ranking);
				}
			});
		}else{
			player.sendMessage("§b■■■■■■■■§6 §lPlayer Ranking | Top "+ranking.getRanking().length+" §b■■■■■■■■");
			player.sendMessage("§b Platz | " + ranking.getStats().getMySQLName() + " | Player");
			for (int i = 0; i < ranking.getRanking().length ; i++)
				player.sendMessage("§b#§6" + (i+1) + "§b | §6" + UtilMath.trim(2, UtilNumber.toDouble(ranking.getRanking()[i].getTopValue())) + " §b|§6 " + ranking.getRanking()[i].getPlayer());
		}
	}

	public boolean containsKey(Player player, StatsKey key){
		return containsKey(UtilPlayer.getPlayerId(player),key);
	}
	
	public boolean containsKey(int playerId, StatsKey key){
		Map<StatsKey, StatsObject> statsMap = players.get(playerId);
		return (statsMap != null && statsMap.containsKey(key));
	}

	public NBTTagCompound getNBTTagCompound(int playerId,StatsKey key){
		return (NBTTagCompound)get(playerId,key);
	}

	public NBTTagCompound getNBTTagCompound(Player player,StatsKey key){
		return (NBTTagCompound)get(player,key);
	}
	
	public long getLong(StatsKey key, Player player) {
		return (long) get(player, key);
	}

	public long getLong(Player player, StatsKey key) {
		return (long) get(player, key);
	}

	public double getDouble(StatsKey key, Player player) {
		return (double) get(player, key);
	}

	public double getDouble(Player player, StatsKey key) {
		return (double) get(player, key);
	}

	public String getString(StatsKey key, Player player) {
		return (String) get(player, key);
	}

	public String getString(Player player, StatsKey key) {
		return (String) get(player, key);
	}

	public String getString(int playerId, StatsKey key) {
		return (String) get(playerId, key);
	}

	public int getInt(StatsKey key, Player player) {
		return (int) get(player, key);
	}

	public int getInt(Player player, StatsKey key) {
		return (int) get(player, key);
	}

	public int getInt(int playerId, StatsKey key) {
		return (int) get(playerId, key);
	}

	public Object get(StatsKey key, Player player) {
		return get(UtilPlayer.getPlayerId(player),key);
	}

	public Object get(Player player, StatsKey key) {
		return get(UtilPlayer.getPlayerId(player),key);
	}

	public Object get(int playerId, StatsKey key) {
		Map<StatsKey, StatsObject> statsMap = this.players.get(playerId);
		if (statsMap != null) {
			StatsObject statsObject = statsMap.get(key);
			if (statsObject != null) {
				return statsObject.getValue();
			} else {
				return null;
			}
		}else{
			loadPlayer(playerId, null);
		}

		new Exception("need to load player at StatsManager get(playerId, StatsKey)").printStackTrace();
		return null;
	}

	public void getAsync(Player player, StatsKey key, Callback<Object> callback) {
		getAsync(UtilPlayer.getPlayerId(player), key, callback);
	}

	public void getAsync(int playerId, StatsKey key, Callback<Object> callback) {
		Map<StatsKey, StatsObject> statsMap = this.players.get(playerId);
		if (statsMap != null) {
			StatsObject statsObject = statsMap.get(key);
			if (statsObject != null) {
				callback.call(statsObject.getValue());
			}
			return;
		}

		ArrayList<Callback<Integer>> callbacks = this.loading.get(playerId);
		if (callbacks == null) {
			callbacks = new ArrayList<>();
			this.loading.put(playerId, callbacks);
		}
		ArrayList<Callback<Integer>> callbacksFinalVar = callbacks;
		callbacks.add(new Callback<Integer>() {
			@Override
			public void call(Integer playerId) {
				getAsync(playerId, key, callback);
				callbacksFinalVar.remove(this);
			}
		});
	}

	public double getKDR(int k, int d) {
		if (d == 0) { //prevent ArithmeticException - alternative: use deaths to calculate lives (= add 1) and then calculate KLR instead?
			d = 1;
		}
		double kdr = (double) k / (double) d;
		kdr = kdr * 100;
		kdr = Math.round(kdr);
		kdr = kdr / 100D;
		return kdr;
	}
	
	public void setNBTTagCompound(Player player, NBTTagCompound nbt, StatsKey key) throws Exception{
		setNBTTagCompound(UtilPlayer.getPlayerId(player), nbt, key);
	}

	public void setNBTTagCompound(int playerId, NBTTagCompound nbt, StatsKey key) throws Exception{
		if (key == StatsKey.PROPERTIES) {
			this.players.get(playerId).get(key).add(nbt);
			Bukkit.getPluginManager().callEvent(new PlayerStatsChangedEvent(this, key, playerId));
		}
	}
	
	public void setString(Player player, String s, StatsKey key) {
		setString(UtilPlayer.getPlayerId(player), key, s);
	}

	public void setString(Player player, StatsKey key, String s) {
		setString(UtilPlayer.getPlayerId(player), key, s);
	}

	public void setString(int playerId, StatsKey key, String s) {
		if (key.getType() == String.class) {
			this.players.get(playerId).get(key).add(s);
			Bukkit.getPluginManager().callEvent(new PlayerStatsChangedEvent(this, key, playerId));
		}
	}

	public void setLong(Player player, long obj, StatsKey key) {
		set(UtilPlayer.getPlayerId(player), key, obj);
	}

	public void setInt(Player player, int obj, StatsKey key) {
		set(UtilPlayer.getPlayerId(player), key, obj);
	}

	public void setDouble(Player player, double obj, StatsKey key) {
		set(UtilPlayer.getPlayerId(player), key, obj);
	}

	public void set(Player player, StatsKey key, Object obj) {
		set(UtilPlayer.getPlayerId(player), key, obj);
	}

	public void set(int playerId, StatsKey key, Object obj) {
		if(key == StatsKey.PROPERTIES){
			if(obj instanceof NBTTagCompound){
				try {
					setNBTTagCompound(playerId, ((NBTTagCompound)obj), key);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				logMessage("Das Object ist kein NBT TAG " +playerId);
			}
		}else if(key.getType() == String.class){
			setString(playerId, key, ((String) obj));
		}else{
			Map<StatsKey, StatsObject> statsMap = this.players.get(playerId);
			if (statsMap != null) {
				StatsObject statsObject = statsMap.get(key);
				if (statsObject != null) {
					Bukkit.getPluginManager().callEvent(new PlayerStatsSetEvent(this, key, obj, playerId));

					if (key.getType() == int.class) {
						int i = (int) statsObject.getValue();
						int change = (int) obj;

						change = change - i;
						statsObject.add(change);
					} else if (key.getType() == double.class) {
						double i = (double) statsObject.getValue();
						double change = (double) obj;

						change = change - i;
						statsObject.add(change);
					} else if (key.getType() == long.class) {
						long i = (long) statsObject.getValue();
						long change = (long) obj;

						change = change - i;
						statsObject.add(change);
					}
				} else {
					statsObject = new StatsObject(0);
					statsMap.put(key, statsObject);
					statsObject.add(obj);
				}

				Bukkit.getPluginManager().callEvent(new PlayerStatsChangedEvent(this, key, playerId));
			} else {
				logMessage("set LOAD PLAYER " + playerId);
				loadPlayer(playerId, new Callback<Integer>() {
					@Override
					public void call(Integer pid) {
						set(playerId, key, obj);
					}
				});
			}
		}
	}

	public void addLong(Player player, long value, StatsKey key) {
		add(UtilPlayer.getPlayerId(player), key, value);
	}

	public void addInt(Player player, int value, StatsKey key) {
		add(UtilPlayer.getPlayerId(player), key, value);
	}

	public void addDouble(Player player, double value, StatsKey key) {
		add(UtilPlayer.getPlayerId(player), key, value);
	}

	public void add(Player player, StatsKey key, Object value) {
		add( UtilPlayer.getPlayerId(player), key, value);
	}

	public void add(int playerId, StatsKey key, Object value) {
		Map<StatsKey, StatsObject> statsMap = this.players.get(playerId);
		if (statsMap != null) {
			statsMap.computeIfAbsent(key, k -> new StatsObject(0)).add(value);
			Bukkit.getPluginManager().callEvent(new PlayerStatsChangedEvent(this,key, playerId));
		} else {
			logMessage("add LOAD PLAYER "+playerId);
			loadPlayer(playerId, new Callback<Integer>() {

				@Override
				public void call(Integer pid) {
					add(pid, key, value);
				}
			});
		}
	}
	
	public void loadPlayer(String playerName, Callback<Integer> callback) {
		loadPlayer(client.getPlayerAndLoad(playerName), callback);
	}
	
	public void loadPlayer(Player player, Callback<Integer> callback) {
		loadPlayer(player.getName(),callback);
	}
	
	public void loadPlayer(String playerName) {
		loadPlayer(client.getPlayerAndLoad(playerName), null);
	}
	
	public void loadPlayer(Player player) {
		loadPlayer(player.getName());
	}

	public void loadPlayer(int playerId, Callback<Integer> callback) {
		loadPlayer(client.getPlayerAndLoad(playerId), callback);
	}
	
	public void loadPlayer(LoadedPlayer loadedplayer, Callback<Integer> callback){
		if(this.players.containsKey(loadedplayer.getPlayerId())||this.loadplayers.contains(loadedplayer.getName())){
			logMessage("Player is loaded!? "+loadedplayer.getName());
			
			if (callback != null) {
				callback.call(loadedplayer.getPlayerId());
			}
			loadplayers.remove(loadedplayer.getName());
			return;
		}
		loadplayers.add(loadedplayer.getName());
		
		loadedplayer.getStats(getType()).getAsync(new Callback<Statistic[]>() {

			@Override
			public void call(Statistic[] statistics) {

				Map<StatsKey, StatsObject> statsMap = players.computeIfAbsent(loadedplayer.getPlayerId(), key -> new EnumMap<>(StatsKey.class));

				for (Statistic s : statistics) {
					if(s.getStatsKey() == StatsKey.PROPERTIES){
						if(((String)s.getValue()).isEmpty()){
							statsMap.put(s.getStatsKey(), new StatsObject(new NBTTagCompound()));
						}else{
							try {
								statsMap.put(s.getStatsKey(), new StatsObject(NBTCompressedStreamTools.read( ((String)s.getValue()) )));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}else{
						statsMap.put(s.getStatsKey(), new StatsObject(s.getValue()));
					}
				}

				if (callback != null) {
					callback.call(loadedplayer.getPlayerId());
				}
				
				loadplayers.remove(loadedplayer.getName());
				Bukkit.getPluginManager().callEvent(new PlayerStatsLoadedEvent(StatsManager.this, loadedplayer.getPlayerId()));
			}
		});
	}
	
	public void saveAll() {
		EditStats[] stats;
		for (Map.Entry<Integer, Map<StatsKey, StatsObject>> entry : this.players.entrySet()) {
			LoadedPlayer loadedplayer = client.getPlayerAndLoad(entry.getKey());
			Map<StatsKey, StatsObject> statsMap = entry.getValue();
			stats = createEditStatsArray(statsMap);
			loadedplayer.setStats(stats);
		}
	}

	private EditStats[] createEditStatsArray(Map<StatsKey, StatsObject> statsMap) {
		EditStats[] stats;
		stats = new EditStats[statsMap.size()];

		int i = 0;
		for (Map.Entry<StatsKey, StatsObject> entry : statsMap.entrySet()) {
			StatsKey key = entry.getKey();
			StatsObject statsObject = entry.getValue();
			if (statsObject.getChange() != null) {
				if(key == StatsKey.PROPERTIES){
					try {
						stats[i] = new EditStats(getType(), Action.SET, key, NBTCompressedStreamTools.toString(((NBTTagCompound)statsObject.getValue())));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if (key.getType() == String.class) {
					stats[i] = new EditStats(getType(), Action.SET, key, statsObject.getValue());
				} else {
					stats[i] = new EditStats(getType(), Action.ADD, key, statsObject.getChange());
				}
				statsObject.reset();
			}
			i++;
		}
		return stats;
	}

	public void SaveAllPlayerData(Player player) {
		save(UtilPlayer.getPlayerId(player));
	}

	public void save(Player player) {
		save(UtilPlayer.getPlayerId(player));
	}

	public void save(int playerId) {
		if (isOnDisable()) return;
		if (this.players.containsKey(playerId)) {
			LoadedPlayer loadedplayer = client.getPlayerAndLoad(playerId);
			Map<StatsKey, StatsObject> statsMap = this.players.get(playerId);
			EditStats[] stats = createEditStatsArray(statsMap);

			loadedplayer.setStats(stats);
			this.loadplayers.remove(loadedplayer.getName());
		}
	}
}
