package eu.epicpvp.kcore.StatsManager;

import java.util.ArrayList;
import java.util.HashMap;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.ClientWrapper;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.dataserver.protocoll.packets.PacketInStatsEdit.Action;
import dev.wolveringer.dataserver.protocoll.packets.PacketInStatsEdit.EditStats;
import dev.wolveringer.gamestats.Statistic;
import eu.epicpvp.kcore.Listener.MoneyListener.MoneyListener;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsChangedEvent;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsLoadedEvent;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsSetEvent;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsManager extends kListener {

	@Getter
	private JavaPlugin instance;
	@Getter
	private GameType type;
	@Getter
	private HashMap<Integer, HashMap<StatsKey, StatsObject>> players;
	private ClientWrapper client;
	private StatsManager statsManager;
	private ArrayList<Ranking> rankings;
	@Getter
	@Setter
	private boolean onDisable = false;
	private HashMap<Integer, ArrayList<Callback<Integer>>> loading;
	private ArrayList<String> loadplayers;

	public StatsManager(JavaPlugin instance, ClientWrapper client, GameType type) {
		super(instance, "StatsManager");
		this.players = new HashMap<>();
		this.rankings = new ArrayList<>();
		this.loadplayers = new ArrayList<>();
		this.loading = new HashMap<>();
		this.instance = instance;
		this.type = type;
		this.client = client;
		this.statsManager = this;
		if(type==GameType.Money)new MoneyListener(this);
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

	@EventHandler(priority=EventPriority.HIGHEST)
	public void quit(PlayerQuitEvent ev) {
		this.loadplayers.remove(ev.getPlayer().getName());
		save(ev.getPlayer());
		if (this.loading.containsKey(ev.getPlayer().getName().toLowerCase())) {
			this.loading.get(ev.getPlayer().getName().toLowerCase()).clear();
			this.loading.remove(ev.getPlayer().getName().toLowerCase());
		}
	}

	@EventHandler
	public void loading(PlayerStatsLoadedEvent ev) {
		if (this.loading.containsKey(ev.getPlayerId())) {
			for (Callback<Integer> call : new ArrayList<>(this.loading.get(ev.getPlayerId())))
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
		});
	}

	public void SendRankingMessage(Player player, Ranking ranking) {
		if(ranking.getRanking() ==null){
			ranking.load(new Callback() {
				@Override
				public void call(Object obj) {
					statsManager.SendRankingMessage(player, ranking);
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
		return (players.containsKey(playerId) && players.get(playerId).containsKey(key));
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

	public int getInt(StatsKey key, Player player) {
		return (int) get(player, key);
	}

	public int getInt(Player player, StatsKey key) {
		return (int) get(player, key);
	}

	public Object get(StatsKey key, Player player) {
		return get(UtilPlayer.getPlayerId(player),key);
	}

	public Object get(Player player, StatsKey key) {
		return get(UtilPlayer.getPlayerId(player),key);
	}

	public Object get(int playerId, StatsKey key) {
		if (this.players.containsKey(playerId)) {
			if (this.players.get(playerId).containsKey(key)) {
				return this.players.get(playerId).get(key).getValue();
			}
		}else{
			loadPlayer(playerId, null);
		}

		new NullPointerException().printStackTrace();
		return null;
	}

	public void getAsync(Player player, StatsKey key, Callback<Object> callback) {
		getAsync(UtilPlayer.getPlayerId(player), key, callback);
	}

	public void getAsync(int playerId, StatsKey key, Callback<Object> callback) {
		if (this.players.containsKey(playerId)) {
			if (this.players.get(playerId).containsKey(key)) {
				callback.call(this.players.get(playerId).get(key).getValue());
			}
			return;
		}
			
		if (!this.loading.containsKey(playerId))
			this.loading.put(playerId, new ArrayList<>());
		this.loading.get(playerId).add(new Callback<Integer>() {
			@Override
			public void call(Integer playerId) {
				getAsync(playerId, key, callback);
				loading.get(playerId).remove(this);
			}
		});
	}

	public double getKDR(int k, int d) {
		double kdr = (double) k / (double) d;
		kdr = kdr * 100;
		kdr = Math.round(kdr);
		kdr = kdr / 100;
		return kdr;
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

	public void set(int playerId, StatsKey key,Object obj) {
		if (key.getType() != String.class) {
			if (this.players.containsKey(playerId)) {
				if (this.players.get(playerId).containsKey(key)) {
					PlayerStatsSetEvent ev = new PlayerStatsSetEvent(statsManager, key, obj, playerId);
					Bukkit.getPluginManager().callEvent(ev);
					obj=ev.getValue();
					
					if (key.getType() == int.class) {
						int i = (int) this.players.get(playerId).get(key).getValue();
						int change = (int) obj;

						change = change - i;
						this.players.get(playerId).get(key).add(change);
					} else if (key.getType() == double.class) {
						double i = (double) this.players.get(playerId).get(key).getValue();
						double change = (double) obj;

						change = change - i;
						this.players.get(playerId).get(key).add(change);
					} else if (key.getType() == long.class) {
						long i = (long) this.players.get(playerId).get(key).getValue();
						long change = (long) obj;

						change = change - i;
						this.players.get(playerId).get(key).add(change);
					}
				} else {
					this.players.get(playerId).put(key, new StatsObject(0));
					this.players.get(playerId).get(key).add(obj);
				}
					
				Bukkit.getPluginManager().callEvent(new PlayerStatsChangedEvent(this, key, playerId));
			} else {
				logMessage("set LOAD PLAYER "+playerId);
				Object cobj = obj;
				loadPlayer(playerId, new Callback<Integer>() {
					@Override
					public void call(Integer pid) {
						set(playerId, key, cobj);
					}
				});
			}
		} else {
			setString(playerId, key, ((String) obj));
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
		if (this.players.containsKey(playerId)) {
			if (this.players.get(playerId).containsKey(key)) {
				this.players.get(playerId).get(key).add(value);
					
				Bukkit.getPluginManager().callEvent(new PlayerStatsChangedEvent(statsManager,key, playerId));
			} else {
				this.players.get(playerId).put(key, new StatsObject(0));
				this.players.get(playerId).get(key).add(value);
					
				Bukkit.getPluginManager().callEvent(new PlayerStatsChangedEvent(statsManager,key, playerId));
			}
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
			return;
		}
		loadplayers.add(loadedplayer.getName());
		
		loadedplayer.getStats(getType()).getAsync(new Callback<Statistic[]>() {

			@Override
			public void call(Statistic[] obj) {
				if (!players.containsKey(loadedplayer.getPlayerId()))
					players.put(loadedplayer.getPlayerId(), new HashMap<>());
				
				for (Statistic s : obj) {
					players.get(loadedplayer.getPlayerId()).put(s.getStatsKey(), new StatsObject(s.getValue()));
				}

				if (callback != null) {
					callback.call(loadedplayer.getPlayerId());
				}
				
				loadplayers.remove(loadedplayer.getName());
				Bukkit.getPluginManager().callEvent(new PlayerStatsLoadedEvent(statsManager, loadedplayer.getPlayerId()));
			}
		});
	}
	
	public void saveAll() {
		EditStats[] stats;
		LoadedPlayer loadedplayer;
		for (int playerId : this.players.keySet()) {
			loadedplayer = client.getPlayerAndLoad(playerId);
			stats = new EditStats[this.players.get(playerId).size()];

			for (int i = 0; i < this.players.get(playerId).size(); i++) {
				StatsKey key = (StatsKey) this.players.get(playerId).keySet().toArray()[i];
				if (this.players.get(playerId).get(key).getChange() != null) {
					if (key.getType() == String.class) {
						stats[i] = new EditStats(getType(), Action.SET, key, this.players.get(playerId).get(key).getValue());
					} else {
						stats[i] = new EditStats(getType(), Action.ADD, key, this.players.get(playerId).get(key).getChange());
					}
				}
			}

			loadedplayer.setStats(stats);
		}
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
			EditStats[] stats = new EditStats[this.players.get(playerId).size()];

			StatsKey key;
			for (int i = 0; i < this.players.get(playerId).size(); i++) {
				key = (StatsKey) this.players.get(playerId).keySet().toArray()[i];

				if (this.players.get(playerId).get(key).getChange() != null) {
					if (key.getType() == String.class) {
						stats[i] = new EditStats(getType(), Action.SET, key,
								this.players.get(playerId).get(key).getValue());
					} else {
						stats[i] = new EditStats(getType(), Action.ADD, key,
								this.players.get(playerId).get(key).getChange());
					}
				}
			}

			loadedplayer.setStats(stats);
			this.players.remove(playerId);
			loadplayers.remove(loadedplayer.getName());
		}
	}
}
