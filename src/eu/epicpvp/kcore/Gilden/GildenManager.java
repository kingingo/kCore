package eu.epicpvp.kcore.Gilden;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dev.wolveringer.client.Callback;
import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import eu.epicpvp.kcore.Command.CommandHandler;
import eu.epicpvp.kcore.Gilden.Events.GildeLoadEvent;
import eu.epicpvp.kcore.Gilden.Events.GildePlayerJoinEvent;
import eu.epicpvp.kcore.Gilden.Events.GildePlayerLeaveEvent;
import eu.epicpvp.kcore.Gilden.Events.GildenChatEvent;
import eu.epicpvp.kcore.MySQL.Events.MySQLErrorEvent;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.MySQL.MySQLErr;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsChangedEvent;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Util.UtilDebug;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.Util.UtilTime;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GildenManager implements Listener {

	@Getter
	private JavaPlugin instance;
	@Getter
	private HashMap<String,String[]> gilden_players = new HashMap<>();
	@Getter
	private HashMap<Integer,String> gilden_player = new HashMap<>();
	@Getter
	protected HashMap<String,String> gilden_tag = new HashMap<>();
	@Getter
	protected HashMap<String,EnumMap<GildenType,EnumMap<StatsKey,Object>>> gilden_data = new HashMap<>();
	@Getter
	protected HashMap<String,EnumMap<GildenType,ArrayList<StatsKey>>> gilden_data_musst_saved = new HashMap<>();
	@Getter
	private HashMap<Player,String> gilden_einladung = new HashMap<>();
	@Getter
	private HashMap<String,Integer> gilden_count = new HashMap<>();
	@Getter
	protected MySQL mysql;
	@Getter
	protected GildenType typ;
	@Getter
	private HashMap<Player,Long> teleport = new HashMap<>();
	@Getter
	private HashMap<Player,Location> teleport_loc = new HashMap<>();
	@Getter
	@Setter
	private boolean onDisable=false;
	protected HashMap<Integer,String> ranking = new HashMap<>();
	@Getter
	protected HashMap<String,Integer> extra_prefix = new HashMap<>();
	@Getter
	private StatsManager statsManager;
	@Getter
	@Setter
	private boolean async=false;

	public GildenManager(MySQL mysql,GildenType typ,CommandHandler cmd,StatsManager statsManager){
		this.instance=statsManager.getInstance();
		this.typ=typ;
		this.statsManager=statsManager;
		this.mysql=mysql;
		mysql.Update(isAsync(),"CREATE TABLE IF NOT EXISTS list_gilden_"+typ.getKuerzel()+"(gilde varchar(30),gildentag varchar(30),member int,founder_playerId varchar(100),owner_playerId varchar(100))");
		mysql.Update(isAsync(),"CREATE TABLE IF NOT EXISTS list_gilden_"+typ.getKuerzel()+"_user(playerId int,gilde varchar(30))");
		CreateTable();
		cmd.register(CommandGilde.class, new CommandGilde(this));
		Bukkit.getPluginManager().registerEvents(this, getInstance());

		for(Player p : UtilServer.getPlayers()){
			if(!isPlayerInGilde(UtilPlayer.getPlayerId(p)))continue;
			if(gilden_player.containsKey( UtilPlayer.getPlayerId(p) ))gilden_player.remove( UtilPlayer.getPlayerId(p) );
			getPlayerGilde(p);
		}

		LoadRanking(false);
		UtilServer.setGildenManager(this);
	}

	@EventHandler
	public void s(PlayerStatsChangedEvent ev){
		if(ev.getManager().getType()!=GameType.Money){
			//TODO insert something?
		}
	}

	@EventHandler
	public void chat(AsyncPlayerChatEvent ev) {
		if (ev.getMessage().startsWith("#") && isPlayerInGilde(ev.getPlayer())) {
			ev.setMessage(ChatColor.translateAlternateColorCodes('&', ev.getMessage().substring(1)));
			GildenChatEvent chatEvent = new GildenChatEvent(ev.getPlayer(), ev.getMessage(), getPlayerGilde(ev.getPlayer()), this);

			Bukkit.getPluginManager().callEvent(chatEvent);

			if (!chatEvent.isCancelled()) {
				sendGildenChat(chatEvent.getGilde(), chatEvent.getPlayer(), chatEvent.getMessage());
				ev.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void Ranking(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_32)return;
		extra_prefix.clear();
		LoadRanking(true);
	}

	public void LoadRanking(boolean b) {
		if (getTyp() == GildenType.PVP) {
			LoadRankingFame(b);
		} else {
			LoadRankingKills(b);
		}
	}

	public void LoadRankingKills(boolean b){
		if(ranking.isEmpty()||b){
			extra_prefix.clear();
			getMysql().asyncQuery("SELECT `kills`,`gilde` FROM `list_gilden_"+typ.getKuerzel()+"_data` ORDER BY kills DESC LIMIT 15;", new Callback<ResultSet>() {

				@Override
				public void call(ResultSet rs, Throwable exception) {
					try{

						if((ranking.size()+1)==1){
							ranking.put(1, "§b#§6" + 1 + "§b | §6" + rs.getInt(1) + " §b|§4§l " + rs.getString(2));
						}else if((ranking.size()+1)==2){
					  		ranking.put(2, "§b#§6" + 2 + "§b | §6" + rs.getInt(1) + " §b|§2§l " + rs.getString(2));
					  	}else if((ranking.size()+1)==3){
					  		ranking.put(3, "§b#§6" + 3 + "§b | §6" + rs.getInt(1) + " §b|§e§l " + rs.getString(2));
					  	}else if((ranking.size()+1)>=4 && (ranking.size()+1)<=6){
					  		ranking.put(4, "§b#§6" + 4 + "§b | §6" + rs.getInt(1) + " §b|§3 " + rs.getString(2));
					  	}else if((ranking.size()+1)>=7 && (ranking.size()+1)<=9){
					  		ranking.put(5, "§b#§6" + 5 + "§b | §6" + rs.getInt(1) + " §b|§d " + rs.getString(2));
					  	}else if((ranking.size()+1)>=10 && (ranking.size()+1)<=12){
					  		ranking.put(6, "§b#§6" + 6 + "§b | §6" + rs.getInt(1) + " §b|§a " + rs.getString(2));
					  	}else if((ranking.size()+1)>=13 && (ranking.size()+1)<=15){
					  		ranking.put(7, "§b#§6" + 7 + "§b | §6" + rs.getInt(1) + " §b|§b " + rs.getString(2));
					  	}else{
					  		ranking.put(8, "§b#§6" + 8 + "§b | §6" + rs.getInt(1) + " §b|§6 " + rs.getString(2));
					  	}
						extra_prefix.put(rs.getString(2).toLowerCase(), ranking.size()+1);
					 } catch (Exception err) {
					      System.out.println("MySQL-Error: " + err.getMessage());
					 }
				}
			});
		}
	}

	public void LoadRankingFame(boolean b){
		if(ranking.isEmpty()||b){
			extra_prefix.clear();
			getMysql().asyncQuery("SELECT `elo`,`gilde` FROM `list_gilden_"+typ.getKuerzel()+"_data` ORDER BY elo DESC LIMIT 15;", new Callback<ResultSet>() {

				@Override
				public void call(ResultSet rs, Throwable exception) {
					try{
						if((ranking.size()+1)==1){
							ranking.put(1, "§b#§6" + 1 + "§b | §6" + rs.getInt(1) + " §b|§4§l " + rs.getString(2));
						}else if((ranking.size()+1)==2){
					  		ranking.put(2, "§b#§6" + 2 + "§b | §6" + rs.getInt(1) + " §b|§2§l " + rs.getString(2));
					  	}else if((ranking.size()+1)==3){
					  		ranking.put(3, "§b#§6" + 3 + "§b | §6" + rs.getInt(1) + " §b|§e§l " + rs.getString(2));
					  	}else if((ranking.size()+1)>=4 && (ranking.size()+1)<=6){
					  		ranking.put(4, "§b#§6" + 4 + "§b | §6" + rs.getInt(1) + " §b|§3 " + rs.getString(2));
					  	}else if((ranking.size()+1)>=7 && (ranking.size()+1)<=9){
					  		ranking.put(5, "§b#§6" + 5 + "§b | §6" + rs.getInt(1) + " §b|§d " + rs.getString(2));
					  	}else if((ranking.size()+1)>=10 && (ranking.size()+1)<=12){
					  		ranking.put(6, "§b#§6" + 6 + "§b | §6" + rs.getInt(1) + " §b|§a " + rs.getString(2));
					  	}else if((ranking.size()+1)>=13 && (ranking.size()+1)<=15){
					  		ranking.put(7, "§b#§6" + 7 + "§b | §6" + rs.getInt(1) + " §b|§b " + rs.getString(2));
					  	}else{
					  		ranking.put(8, "§b#§6" + 8 + "§b | §6" + rs.getInt(1) + " §b|§6 " + rs.getString(2));
					  	}
						extra_prefix.put(rs.getString(2).toLowerCase(), ranking.size()+1);
					 } catch (Exception err) {
					      System.out.println("MySQL-Error: " + err.getMessage());
					 }
				}
			});
		}
	}

	public void Ranking(Player p) {
		p.sendMessage("§b– – – – – – – – §6§lGilden Ranking | Top 15 §b– – – – – – – –");
		p.sendMessage("§b Place | " + getTyp().getRankWith() + " | Gilde");
		LoadRanking(false);
		ranking.values().forEach(p::sendMessage);
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void DamageTeam(EntityDamageByEntityEvent ev){
		if(!ev.isCancelled()){
			if(ev.getEntity() instanceof Player && ev.getDamager() instanceof Player){
				Player attack = (Player) ev.getDamager();
				if(this.gilden_player.containsKey(UtilPlayer.getPlayerId(attack))){
					Player defend = (Player)ev.getEntity();
					if(this.gilden_player.containsKey(UtilPlayer.getPlayerId( defend ))){
						if(!this.gilden_player.get(UtilPlayer.getPlayerId(attack)).equalsIgnoreCase("-")){
							if(!this.gilden_player.get(UtilPlayer.getPlayerId( defend )).equalsIgnoreCase("-")){
								if(this.gilden_player.get(UtilPlayer.getPlayerId(attack)).equalsIgnoreCase(this.gilden_player.get(UtilPlayer.getPlayerId( defend )))){
									ev.setCancelled(true);
								}
							}
						}
					}
				}

//				if(isPlayerInGilde(UtilPlayer.getPlayerId(attack))&&isPlayerInGilde(UtilPlayer.getPlayerId(defend))){
//					if(getPlayerGilde(attack).equalsIgnoreCase(getPlayerGilde(defend))){
//						ev.setCancelled(true);
//					}
//				}
			}
		}
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void DamageProjectile(EntityDamageByEntityEvent ev){
		if(!ev.isCancelled()){
			if(ev.getEntity() instanceof Player && ev.getDamager() instanceof Projectile){
				Projectile projectile = (Projectile)ev.getDamager();
				if(!(projectile.getShooter() instanceof Player))return;

				if(this.gilden_player.containsKey(UtilPlayer.getPlayerId( ((Player)projectile.getShooter())))){
					Player defend = (Player)ev.getEntity();
					if(this.gilden_player.containsKey(UtilPlayer.getPlayerId( defend ))){
						if(!this.gilden_player.get(UtilPlayer.getPlayerId( ((Player)projectile.getShooter()))).equalsIgnoreCase("-")){
							if(!this.gilden_player.get(UtilPlayer.getPlayerId( defend )).equalsIgnoreCase("-")){
								if(this.gilden_player.get(UtilPlayer.getPlayerId( ((Player)projectile.getShooter()))).equalsIgnoreCase(this.gilden_player.get(UtilPlayer.getPlayerId( defend )))){
									ev.setCancelled(true);
								}
							}
						}
					}

				}
//				if(isPlayerInGilde(UtilPlayer.getPlayerId( ((Player)projectile.getShooter())) )&&isPlayerInGilde(UtilPlayer.getPlayerId( defend ) )){
//					if(getPlayerGilde(((Player)projectile.getShooter())).equalsIgnoreCase(getPlayerGilde(defend))){
//						ev.setCancelled(true);
//					}
//				}
			}
		}
	}

//	@EventHandler
//	public void elo(PlayerEloEvent ev){
//		if(getTyp()==GildenType.PVP){
//			if(isPlayerInGilde(ev.getPlayer())){
//				g=getPlayerGilde(ev.getPlayer());
//				setDouble(g, getDouble(StatsKey.ELO, g)-ev.getElo_from(), StatsKey.ELO);
//				setDouble(g, getDouble(StatsKey.ELO, g)+ev.getElo_to(), StatsKey.ELO);
//			}
//		}
//	}

	@EventHandler
	public void join(GildePlayerJoinEvent ev){
		if(getTyp()==GildenType.PVP){
			if(isPlayerInGilde(ev.getPlayer())){
				setDouble(ev.getGilde(), getDouble(StatsKey.ELO, ev.getGilde())+ev.getManager().getStatsManager().getDouble(ev.getPlayer(), StatsKey.ELO), StatsKey.ELO);
			}
		}
	}

	@EventHandler
	public void leave(GildePlayerLeaveEvent ev){
		if(getTyp()==GildenType.PVP){
			if(UtilPlayer.isOnline(ev.getPlayer())){
				Player p = Bukkit.getPlayer(ev.getPlayer());
				if(isPlayerInGilde(p)){
					setDouble(ev.getGilde(), getDouble(StatsKey.ELO, ev.getGilde())-ev.getManager().getStatsManager().getDouble(p, StatsKey.ELO), StatsKey.ELO);
				}
			}else{
				if(isPlayerInGilde(UtilPlayer.getPlayerId(ev.getPlayer()))){
//					setDouble(ev.getGilde(), getDouble(StatsKey.ELO, ev.getGilde())-ev.getManager().getStatsManager().getDoubleWithUUID(StatsKey.ELO, UtilPlayer.getPlayerId(ev.getPlayer())), StatsKey.ELO);
				}
			}
		}
	}

	@EventHandler
	public void Death(PlayerDeathEvent ev) {
		ev.setDeathMessage(null);
		Player plr = ev.getEntity();
		if (isPlayerInGilde(plr)) {
			String gilde = getPlayerGilde(plr);
			if (gilde != null && !gilde.equalsIgnoreCase("-")) {
				setInt(gilde, getInt(StatsKey.DEATHS, gilde) + 1, StatsKey.DEATHS);
			}
		}
		if (ev.getEntity().getKiller() != null) {
			if (isPlayerInGilde(ev.getEntity().getKiller())) {
				String gilde = getPlayerGilde(ev.getEntity().getKiller());
				if (gilde != null && !gilde.equalsIgnoreCase("-")) {
					setInt(gilde, getInt(StatsKey.KILLS, gilde) + 1, StatsKey.KILLS);
				}
			}
		}
	}

	public boolean hasMoved(Location loc, Player player) {
		Location plrLoc = player.getLocation();
		if (loc.getBlockX() == plrLoc.getBlockX() && loc.getBlockY() == plrLoc.getBlockY() && loc.getBlockZ() == plrLoc.getBlockZ()) {
			return false;
		} else {
			double diff_x = Math.abs(loc.getX() - plrLoc.getX());
			double diff_y = Math.abs(loc.getY() - plrLoc.getY());
			double diff_z = Math.abs(loc.getZ() - plrLoc.getZ());

			if (diff_x < 0.6 && diff_y < 0.6 && diff_z < 0.6) {
				return false;
			} else {
				return true;
			}
		}
	}

	@EventHandler
	public void Update(UpdateEvent ev){
		if (ev.getType() != UpdateType.FAST) return;
		if (teleport.isEmpty()) return;
		for (int i = 0; i < teleport.size(); i++) {
			Player p = ((Player) teleport.keySet().toArray()[i]);

			if (teleport.get(p) <= System.currentTimeMillis()) {

				if (!p.isOnline()) {
					teleport.remove(p);
					teleport_loc.remove(p);
					continue;
				}

				if (!hasMoved(teleport_loc.get(p), p)) {
					TeleportToHome(p);
				} else {
					p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX") + TranslationHandler.getText(p, "GILDE_TELEPORT_CANCELLED"));
				}
				teleport.remove(p);
				teleport_loc.remove(p);
			} else {
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX") + TranslationHandler.getText(p, "GILDE_HOME", UtilTime.formatMili((teleport.get(p) - System.currentTimeMillis()))));
			}
		}
	}

	public void TeleportToHome(Player p, String gilde) {
		gilde = gilde.toLowerCase();
		try {
			String w = getString(StatsKey.WORLD, gilde, getTyp());
			int x = getInt(StatsKey.LOC_X, gilde, typ);
			int y = getInt(StatsKey.LOC_Y, gilde, typ);
			int z = getInt(StatsKey.LOC_Z, gilde, typ);
			if (Bukkit.getWorld(w) == null) return;
			if (x == 0 && y == 0 && z == 0 && gilde.equalsIgnoreCase("0")) return;
			Location loc = new Location(Bukkit.getWorld(w), x, y, z);
			p.teleport(loc);
			p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX") + TranslationHandler.getText(p, "GILDE_TELEPORTET"));
		} catch (NullPointerException e) {

		}
	}

	public void TeleportToHome(Player p){
		try{
			if(!isPlayerInGilde(p)){
				p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_PLAYER_IS_NOT_IN_GILDE"));
				return;
			}
			String g = getPlayerGilde(p);
			String w = getString(StatsKey.WORLD, g, getTyp());
			int x = getInt(StatsKey.LOC_X, g, typ);
			int y = getInt(StatsKey.LOC_Y, g, typ);
			int z = getInt(StatsKey.LOC_Z, g, typ);
			if(Bukkit.getWorld(w)==null)return;
			if(x==0&&y==0&&z==0&&g.equalsIgnoreCase("0"))return;
			Location loc = new Location(Bukkit.getWorld(w),x,y,z);
			p.teleport(loc);
			p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, "GILDE_TELEPORTET"));
		}catch(NullPointerException e){

		}
	}

	@EventHandler
	public void PlayerQuit(PlayerQuitEvent ev){
		if(isOnDisable())return;
		if(!isPlayerInGilde(ev.getPlayer()))return;
		UpdateGilde(getPlayerGilde(ev.getPlayer()), getTyp());
		sendGildenChat(getPlayerGilde(ev.getPlayer()), "GILDE_PLAYER_LEAVE",ev.getPlayer().getName());
	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent ev){
		if(gilden_player.containsKey( UtilPlayer.getPlayerId(ev.getPlayer()) ))gilden_player.remove( UtilPlayer.getPlayerId(ev.getPlayer()) );
		if(!isPlayerInGilde(ev.getPlayer()))return;
		sendGildenChat(getPlayerGilde(ev.getPlayer()), "GILDE_PLAYER_JOIN",ev.getPlayer().getName());
	}

	public void sendGildenChat(String gilde, Player player, String msg) {
		gilde = gilde.toLowerCase();
		for (Map.Entry<Integer, String> entry : gilden_player.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(gilde)) {
				int playerId = entry.getKey();
				Player p = UtilPlayer.searchExact(playerId);
				if (p != null) {
					p.sendMessage(TranslationHandler.getText(p, "GILDE_CHAT_PREFIX", player.getName()) + msg);
				}
			}
		}
	}

	public void sendGildenChat(String gilde, String name) {
		gilde = gilde.toLowerCase();
		for (Map.Entry<Integer, String> entry : gilden_player.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(gilde)) {
				int playerId = entry.getKey();
				Player p = UtilPlayer.searchExact(playerId);
				if (p != null) {
					p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX") + TranslationHandler.getText(p, name));
				}
			}
		}
	}

	public void sendGildenChat(String gilde,String name,Object... input){
		gilde=gilde.toLowerCase();
		for(Map.Entry<Integer, String> entry : gilden_player.entrySet()){
			if(entry.getValue().equalsIgnoreCase(gilde)){
				int playerId = entry.getKey();
				Player p = UtilPlayer.searchExact(playerId);
				if (p != null) {
					p.sendMessage(TranslationHandler.getText(p, "GILDE_PREFIX")+TranslationHandler.getText(p, name,input));
				}
			}
		}
	}

	public void sendGildenChat(String gilde,String name,Object input){
		sendGildenChat(gilde, name, new Object[]{input});
	}

	public double getKDR(int k,int d){
		double kdr = (double)k/(double)d;
		kdr = kdr * 100;
		kdr = Math.round(kdr);
		kdr = kdr / 100;
		return kdr;
	}

	public void removeGildenEintrag(Player player,String name){
		getMember(name);
		Iterator<Map.Entry<Integer, String>> iterator = getGilden_player().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, String> entry = iterator.next();
			if (entry.getValue().equalsIgnoreCase(name)) {
				iterator.remove();
			}
		}

		mysql.Update(isAsync(), "DELETE FROM `list_gilden_" + typ.getKuerzel() + "` WHERE `gilde`='" + name.toLowerCase() + "'");
		mysql.Update(isAsync(), "DELETE FROM `list_gilden_" + typ.getKuerzel() + "_data` WHERE `gilde`='" + name.toLowerCase() + "'");
		mysql.Update(isAsync(), "DELETE FROM `list_gilden_" + typ.getKuerzel() + "_user` WHERE `gilde`='" + name.toLowerCase() + "'");
		gilden_data.remove(name);
		gilden_tag.remove(name);
		gilden_data_musst_saved.remove(name);
	}

	public void loadTag(final String gilde){
		if(gilden_tag.containsKey(gilde.toLowerCase()))return;
		getMysql().asyncGetString("SELECT `gildentag` FROM `list_gilden_"+typ.getKuerzel()+"` WHERE gilde='"+gilde.toLowerCase()+"'", new Callback<String>() {

			@Override
			public void call(String value, Throwable exception) {
				gilden_tag.put(gilde.toLowerCase(), value);
			}
		});
	}

	public void loadStats(final String gilde){
		for(StatsKey stats : typ.getStats()){
			if(!this.gilden_data.get(gilde.toLowerCase()).get(typ).containsKey(stats)){
				getMysql().asyncGetObject("SELECT "+stats.getMySQLName()+" FROM list_gilden_"+typ.getKuerzel()+"_data WHERE gilde= '"+gilde.toLowerCase()+"'", new Callback<Object>() {
					@Override
					public void call(Object value, Throwable exception) {
						if(value instanceof Double){
							gilden_data.get(gilde.toLowerCase()).get(typ).put(stats, value);
						}else if(value instanceof Integer){
							gilden_data.get(gilde.toLowerCase()).get(typ).put(stats, value);
						}else if(value instanceof String){
							gilden_data.get(gilde.toLowerCase()).get(typ).put(stats, value);
						}else{
							gilden_data.get(gilde.toLowerCase()).get(typ).put(stats, value);
						}
					}
				});
			}
		}
	}

	public void LoadExistGildeData(final String gilde,GildenType typ){
		if(gilden_data.containsKey(gilde.toLowerCase()))return;
		getMysql().asyncQuery("SELECT `gilde` FROM `list_gilden_"+typ.getKuerzel()+"_data` WHERE `gilde`='"+gilde.toLowerCase()+"'", new Callback<ResultSet>() {

			@Override
			public void call(ResultSet value, Throwable exception) {
				EnumMap<GildenType, EnumMap<StatsKey, Object>> map = new EnumMap<>(GildenType.class);
				gilden_data.put(gilde.toLowerCase(), map);
				map.put(typ, new EnumMap<>(StatsKey.class));
				loadTag(gilde.toLowerCase());
				loadStats(gilde.toLowerCase());
			}
		}, new Callback<Boolean>() {

			@Override
			public void call(Boolean value, Throwable exception) {
				createDataEintrag(gilde.toLowerCase(), typ);
				EnumMap<GildenType, EnumMap<StatsKey, Object>> map = new EnumMap<>(GildenType.class);
				gilden_data.put(gilde.toLowerCase(), map);
				map.put(typ, new EnumMap<>(StatsKey.class));
				loadTag(gilde.toLowerCase());
			}
		});
	}

	public void loadPlayer(Player player){
		if(this.gilden_player.containsKey(UtilPlayer.getPlayerId(player)))return;

		getMysql().asyncQuery("SELECT `gilde` FROM `list_gilden_"+typ.getKuerzel()+"_user` WHERE playerId='"+UtilPlayer.getPlayerId(player)+"'", new Callback<ResultSet>() {

			@Override
			public void call(ResultSet value, Throwable exception) {
				if(value != null){
					try {
						GildenPlayerPut(player, value.getString(1));
						LoadExistGildeData(value.getString(1).toLowerCase(), typ);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		},new Callback<Boolean>() {

			@Override
			public void call(Boolean value, Throwable exception) {
				GildenPlayerPut(player,"-");
			}
		});
	}

	public void removePlayerEintrag(Player player){
		removePlayerEintrag(UtilPlayer.getPlayerId(player),player.getName());
	}

	public void removePlayerEintrag(int playerId,String name){
		Bukkit.getPluginManager().callEvent(new GildePlayerLeaveEvent(getPlayerGilde(playerId), name,playerId, this));
		getGilden_players().remove(getPlayerGilde(playerId));
		getGilden_count().remove(getPlayerGilde(playerId));
		getGilden_player().remove(playerId);
		mysql.Update(isAsync(),"DELETE FROM list_gilden_"+typ.getKuerzel()+"_user WHERE playerId='" + playerId + "'");
	}

	public void createPlayerEintrag(Player player,String gilde){
		gilde=gilde.toLowerCase();
		GildenPlayerPut(player, gilde);
		mysql.Update(isAsync(),"INSERT INTO list_gilden_"+typ.getKuerzel()+"_user (playerId,gilde) VALUES ('"+UtilPlayer.getPlayerId(player)+"','"+gilde.toLowerCase()+"');");
		Bukkit.getPluginManager().callEvent(new GildePlayerJoinEvent(gilde, player, this));
	}

	public String getPlayerGilde(Player player){
		return getPlayerGilde(UtilPlayer.getPlayerId(player));
	}

	public String getPlayerGilde(int playerId){
		if(gilden_player.containsKey(playerId))return gilden_player.get(playerId);
		String gilde  = "-";
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gilde` FROM `list_gilden_"+typ.getKuerzel()+"_user` WHERE playerId='"+playerId+"'");

	      while (rs.next()) {
	    	  gilde=rs.getString(1);
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		gilde=gilde.toLowerCase();
		GildenPlayerPut(playerId,gilde);

		if(UtilDebug.isDebug())UtilDebug.debug("getPlayerGilde", new String[]{"Gilde:"+gilde,"PlayerId: "+playerId});

		return gilde;
	}

	public boolean isPlayerInGilde(Player player){
		return isPlayerInGilde(UtilPlayer.getPlayerId(player));
	}

	public boolean isPlayerInGilde(int playerId){
		if(gilden_player.containsKey(playerId)){
			if(gilden_player.get(playerId).equalsIgnoreCase("-"))return false;
			return true;
		}
		String g = getPlayerGilde(playerId);
		g=g.toLowerCase();
		if(!ExistGilde(g)){
			if(UtilPlayer.isOnline(playerId)){
				removePlayerEintrag(UtilPlayer.searchExact(playerId));
			}
		}

		return !g.equalsIgnoreCase("-");
	}

	public boolean ExistGildeData(String gilde,GildenType typ){
		gilde=gilde.toLowerCase();
		boolean done = false;
		if(gilden_data.containsKey(gilde))return true;
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gilde` FROM `list_gilden_"+typ.getKuerzel()+"_data` WHERE gilde='"+gilde.toLowerCase()+"'");

	      while (rs.next()) {
	    	  done=true;
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }

		EnumMap<GildenType, EnumMap<StatsKey, Object>> map = new EnumMap<>(GildenType.class);
		gilden_data.put(gilde, map);
		map.put(typ, new EnumMap<>(StatsKey.class));
		if(!done)createDataEintrag(gilde, typ);
		return done;
	}

	public void AllUpdateGilde(){
		for(String g : gilden_data_musst_saved.keySet()){
			if(!gilden_data.containsKey(g))continue;
			for(GildenType typ : gilden_data_musst_saved.get(g).keySet()){
				if(!gilden_data.get(g).containsKey(typ))continue;
				for(StatsKey s : gilden_data_musst_saved.get(g).get(typ)){
					if(!s.isMysql())continue;

					if(this.gilden_data.get(g).get(typ).containsKey(s)){
						Object o = gilden_data.get(g).get(typ).get(s);

						if(UtilDebug.isDebug()){
							UtilDebug.debug("AllUpdateGilde", new String[]{"Gilde:"+g,"Type:"+typ.name()+"Stats:"+s.getContraction(),"OBJ:"+o});
						}

						if(o instanceof Integer){
							mysql.Update(isAsync(),"UPDATE list_gilden_"+typ.getKuerzel()+"_data SET "+s.getMySQLName()+"='"+ o +"' WHERE gilde='" + g.toLowerCase() + "'");
						}else if(o instanceof String){
							mysql.Update(isAsync(),"UPDATE list_gilden_"+typ.getKuerzel()+"_data SET "+s.getMySQLName()+"='"+ o +"' WHERE gilde='" + g.toLowerCase() + "'");
						}else if(o instanceof Double){
							mysql.Update(isAsync(),"UPDATE list_gilden_"+typ.getKuerzel()+"_data SET "+s.getMySQLName()+"='"+ o +"' WHERE gilde='" + g.toLowerCase() + "'");
						}
					}
				}
			}
		}
		gilden_data_musst_saved.clear();
	}

	public int getAnzahl(String gilde){
		gilde=gilde.toLowerCase();
		if(getGilden_count().containsKey(gilde)){
			return getGilden_count().get(gilde);
		}

		int i = getMysql().getInt("SELECT COUNT(*) FROM `list_gilden_"+typ.getKuerzel()+"_user` WHERE gilde='"+gilde.toLowerCase()+"'");
		getGilden_count().put(gilde, i);
		return i;
	}

	public void UpdateGilde(String gilde,GildenType typ){
		gilde=gilde.toLowerCase();
		if(!gilden_data_musst_saved.containsKey(gilde))return;
		if(!gilden_data_musst_saved.get(gilde).containsKey(typ))return;
		if(!gilden_data.containsKey(gilde)){
			if(UtilDebug.isDebug()){
				UtilDebug.debug("UpdateGilde", new String[]{"GILDE NOT FOUND","Gilde:"+gilde,"Type:"+typ.name()});
			}
			return;
		}
		if(!gilden_data.get(gilde).containsKey(typ)){
			if(UtilDebug.isDebug()){
				UtilDebug.debug("UpdateGilde", new String[]{"TYP NOT FOUND","Gilde:"+gilde,"Type:"+typ.name()});
			}
			return;
		}

		StatsKey s;
		for(int i = 0; i<gilden_data.get(gilde).get(typ).size();i++){
			s=(StatsKey)gilden_data.get(gilde).get(typ).keySet().toArray()[i];

			if(!this.gilden_data_musst_saved.containsKey(gilde))break;
			if(!this.gilden_data_musst_saved.get(gilde).containsKey(typ))break;
			if(!gilden_data_musst_saved.get(gilde).get(typ).contains(s))continue;
			if(!s.isMysql()){
				this.gilden_data_musst_saved.get(gilde).get(typ).remove(s);
				continue;
			}

			Object o = gilden_data.get(gilde).get(typ).get(s);

			if(UtilDebug.isDebug())UtilDebug.debug("UpdateGilde", new String[]{"Gilde:"+gilde,"Type:"+typ.name(),"Stats:"+s.getContraction(),"OBJ:"+o});

			if(o instanceof Integer){
				if(mysql.Update("UPDATE list_gilden_"+typ.getKuerzel()+"_data SET "+s.getMySQLName()+"='"+ o +"' WHERE gilde='" + gilde.toLowerCase() + "'")){
					this.gilden_data_musst_saved.get(gilde).get(typ).remove(s);
				}
			}else if(o instanceof String){
				if(mysql.Update("UPDATE list_gilden_"+typ.getKuerzel()+"_data SET "+s.getMySQLName()+"='"+ o +"' WHERE gilde='" + gilde.toLowerCase() + "'")){
					this.gilden_data_musst_saved.get(gilde).get(typ).remove(s);
				}
			}else if(o instanceof Double){
				if(mysql.Update("UPDATE list_gilden_"+typ.getKuerzel()+"_data SET "+s.getMySQLName()+"='"+ o +"' WHERE gilde='" + gilde.toLowerCase() + "'")){
					this.gilden_data_musst_saved.get(gilde).get(typ).remove(s);
				}
			}

			if(this.gilden_data_musst_saved.containsKey(gilde)&&this.gilden_data_musst_saved.get(gilde).get(typ).isEmpty()){
				this.gilden_data_musst_saved.get(gilde).remove(typ);
			}
		}

		if(this.gilden_data_musst_saved.containsKey(gilde)&&this.gilden_data_musst_saved.get(gilde).isEmpty()){
			this.gilden_data_musst_saved.remove(gilde);
		}
	}

	public boolean ExistGilde(String gilde){
		gilde=gilde.toLowerCase();
		boolean done = false;
		if(gilden_tag.containsKey(gilde))return true;
		String i = "";
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gildentag` FROM `list_gilden_"+typ.getKuerzel()+"` WHERE gilde='"+gilde.toLowerCase()+"'");
	      while (rs.next()) {
	    		  done=true;
	    		  i=rs.getString(1);
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }

		if(done)gilden_tag.put(gilde, i);
		return done;
	}

	public String getTag(String gilde){
		gilde=gilde.toLowerCase();
		if(gilden_tag.containsKey(gilde))return gilden_tag.get(gilde);
		String tag = "";
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gildentag` FROM `list_gilden_"+typ.getKuerzel()+"` WHERE gilde='"+gilde.toLowerCase()+"'");

	      while (rs.next()) {
	    	  tag=rs.getString(1);
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }

		gilden_tag.put(gilde,tag);
		return tag;
	}

	public void GildenPlayerPut(Player player,String Gilde){
		GildenPlayerPut(UtilPlayer.getPlayerId(player),Gilde);
	}

	public void GildenPlayerPut(Player player){
		GildenPlayerPut(UtilPlayer.getPlayerId(player));
	}

	public void GildenPlayerPut(int playerId){
		GildenPlayerPut(playerId, getPlayerGilde(playerId));
	}

	public void GildenPlayerPut(int playerId,String gilde){
		gilde=gilde.toLowerCase();
		if(getGilden_player().containsKey(playerId))getGilden_player().remove(playerId);
		getGilden_player().put(playerId, gilde);
		Bukkit.getPluginManager().callEvent(new GildeLoadEvent(getGilden_player().get(playerId),this));
	}

	public void getMember(String gilde){
		gilde=gilde.toLowerCase();
		if(getGilden_count().containsKey(gilde.toLowerCase()))return;
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `playerId` FROM `list_gilden_"+typ.getKuerzel()+"_user` WHERE gilde='"+gilde.toLowerCase()+"'");

	      while (rs.next()) {
	    	  if(gilden_player.containsKey(Integer.parseInt(rs.getString(1).toLowerCase())))continue;
	    	  GildenPlayerPut(rs.getInt(1), gilde);
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		getAnzahl(gilde);
	}

	public void setDouble(String gilde,double d, StatsKey s){
		setDouble(gilde, getTyp(), d, s);
	}

	public void setDouble(String gilde,GildenType typ,double i,StatsKey s){
		gilde=gilde.toLowerCase();
		if(!ExistGilde(gilde))return ;
		ExistGildeData(gilde, typ);
		if(gilden_data.get(gilde).get(typ).containsKey(s))gilden_data.get(gilde).get(typ).remove(s);
		gilden_data.get(gilde).get(typ).put(s, i);
		if(!gilden_data_musst_saved.containsKey(gilde)) {
			gilden_data_musst_saved.put(gilde, new EnumMap<>(GildenType.class));
		}
		EnumMap<GildenType, ArrayList<StatsKey>> gildenData = gilden_data_musst_saved.get(gilde);
		if(!gildenData.containsKey(typ)) {
			gildenData.put(typ, new ArrayList<>());
		}
		if(!gildenData.get(typ).contains(s)) {
			gildenData.get(typ).add(s);
		}
	}

	public double getDouble(StatsKey s,String gilde){
		return getDouble(s, gilde,getTyp());
	}

	public double getDouble(StatsKey s, String gilde, GildenType typ) {
		gilde = gilde.toLowerCase();
		if (!ExistGilde(gilde)) return 0.0;
		ExistGildeData(gilde, typ);
		EnumMap<GildenType, EnumMap<StatsKey, Object>> gildenDatas = gilden_data.get(gilde);

		if (gildenDatas != null) {
			EnumMap<StatsKey, Object> gildenTypData = gildenDatas.get(typ);
			if (gildenTypData != null && gildenTypData.containsKey(s)) {
				return UtilNumber.toDouble(gildenTypData.get(s));
			}
		}
		double i = -1;
		try {
			ResultSet rs = mysql.Query("SELECT " + s.getMySQLName() + " FROM `list_gilden_" + typ.getKuerzel() + "_data` WHERE gilde= '" + gilde.toLowerCase() + "'");
			while (rs.next()) {
				i = rs.getDouble(1);
			}
			rs.close();
		} catch (Exception err) {
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY, err, getMysql()));
		}

		if (gildenDatas == null) {
			gildenDatas = new EnumMap<>(GildenType.class);
			gilden_data.put(gilde, gildenDatas);
		}
		EnumMap<StatsKey, Object> gildenTypData = gildenDatas.get(typ);
		if (gildenTypData == null) {
			gildenTypData = new EnumMap<>(StatsKey.class);
			gildenDatas.put(typ, gildenTypData);
		}
		gildenTypData.put(s, i);
		return i;
	}

	public void setInt(String gilde,int i, StatsKey s){
		setInt(gilde, getTyp(), i, s);
	}

	public void setInt(String gilde,GildenType typ,int i,StatsKey s){
		gilde=gilde.toLowerCase();
		if(!ExistGilde(gilde))return;
		ExistGildeData(gilde, typ);
		if(gilden_data.get(gilde).get(typ).containsKey(s))gilden_data.get(gilde).get(typ).remove(s);
		gilden_data.get(gilde).get(typ).put(s, i);
		if(!gilden_data_musst_saved.containsKey(gilde))gilden_data_musst_saved.put(gilde, new EnumMap<>(GildenType.class));
		if(!gilden_data_musst_saved.get(gilde).containsKey(typ))gilden_data_musst_saved.get(gilde).put(typ, new ArrayList<>());
		if(!gilden_data_musst_saved.get(gilde).get(typ).contains(s))gilden_data_musst_saved.get(gilde).get(typ).add(s);
	}

	public Integer getInt(StatsKey s,String gilde){
		return getInt(s, gilde,getTyp());
	}

	public Integer getInt(StatsKey s,String gilde,GildenType typ){
		gilde=gilde.toLowerCase();
		if(!ExistGilde(gilde))return null;
		ExistGildeData(gilde, typ);
		if(gilden_data.containsKey(gilde)&&gilden_data.get(gilde).containsKey(typ)&&gilden_data.get(gilde).get(typ).containsKey(s)){
			return UtilNumber.toInt(gilden_data.get(gilde).get(typ).get(s));
		}
		int i = -1;
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getMySQLName()+" FROM `list_gilden_"+typ.getKuerzel()+"_data` WHERE gilde= '"+gilde.toLowerCase()+"'");
			while(rs.next()){
				i=rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
		}

		if(!gilden_data.containsKey(gilde))gilden_data.put(gilde, new EnumMap<>(GildenType.class));
		if(!gilden_data.get(gilde).containsKey(typ))gilden_data.get(gilde).put(typ, new EnumMap<>(StatsKey.class));
		gilden_data.get(gilde).get(typ).put(s, i);
		return i;
	}

	public Integer getRank(String gilde,GildenType typ,StatsKey s){
		gilde=gilde.toLowerCase();
		if(!ExistGilde(gilde))return null;
		ExistGildeData(gilde, typ);

		boolean done = false;
		int n = -1;

		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gilde`,`typ` FROM `list_gilden_"+typ.getKuerzel()+"_data` WHERE ORDER BY `"+s.getMySQLName()+"` DESC;");

	      while ((rs.next()) && (!done)) {
	    	 n++;
	        if (rs.getString(1).equalsIgnoreCase(gilde)) {
	          done = true;
	        }
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }

	    return n;
	}

	public void setString(String gilde,String i, StatsKey s){
		setString(gilde, getTyp(), i, s);
	}

	public String[] getGildenPlayersName(String gilde){
		gilde=gilde.toLowerCase();
		if(gilden_players.containsKey(gilde))return gilden_players.get(gilde);
		int anzahl = 0;
		for(int n : getGilden_player().keySet())if(getGilden_player().get(n).equalsIgnoreCase(gilde))anzahl++;
		if(anzahl==0)return null;
		String[] players = new String[anzahl];

		try{
			anzahl=0;
			ResultSet rs = mysql.Query("SELECT playerId FROM `list_gilden_"+typ.getKuerzel()+"_user` WHERE gilde= '"+gilde.toLowerCase()+"'");
			while(rs.next()){
				players[anzahl]=UtilServer.getClient().getPlayerAndLoad(rs.getInt(1)).getName();
				anzahl++;
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
		}
		gilden_players.put(gilde, players);
		return players;
	}

	public void setString(String gilde,GildenType typ,String i,StatsKey s){
		gilde=gilde.toLowerCase();
		if(!ExistGilde(gilde))return ;
		ExistGildeData(gilde, typ);
		if(gilden_data.get(gilde).get(typ).containsKey(s))gilden_data.get(gilde).get(typ).remove(s);
		gilden_data.get(gilde).get(typ).put(s, i);
		if(!gilden_data_musst_saved.containsKey(gilde))gilden_data_musst_saved.put(gilde, new EnumMap<>(GildenType.class));
		if(!gilden_data_musst_saved.get(gilde).containsKey(typ))gilden_data_musst_saved.get(gilde).put(typ, new ArrayList<>());
		if(!gilden_data_musst_saved.get(gilde).get(typ).contains(s))gilden_data_musst_saved.get(gilde).get(typ).add(s);
	}

	public int getOwner(String gilde){
		gilde=gilde.toLowerCase();
		if(!ExistGilde(gilde))return 0;
		ExistGildeData(gilde, typ);

		int i = 0;
		try{
			ResultSet rs = mysql.Query("SELECT owner_playerId FROM `list_gilden_"+typ.getKuerzel()+"` WHERE gilde= '"+gilde.toLowerCase()+"'");
			while(rs.next()){
				i=rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
		}
		return i;
	}

	public String getString(StatsKey s,String gilde){
		return getString(s, gilde,getTyp());
	}

	public String getString(StatsKey s,String gilde,GildenType typ){
		gilde=gilde.toLowerCase();
		if(!ExistGilde(gilde))return null;
		ExistGildeData(gilde, typ);
		if(gilden_data.containsKey(gilde)&&gilden_data.get(gilde).containsKey(typ)&&gilden_data.get(gilde).get(typ).containsKey(s)){
			return (String)gilden_data.get(gilde).get(typ).get(s);
		}
		String i = "";
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getMySQLName()+" FROM list_gilden_"+typ.getKuerzel()+"_data WHERE gilde= '"+gilde.toLowerCase()+"'");
			while(rs.next()){
				i=rs.getString(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
		}

		if(!gilden_data.containsKey(gilde))gilden_data.put(gilde, new EnumMap<>(GildenType.class));
		if(!gilden_data.get(gilde).containsKey(typ))gilden_data.get(gilde).put(typ, new EnumMap<>(StatsKey.class));
		gilden_data.get(gilde).get(typ).put(s, i);
		return i;
	}

	public void createGildenEintrag(String gilde,String gildentag,int member,int founder){
		String t = "INSERT INTO list_gilden_"+typ.getKuerzel()+" (gilde,gildentag,member,founder_playerId,owner_playerId) VALUES ('"+gilde.toLowerCase()+"','"+gildentag+"','"+member+"','"+founder+"','"+founder+"');";
		mysql.Update(isAsync(),t);
	}

	public void createDataEintrag(String gilde,GildenType typ){
		StatsKey[] stats = typ.getStats();
		String tt = "gilde,";
		String ti = "'"+gilde.toLowerCase()+"',";
		for(StatsKey s : stats){
			tt=tt+s.getMySQLName()+",";
			ti=ti+"'0',";
		}
		String t = "INSERT INTO list_gilden_"+typ.getKuerzel()+"_data ("+tt.substring(0, tt.length()-1)+") VALUES ("+ti.subSequence(0, ti.length()-1)+");";
		mysql.Update(isAsync(),t);

		for(StatsKey s : typ.getStats()){
			if(StatsKey.LOC_X!=s&&StatsKey.LOC_Y!=s&&StatsKey.LOC_Z!=s&&StatsKey.WORLD!=s){
				this.gilden_data.get(gilde.toLowerCase()).get(typ).put(s, 0);
			}
		}
	}

	public void CreateTable(){
		String tt = "gilde varchar(30),";
		for(StatsKey s : getTyp().getStats()){
			tt=tt+s.getMySQLSyntax()+",";
		}
		mysql.Update(isAsync(),"CREATE TABLE IF NOT EXISTS list_gilden_"+typ.getKuerzel()+"_data("+tt.substring(0, tt.length()-1)+")");
	}

	public void onKick(String kick_o, int kick_id) {
	}

	public boolean onCreate(Player p) {
		return false;
	}

	public boolean onHomeUseSet(Player p, String g) {
		return false;
	}

	public void onHomeAdminUse(Player p, String arg) {
	}

	public void onOwnerLeave(String g) {
	}

	public void onMemberLeave(Player p) {
	}
}
