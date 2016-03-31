package eu.epicpvp.kcore.StatsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.client.Callback;
import dev.wolveringer.client.ClientWrapper;
import dev.wolveringer.client.LoadedPlayer;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.dataserver.protocoll.packets.PacketInStatsEdit.Action;
import dev.wolveringer.dataserver.protocoll.packets.PacketInStatsEdit.EditStats;
import dev.wolveringer.gamestats.Statistic;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsChangeEvent;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsLoadedEvent;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;
import lombok.Setter;

public class StatsManager extends kListener{

	@Getter
	private JavaPlugin instance;
	@Getter
	private GameType type;
	@Getter
	private HashMap<UUID,HashMap<StatsKey,StatsObject>> players; 
	private ClientWrapper client;
	private StatsManager statsManager;
	private ArrayList<Ranking> rankings;
	@Getter
	@Setter
	private boolean onDisable=false;
	private HashMap<String,ArrayList<Callback<String>>> loading;
	
	public StatsManager(JavaPlugin instance,ClientWrapper client,GameType type){
		super(instance,"StatsManager");
		this.players = new HashMap<>();
		this.rankings=new ArrayList<>();
		this.loading=new HashMap<>();
		this.instance=instance;
		this.type=type;
		this.client=client;
		this.statsManager=this;
	}
	
	public boolean isLoaded(Player player){
		return isLoaded(getUUID(player));
	}
	
	public boolean isLoaded(UUID uuid){
		return players.containsKey(uuid);
	}
	
	public void addRanking(Ranking ranking){
		rankings.add(ranking);
	}
	
	@EventHandler
	public void loading(PlayerStatsLoadedEvent ev){
		if(this.loading.containsKey(ev.getPlayername().toLowerCase())){
			for(Callback<String> call : this.loading.get(ev.getPlayername().toLowerCase()))call.call(ev.getPlayername());
		}
	}
	
	@EventHandler
	public void Ranking(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_08)return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(Ranking ranking : rankings)ranking.load();
			}
		});
	}
	
	public void SendRankingMessage(Player player,Ranking ranking,String Zeitraum){
		if(ranking.getRanking().isEmpty())ranking.load();
		player.sendMessage("§b– – – – – – – – §6 §lPlayer Ranking | "+Zeitraum+" | Top "+ranking.getLength()+" §b– – – – – – – – ");
		player.sendMessage("§b Platz | "+(ranking.getStats().getMySQLName().equalsIgnoreCase("elo") ? "FAME" : ranking.getStats().getMySQLName())+" | Player");
		for(Integer i : ranking.getRanking().keySet())player.sendMessage("§b#§6" + String.valueOf(i) + "§b | §6" + String.valueOf(ranking.getRanking().get(i).stats) + " §b|§6 " +ranking.getRanking().get(i).player);
	}
	
	public void SendRankingMessage(Player player,Ranking ranking){
		if(ranking.getRanking().isEmpty())ranking.load();
		player.sendMessage("§b– – – – – – – – §6 §lPlayer Ranking | Top "+ranking.getLength()+" §b– – – – – – – – ");
		player.sendMessage("§b Platz | "+(ranking.getStats().getMySQLName().equalsIgnoreCase("elo") ? "FAME" : ranking.getStats().getMySQLName())+" | Player");
		for(Integer i : ranking.getRanking().keySet())player.sendMessage("§b#§6" + String.valueOf(i) + "§b | §6" + String.valueOf(ranking.getRanking().get(i).stats) + " §b|§6 " +ranking.getRanking().get(i).player);
	}
	
	public long getLong(StatsKey key,Player player){
		return (long) get(player, key);
	}
	
	public long getLong(Player player, StatsKey key){
		return (long) get(player, key);
	}
	
	public long getLong(UUID uuid, StatsKey key){
		return (long) get(uuid, key);
	}
	
	public double getDouble (StatsKey key,Player player){
		return (double) get(player, key);
	}
	
	public double getDouble(Player player, StatsKey key){
		return (double) get(player, key);
	}
	
	public double getDouble(UUID uuid, StatsKey key){
		return (double) get(uuid, key);
	}
	
	public String getString(StatsKey key,Player player){
		return (String) get(player, key);
	}
	
	public String getString(Player player, StatsKey key){
		return (String) get(player, key);
	}
	
	public String getString(UUID uuid, StatsKey key){
		return (String) get(uuid, key);
	}
	
	public int getInt(StatsKey key,Player player){
		return (int) get(player, key);
	}
	
	public int getInt(Player player, StatsKey key){
		return (int) get(player, key);
	}
	
	public int getInt(UUID uuid, StatsKey key){
		return (int) get(uuid, key);
	}

	public Object get(StatsKey key,Player player){
		return get(getUUID(player),key);
	}
	
	public Object get(Player player, StatsKey key){
		return get(getUUID(player),key);
	}
	
	public Object get(UUID uuid, StatsKey key){
		if(this.players.containsKey(uuid)){
			if(this.players.get(uuid).containsKey(key)){
				return this.players.get(uuid).get(key).getValue();
			}
		}

		throw new NullPointerException();
	}
	
	public void getAsync(Player player, StatsKey key,Callback<Object> callback){
		getAsync(getUUID(player), player.getName(), key, callback);
	}
	
	public void getAsync(UUID uuid,String playername, StatsKey key,Callback<Object> callback){
		if(this.players.containsKey(uuid)){
			if(this.players.get(uuid).containsKey(key)){
				callback.call(this.players.get(uuid).get(key).getValue());
			}
		}else if(playername!=null){
			playername=playername.toLowerCase();
			if(!this.loading.containsKey(playername))this.loading.put(playername, new ArrayList<>());
			this.loading.get(playername).add(new Callback<String>() {

				@Override
				public void call(String playername) {
					getAsync(uuid, null, key, callback);
				}
				
			});
		}
	}
	
	public double getKDR(int k,int d){
		double kdr = (double)k/(double)d;
		kdr = kdr * 100;
		kdr = Math.round(kdr);
		kdr = kdr / 100;
		return kdr;
	}
	
	public void setString(Player player, String s, StatsKey key){
		setString(getUUID(player),player.getName(),key,s);
	}
	
	public void setString(Player player, StatsKey key, String s){
		setString(getUUID(player),player.getName(),key,s);
	}
	
	public void setString(UUID uuid,String playername, StatsKey key, String s){
		if(key.getType() == String.class){
			this.players.get(uuid).get(key).add(s);
			if(playername!=null)Bukkit.getPluginManager().callEvent(new PlayerStatsChangeEvent(this, key, playername));
		}
	}
	
	public void setLong(Player player, long obj, StatsKey key){
		set(getUUID(player),player.getName(),key,obj);
	}
	
	public void setInt(Player player, int obj, StatsKey key){
		set(getUUID(player),player.getName(),key,obj);
	}
	
	public void setDouble(Player player, double obj, StatsKey key){
		set(getUUID(player),player.getName(),key,obj);
	}
	
	public void set(Player player, StatsKey key, Object obj){
		set(getUUID(player),player.getName(),key,obj);
	}
	
	public void set(UUID uuid,String playername, StatsKey key, Object obj){
		if(key.getType() != String.class){
			if(this.players.containsKey(uuid)){
				if(this.players.get(uuid).containsKey(key)){
					if(key.getType() == int.class){
						int i = (int) this.players.get(uuid).get(key).getValue();
						int change = (int) obj;
						
						change = change - i;
						this.players.get(uuid).get(key).add(change);
					}else if(key.getType() == double.class){
						double i = (double) this.players.get(uuid).get(key).getValue();
						double change = (double) obj;
						
						change = change - i;
						this.players.get(uuid).get(key).add(change);
					}else if(key.getType() == long.class){
						long i = (long) this.players.get(uuid).get(key).getValue();
						long change = (long) obj;
						
						change = change - i;
						this.players.get(uuid).get(key).add(change);
					}
				}else{
					this.players.get(uuid).put(key, new StatsObject(0));
					this.players.get(uuid).get(key).add(obj);
				}
				if(playername!=null)Bukkit.getPluginManager().callEvent(new PlayerStatsChangeEvent(this, key, playername));
			}else{
				loadPlayer(uuid,playername, new Callback<UUID>() {
	
					@Override
					public void call(UUID obj) {
						set(uuid,playername, key, obj);
					}
				});
			}
		}else{
			setString(uuid,playername, key, ((String)obj));
		}
	}
	
	public void addLong(Player player, long value,StatsKey key){
		add(getUUID(player),player.getName(), key,value);
	}
	
	public void addInt(Player player, int value,StatsKey key){
		add(getUUID(player),player.getName(), key,value);
	}
	
	public void addDouble(Player player, double value,StatsKey key){
		add(getUUID(player),player.getName(), key,value);
	}
	
	public void add(Player player, StatsKey key, Object value){
		add(getUUID(player),player.getName(), key,value);
	}
	
	public void add(UUID uuid,String playername, StatsKey key, Object value){
		if(this.players.containsKey(uuid)){
			if(this.players.get(uuid).containsKey(key)){
				this.players.get(uuid).get(key).add(value);
				if(playername!=null)Bukkit.getPluginManager().callEvent(new PlayerStatsLoadedEvent(statsManager, playername));
			}else{
				this.players.get(uuid).put(key, new StatsObject(0));
				this.players.get(uuid).get(key).add(value);
				if(playername!=null)Bukkit.getPluginManager().callEvent(new PlayerStatsLoadedEvent(statsManager, playername));
			}
		}else{
			loadPlayer(uuid,playername, new Callback<UUID>() {

				@Override
				public void call(UUID obj) {
					add(uuid,playername, key, obj);
				}
			});
		}
	}
	
	public UUID getUUID(Player player){
		return UtilPlayer.getRealUUID(player);
	}
	
	public void loadPlayer(Player player){
		loadPlayer(getUUID(player),player.getName(),null);
	}
	
	public void loadPlayer(String playername,Callback<UUID> callback){
		LoadedPlayer loadedplayer = client.getPlayerAndLoad(playername);
		
		loadedplayer.getStats(getType()).getAsync(new Callback<Statistic[]>() {
			
			@Override
			public void call(Statistic[] obj) {
				if(!players.containsKey(loadedplayer.getUUID()))players.put(loadedplayer.getUUID(), new HashMap<>());
				
				for(Statistic s : obj){
					players.get(loadedplayer.getUUID()).put(s.getStatsKey(), new StatsObject(s.getValue()));
				}
				
				if(callback!=null){
					callback.call(loadedplayer.getUUID());
				}
				if(playername!=null)Bukkit.getPluginManager().callEvent(new PlayerStatsLoadedEvent(statsManager, playername));
			}
		});
	}
	
	public void loadPlayer(UUID uuid,String playername,Callback<UUID> callback){
		LoadedPlayer loadedplayer = client.getPlayerAndLoad(uuid);
		
		loadedplayer.getStats(getType()).getAsync(new Callback<Statistic[]>() {
			
			@Override
			public void call(Statistic[] obj) {
				if(!players.containsKey(loadedplayer.getUUID()))players.put(loadedplayer.getUUID(), new HashMap<>());
				
				for(Statistic s : obj){
					players.get(loadedplayer.getUUID()).put(s.getStatsKey(), new StatsObject(s.getValue()));
				}
				
				if(callback!=null){
					callback.call(loadedplayer.getUUID());
				}
				if(playername!=null)Bukkit.getPluginManager().callEvent(new PlayerStatsLoadedEvent(statsManager, playername));
			}
		});
	}
	
	public void saveAll(){
		EditStats[] stats;
		LoadedPlayer loadedplayer;
		for(UUID uuid : this.players.keySet()){
			loadedplayer = client.getPlayer(uuid);
			loadedplayer.load();
			stats = new EditStats[this.players.get(uuid).size()];
			
			StatsKey key;
			for(int i = 0; i < this.players.get(uuid).size() ; i++){
				key=(StatsKey)this.players.get(uuid).keySet().toArray()[i];

				if(this.players.get(uuid).get(key).getChange()!=null){
					if(key.getType() == String.class){
						stats[i]=new EditStats(getType(), Action.SET, key, this.players.get(uuid).get(key).getValue());
					}else{
						stats[i]=new EditStats(getType(), Action.ADD, key, this.players.get(uuid).get(key).getChange());
					}
				}
			}
			
			loadedplayer.setStats(stats);
		}
	}
	
	public void SaveAllPlayerData(Player player){
		save(getUUID(player));
	}
	
	public void save(Player player){
		save(getUUID(player));
	}
	
	public void save(UUID uuid){
		if(isOnDisable())return;
		if(this.players.containsKey( uuid )){
			LoadedPlayer loadedplayer = client.getPlayer(uuid);
			loadedplayer.load();
			EditStats[] stats = new EditStats[this.players.get(uuid).size()];
			
			StatsKey key;
			for(int i = 0; i < this.players.get(uuid).size() ; i++){
				key=(StatsKey)this.players.get(uuid).keySet().toArray()[i];
				
				if(this.players.get(uuid).get(key).getChange()!=null){
					if(key.getType() == String.class){
						stats[i]=new EditStats(getType(), Action.SET, key, this.players.get(uuid).get(key).getValue());
					}else{
						stats[i]=new EditStats(getType(), Action.ADD, key, this.players.get(uuid).get(key).getChange());
					}
				}
			}
			
			loadedplayer.setStats(stats);
			this.players.remove(uuid);
		}
	}
}
