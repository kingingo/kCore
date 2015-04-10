package me.kingingo.kcore.Gilden;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kSkyblock.SkyBlockManager;
import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.Events.GildeLoadEvent;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.PlayerStats.Stats;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GildenManager implements Listener {

	@Getter
	private JavaPlugin instance;
	@Getter
	private HashMap<UUID,String> gilden_player = new HashMap<>();
	@Getter
	protected HashMap<String,String> gilden_tag = new HashMap<>();
	@Getter
	protected HashMap<String,HashMap<GildenType,HashMap<Stats,Object>>> gilden_data = new HashMap<>();
	@Getter
	protected HashMap<String,HashMap<GildenType,ArrayList<Stats>>> gilden_data_musst_saved = new HashMap<>();
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
	HashMap<Integer,String> ranking = new HashMap<>();
	@Getter
	HashMap<String,Integer> extra_prefix = new HashMap<>();
	
	public GildenManager(JavaPlugin instance,MySQL mysql,GildenType typ,CommandHandler cmd){
		this.instance=instance;
		this.typ=typ;
		this.mysql=mysql;
		mysql.Update("CREATE TABLE IF NOT EXISTS list_gilden_"+typ.getKürzel()+"(gilde varchar(30),gildentag varchar(30),member int,founder_uuid varchar(100),owner_uuid varchar(100))");
		mysql.Update("CREATE TABLE IF NOT EXISTS list_gilden_"+typ.getKürzel()+"_user(player varchar(30),UUID varchar(100),gilde varchar(30))");
		CreateTable();
		cmd.register(CommandGilde.class, new CommandGilde(this));
		Bukkit.getPluginManager().registerEvents(this, getInstance());
		
		for(Player p : UtilServer.getPlayers()){
			if(!isPlayerInGilde(UtilPlayer.getRealUUID(p)))continue;
			if(gilden_player.containsKey( UtilPlayer.getRealUUID(p) ))gilden_player.remove( UtilPlayer.getRealUUID(p) );
			getPlayerGilde(p);
		}
		
		LoadRanking();
	}
	
	@EventHandler
	public void Ranking(UpdateEvent ev){
		if(ev.getType()!=UpdateType.MIN_08)return;
		extra_prefix.clear();
		try{
		     ResultSet rs = getMysql().Query("SELECT `kills`,`gilde` FROM `list_gilden_"+typ.getKürzel()+"_data` ORDER BY kills DESC LIMIT 15;");

		      int zahl = 1;
		      
		      while (rs.next()) {
		  			if(zahl==1){
		  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§4§l " + rs.getString(2));
		  			}else if(zahl==2){
		  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§2§l " + rs.getString(2));
		  			}else if(zahl==3){
		  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§e§l " + rs.getString(2));
		  			}else if(zahl>=4 && zahl<=6){
		  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§3 " + rs.getString(2));
		  			}else if(zahl>=7 && zahl<=9){
		  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§d " + rs.getString(2));
		  			}else if(zahl>=10 && zahl<=12){
		  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§a " + rs.getString(2));
		  			}else if(zahl>=13 && zahl<=15){
		  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§b " + rs.getString(2));
		  			}else{
		  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§6 " + rs.getString(2));
		  			}
			     extra_prefix.put(rs.getString(2).toLowerCase(), zahl);
			     zahl++;
		      }

		      rs.close();
		 } catch (Exception err) {
		      System.out.println("MySQL-Error: " + err.getMessage());
		 }
		
	}
	
	public void LoadRanking(){
		if(ranking.isEmpty()){
			extra_prefix.clear();
			try{
			     ResultSet rs = getMysql().Query("SELECT `kills`,`gilde` FROM `list_gilden_"+typ.getKürzel()+"_data` ORDER BY kills DESC LIMIT 15;");

			      int zahl = 1;
			      
			      while (rs.next()) {
			    	  if(zahl==1){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§4§l " + rs.getString(2));
			  			}else if(zahl==2){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§2§l " + rs.getString(2));
			  			}else if(zahl==3){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§e§l " + rs.getString(2));
			  			}else if(zahl>=4 && zahl<=6){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§3 " + rs.getString(2));
			  			}else if(zahl>=7 && zahl<=9){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§d " + rs.getString(2));
			  			}else if(zahl>=10 && zahl<=12){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§a " + rs.getString(2));
			  			}else if(zahl>=13 && zahl<=15){
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§b " + rs.getString(2));
			  			}else{
			  				ranking.put(zahl, "§b#§6" + String.valueOf(zahl) + "§b | §6" + String.valueOf(rs.getInt(1)) + " §b|§6 " + rs.getString(2));
			  			}
				     extra_prefix.put(rs.getString(2).toLowerCase(), zahl);
				     zahl++;
			      }

			      rs.close();
			 } catch (Exception err) {
			      System.out.println("MySQL-Error: " + err.getMessage());
			 }
		}
	}
	
	public void Ranking(Player p){
		p.sendMessage("§b■■■■■■■■ §6§lGilden Ranking | Top 15 §b■■■■■■■■");
		p.sendMessage("§b Place | Kills | Gilde");
		LoadRanking();
		for(Integer i : ranking.keySet())p.sendMessage(ranking.get(i));
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void DamageTeam(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player && ev.getDamager() instanceof Player){
			Player attack = (Player)ev.getDamager();
			Player defend = (Player)ev.getEntity();
			if(isPlayerInGilde(UtilPlayer.getRealUUID(attack))&&isPlayerInGilde(UtilPlayer.getRealUUID(defend))){
				if(getPlayerGilde(attack).equalsIgnoreCase(getPlayerGilde(defend))){
					ev.setCancelled(true);
				}
			}
		}else if(ev.getEntity() instanceof Player && ev.getDamager() instanceof Projectile){
			Projectile attack = (Projectile)ev.getDamager();
			Player defend = (Player)ev.getEntity();
			if(!(attack.getShooter() instanceof Player))return;
			if(isPlayerInGilde(UtilPlayer.getRealUUID( ((Player)attack.getShooter())) )&&isPlayerInGilde(UtilPlayer.getRealUUID( defend ) )){
				if(getPlayerGilde(((Player)attack.getShooter())).equalsIgnoreCase(getPlayerGilde(defend))){
					ev.setCancelled(true);
				}
			}
		}
	}
	
	String g;
	@EventHandler
	public void Death(PlayerDeathEvent ev){
		ev.setDeathMessage(null);
		if(ev.getEntity() instanceof Player){
			Player v = (Player)ev.getEntity();
			if(isPlayerInGilde(v)){
				g=getPlayerGilde(v);
				setInt(g, getInt(Stats.DEATHS, g)+1, Stats.DEATHS);
			}
			if(ev.getEntity().getKiller() instanceof Player&&isPlayerInGilde(ev.getEntity().getKiller())){
				g=getPlayerGilde(ev.getEntity().getKiller());
				setInt(g, getInt(Stats.KILLS, g)+1, Stats.KILLS);
			}
		}
	}
	
//	ArrayList<Player> TP = new ArrayList<>();
//	@EventHandler
//	public void Update(UpdateEvent ev){
//		if(ev.getType()!=UpdateType.FAST)return;
//		if(teleport.isEmpty())return;
//		for(Player p : teleport.keySet()){
//			if(teleport.get(p) <= System.currentTimeMillis()){
//				TP.add(p);
//				if(teleport_loc.get(p).getX()==p.getLocation().getX()&&teleport_loc.get(p).getY()==p.getLocation().getY()&&teleport_loc.get(p).getZ()==p.getLocation().getZ()){
//					TeleportToHome(p);
//				}else{
//					p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_TELEPORT_CANCELLED.getText());
//				}
//			}
//		}
//		for(Player p : TP){
//			teleport.remove(p);
//			teleport_loc.remove(p);
//		}
//	}
	
	double dif_x;
	double dif_y;
	double dif_z;
	public boolean hasMoved(Location loc,Player player){
		if(loc.getBlockX() == player.getLocation().getBlockX()&&loc.getBlockY() == player.getLocation().getBlockY()&&loc.getBlockZ() == player.getLocation().getBlockZ()){
			return false;
		}else{
			dif_x=Math.abs(loc.getX()-player.getLocation().getX());
			dif_y=Math.abs(loc.getY()-player.getLocation().getY());
			dif_z=Math.abs(loc.getZ()-player.getLocation().getZ());
			
			if(dif_x<0.6&&dif_y<0.6&&dif_z<0.6){
				return false;
			}else{
				return true;
			}
		}
	}
	
	Player p;
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FAST)return;
		if(teleport.isEmpty())return;
		for(int i = 0; i < teleport.size(); i++){
			p=((Player)teleport.keySet().toArray()[i]);
			
			if(teleport.get(p) <= System.currentTimeMillis()){
				
				if(!p.isOnline()){
					teleport.remove(p);
					teleport_loc.remove(p);
					continue;
				}
				
				if(!hasMoved(teleport_loc.get(p), p)){
				//if(teleport_loc.get(p).getX()==p.getLocation().getX()&&teleport_loc.get(p).getY()==p.getLocation().getY()&&teleport_loc.get(p).getZ()==p.getLocation().getZ()){
					TeleportToHome(p);
				}else{
					p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_TELEPORT_CANCELLED.getText());
				}
				teleport.remove(p);
				teleport_loc.remove(p);
			}else{
				p.sendMessage(Text.PREFIX.getText()+Text.GILDE_HOME.getText( UtilTime.formatMili( (teleport.get(p)-System.currentTimeMillis()) ) ));
			}
		}
	}
	
	public void TeleportToHome(Player p){
		try{
			if(!isPlayerInGilde(p)){
				p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_NOT_IN_GILDE.getText());
				return;
			}
			String g = getPlayerGilde(p);
			String w = getString(Stats.WORLD, g, getTyp());
			int x = getInt(Stats.LOC_X, g, typ);
			int y = getInt(Stats.LOC_Y, g, typ);
			int z = getInt(Stats.LOC_Z, g, typ);
			if(Bukkit.getWorld(w)==null)return;
			if(x==0&&y==0&&z==0&&g.equalsIgnoreCase("0"))return;
			Location loc = new Location(Bukkit.getWorld(w),x,y,z);
			p.teleport(loc);
			p.sendMessage(Text.PREFIX.getText()+Text.GILDE_TELEPORTET.getText());
		}catch(NullPointerException e){
			
		}	
	}
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent ev){
		if(isOnDisable())return;
		if(!isPlayerInGilde(ev.getPlayer()))return;
		UpdateGilde(getPlayerGilde(ev.getPlayer()), getTyp());
		sendGildenChat(getPlayerGilde(ev.getPlayer()), Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_LEAVE.getText(ev.getPlayer().getName()));
	}
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent ev){
		if(gilden_player.containsKey( UtilPlayer.getRealUUID(ev.getPlayer()) ))gilden_player.remove( UtilPlayer.getRealUUID(ev.getPlayer()) );
		if(!isPlayerInGilde(ev.getPlayer()))return;
		sendGildenChat(getPlayerGilde(ev.getPlayer()), Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_JOIN.getText(ev.getPlayer().getName()));
	}
	
	public void sendGildenChat(String gilde,String msg){
		Player p;
		for(UUID n : gilden_player.keySet()){
			if(gilden_player.get(n).equalsIgnoreCase(gilde)){
				if(UtilPlayer.isOnline(n)){
					Bukkit.getPlayer(n).sendMessage(msg);
				}
			}
		}
	}
	
	public double getKDR(int k,int d){
		double kdr = (double)k/(double)d;
		kdr = kdr * 100;
		kdr = Math.round(kdr);
		kdr = kdr / 100;
		return kdr;
	}

	public void removeGildenEintrag(String name){
		ArrayList<UUID> l = new ArrayList<>();
		getMember(name);
		for(UUID n : getGilden_player().keySet()){
			if( getGilden_player().get(n).equalsIgnoreCase(name) ){
				l.add(n);
			}
		}
		for(UUID n : l){
			getGilden_player().remove(n);
		}
		mysql.Update("DELETE FROM list_gilden_"+typ.getKürzel()+" WHERE gilde='" + name.toLowerCase() + "'");
		mysql.Update("DELETE FROM list_gilden_"+typ.getKürzel()+"_data WHERE gilde='" + name.toLowerCase() + "'");
		mysql.Update("DELETE FROM list_gilden_"+typ.getKürzel()+"_user WHERE gilde='" + name.toLowerCase() + "'");
		gilden_data.remove(name);
		gilden_tag.remove(name);
		gilden_data_musst_saved.remove(name);
	}
	
	public void removePlayerEintrag(Player player){
		removePlayerEintrag(UtilPlayer.getRealUUID(player),player.getName());
	}
	
	public void removePlayerEintrag(UUID uuid,String name){
		getGilden_count().remove(getPlayerGilde(uuid));
		getGilden_player().remove(uuid);
		mysql.Update("DELETE FROM list_gilden_"+typ.getKürzel()+"_user WHERE uuid='" + uuid + "'");
		mysql.Update("DELETE FROM list_gilden_"+typ.getKürzel()+"_user WHERE player='" + name.toLowerCase() + "'");
	}
	
	public void createPlayerEintrag(Player player,String gilde){
		GildenPlayerPut(player, gilde);
		mysql.Update("INSERT INTO list_gilden_"+typ.getKürzel()+"_user (player,uuid,gilde) VALUES ('"+player.getName().toLowerCase()+"','"+UtilPlayer.getRealUUID(player)+"','"+gilde.toLowerCase()+"');");
	}
	
	public String getPlayerGilde(Player player){
		return getPlayerGilde(UtilPlayer.getRealUUID(player));
	}
	
	public String getPlayerGilde(UUID uuid){
		if(gilden_player.containsKey(uuid))return gilden_player.get(uuid);
		String g  = "-";
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gilde` FROM `list_gilden_"+typ.getKürzel()+"_user` WHERE uuid='"+uuid+"'");

	      while (rs.next()) {
	    		g=rs.getString(1);
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		GildenPlayerPut(uuid,g);
		
		return g;
	}
	
	public boolean isPlayerInGilde(Player player){
		return isPlayerInGilde(UtilPlayer.getRealUUID(player));
	}
	
	public boolean isPlayerInGilde(UUID uuid){
		if(gilden_player.containsKey(uuid)){
			if(gilden_player.get(uuid).equalsIgnoreCase("-"))return false;
			return true;
		}
		boolean b = false;
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gilde` FROM `list_gilden_"+typ.getKürzel()+"_user` WHERE uuid='"+uuid+"'");
	      while (rs.next()) {
	    	b=Boolean.valueOf(true);
	      }
	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		return b;
	}
	
	public boolean ExistGildeData(String gilde,GildenType typ){
		boolean done = false;
		if(gilden_data.containsKey(gilde))return true;
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gilde` FROM `list_gilden_"+typ.getKürzel()+"_data` WHERE gilde='"+gilde.toLowerCase()+"'");

	      while (rs.next()) {
	    	  done=true;
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		
		if(!done)createDataEintrag(gilde, typ);
		
		gilden_data.put(gilde,new HashMap<GildenType,HashMap<Stats,Object>>());
		gilden_data.get(gilde).put(typ, new HashMap<Stats,Object>());
		return done;
	}
	
	public void AllUpdateGilde(){
		for(String g : gilden_data_musst_saved.keySet()){
			for(GildenType typ : gilden_data_musst_saved.get(g).keySet()){
				for(Stats s : gilden_data_musst_saved.get(g).get(typ)){
					if(!s.isMysql())continue;
					Object o = gilden_data.get(g).get(typ).get(s);
					if(o instanceof Integer){
						mysql.Update("UPDATE list_gilden_"+typ.getKürzel()+"_data SET "+s.getTYP()+"='"+((Integer)o)+"' WHERE gilde='" + g.toLowerCase() + "'");
					}else if(o instanceof String){
						mysql.Update("UPDATE list_gilden_"+typ.getKürzel()+"_data SET "+s.getTYP()+"='"+((String)o)+"' WHERE gilde='" + g.toLowerCase() + "'");
					}else if(o instanceof Double){
						mysql.Update("UPDATE list_gilden_"+typ.getKürzel()+"_data SET "+s.getTYP()+"='"+((Double)o)+"' WHERE gilde='" + g.toLowerCase() + "'");
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
		
		int i = getMysql().getInt("SELECT COUNT(*) FROM `list_gilden_"+typ.getKürzel()+"_user` WHERE gilde='"+gilde.toLowerCase()+"'");
		getGilden_count().put(gilde, i);
		return i;
	}
	
	public void UpdateGilde(String gilde,GildenType typ){
		if(!gilden_data_musst_saved.containsKey(gilde))return;
		if(!gilden_data_musst_saved.get(gilde).containsKey(typ))return;
		for(Stats s : gilden_data.get(gilde).get(typ).keySet()){
			if(!gilden_data_musst_saved.get(gilde).get(typ).contains(s))continue;;
			if(!s.isMysql())continue;
			Object o = gilden_data.get(gilde).get(typ).get(s);
			if(o instanceof Integer){
				mysql.Update("UPDATE list_gilden_"+typ.getKürzel()+"_data SET "+s.getTYP()+"='"+((Integer)o)+"' WHERE gilde='" + gilde.toLowerCase() + "'");
			}else if(o instanceof String){
				mysql.Update("UPDATE list_gilden_"+typ.getKürzel()+"_data SET "+s.getTYP()+"='"+((String)o)+"' WHERE gilde='" + gilde.toLowerCase() + "'");
			}else if(o instanceof Double){
				mysql.Update("UPDATE list_gilden_"+typ.getKürzel()+"_data SET "+s.getTYP()+"='"+((Double)o)+"' WHERE gilde='" + gilde.toLowerCase() + "'");
			}
		}
		gilden_data_musst_saved.get(gilde).remove(typ);
	}
	
	public boolean ExistGilde(String gilde){
		boolean done = false;
		if(gilden_tag.containsKey(gilde))return true;
		String i = "";
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gildentag` FROM `list_gilden_"+typ.getKürzel()+"` WHERE gilde='"+gilde.toLowerCase()+"'");
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
		String tag = "";
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gildentag` FROM `list_gilden_"+typ.getKürzel()+"` WHERE gilde='"+gilde.toLowerCase()+"'");

	      while (rs.next()) {
	    		  tag=rs.getString(1);
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		return tag;
	}
	
	public void GildenPlayerPut(Player player,String Gilde){
		GildenPlayerPut(UtilPlayer.getRealUUID(player),Gilde);
	}
	
	public void GildenPlayerPut(Player player){
		GildenPlayerPut(UtilPlayer.getRealUUID(player));
	}
	
	public void GildenPlayerPut(UUID uuid){
		GildenPlayerPut(uuid, getPlayerGilde(uuid));
	}
	
	public void GildenPlayerPut(UUID uuid,String gilde){
		if(getGilden_player().containsKey(uuid))getGilden_player().remove(uuid);
		getGilden_player().put(uuid, gilde);
		Bukkit.getPluginManager().callEvent(new GildeLoadEvent(getGilden_player().get(uuid),this));
	}
	
	public void getMember(String gilde){
		if(getGilden_count().containsKey(gilde.toLowerCase()))return;
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `uuid` FROM `list_gilden_"+typ.getKürzel()+"_user` WHERE gilde='"+gilde.toLowerCase()+"'");

	      while (rs.next()) {
	    	  if(gilden_player.containsKey(rs.getString(1).toLowerCase()))continue;
	    	  GildenPlayerPut(UUID.fromString(rs.getString(1)), gilde);
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		getAnzahl(gilde);
	}
	
	public void setInt(String gilde,int i, Stats s){
		setInt(gilde, getTyp(), i, s);
	}
	
	public void setInt(String gilde,GildenType typ,int i,Stats s){
		if(!ExistGilde(gilde))return ;
		ExistGildeData(gilde, typ);
		if(gilden_data.get(gilde).get(typ).containsKey(s))gilden_data.get(gilde).get(typ).remove(s);
		gilden_data.get(gilde).get(typ).put(s, i);
		if(!gilden_data_musst_saved.containsKey(gilde))gilden_data_musst_saved.put(gilde, new HashMap<GildenType,ArrayList<Stats>>());
		if(!gilden_data_musst_saved.get(gilde).containsKey(typ))gilden_data_musst_saved.get(gilde).put(typ, new ArrayList<Stats>());
		if(!gilden_data_musst_saved.get(gilde).get(typ).contains(s))gilden_data_musst_saved.get(gilde).get(typ).add(s);
	}
	
	public Integer getInt(Stats s,String gilde){
		return getInt(s, gilde,getTyp());
	}
	
	public Integer getInt(Stats s,String gilde,GildenType typ){
		if(!ExistGilde(gilde))return null;
		ExistGildeData(gilde, typ);
		if(gilden_data.containsKey(gilde)&&gilden_data.get(gilde).containsKey(typ)&&gilden_data.get(gilde).get(typ).containsKey(s)){
			return (int)gilden_data.get(gilde).get(typ).get(s);
		}
		int i = -1;
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM `list_gilden_"+typ.getKürzel()+"_data` WHERE gilde= '"+gilde.toLowerCase()+"'");
			while(rs.next()){
				i=rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
		}
		
		if(!gilden_data.containsKey(gilde))gilden_data.put(gilde, new HashMap<GildenType,HashMap<Stats,Object>>());
		if(!gilden_data.get(gilde).containsKey(typ))gilden_data.get(gilde).put(typ, new HashMap<Stats,Object>());
		gilden_data.get(gilde).get(typ).put(s, i);
		return i;
	}
	
	public Integer getRank(String gilde,GildenType typ,Stats s){
		if(!ExistGilde(gilde))return null;
		ExistGildeData(gilde, typ);
		
		boolean done = false;
		int n = -1;
		
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gilde`,`typ` FROM `list_gilden_"+typ.getKürzel()+"_data` WHERE ORDER BY `"+s.getTYP()+"` DESC;");

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
	
	public void setString(String gilde,String i, Stats s){
		setString(gilde, getTyp(), i, s);
	}
	
	public void setString(String gilde,GildenType typ,String i,Stats s){
		if(!ExistGilde(gilde))return ;
		ExistGildeData(gilde, typ);
		if(gilden_data.get(gilde).get(typ).containsKey(s))gilden_data.get(gilde).get(typ).remove(s);
		gilden_data.get(gilde).get(typ).put(s, i);
		if(!gilden_data_musst_saved.containsKey(gilde))gilden_data_musst_saved.put(gilde, new HashMap<GildenType,ArrayList<Stats>>());
		if(!gilden_data_musst_saved.get(gilde).containsKey(typ))gilden_data_musst_saved.get(gilde).put(typ, new ArrayList<Stats>());
		if(!gilden_data_musst_saved.get(gilde).get(typ).contains(s))gilden_data_musst_saved.get(gilde).get(typ).add(s);
	}
	
	public UUID getOwner(String gilde){
		if(!ExistGilde(gilde))return null;
		ExistGildeData(gilde, typ);

		UUID i = null;
		try{
			ResultSet rs = mysql.Query("SELECT owner_uuid FROM `list_gilden_"+typ.getKürzel()+"` WHERE gilde= '"+gilde.toLowerCase()+"'");
			while(rs.next()){
				i=UUID.fromString(rs.getString(1));
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
		}
		return i;
	}
	
	public String getString(Stats s,String gilde){
		return getString(s, gilde,getTyp());
	}
	
	public String getString(Stats s,String gilde,GildenType typ){
		if(!ExistGilde(gilde))return null;
		ExistGildeData(gilde, typ);
		if(gilden_data.containsKey(gilde)&&gilden_data.get(gilde).containsKey(typ)&&gilden_data.get(gilde).get(typ).containsKey(s)){
			return (String)gilden_data.get(gilde).get(typ).get(s);
		}
		String i = "";
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM list_gilden_"+typ.getKürzel()+"_data WHERE gilde= '"+gilde.toLowerCase()+"'");
			while(rs.next()){
				i=rs.getString(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
		}
		
		if(!gilden_data.containsKey(gilde))gilden_data.put(gilde, new HashMap<GildenType,HashMap<Stats,Object>>());
		if(!gilden_data.get(gilde).containsKey(typ))gilden_data.get(gilde).put(typ, new HashMap<Stats,Object>());
		gilden_data.get(gilde).get(typ).put(s, i);
		return i;
	}
	
	public void createGildenEintrag(String gilde,String gildentag,int member,UUID founder){
		String t = "INSERT INTO list_gilden_"+typ.getKürzel()+" (gilde,gildentag,member,founder_uuid,owner_uuid) VALUES ('"+gilde.toLowerCase()+"','"+gildentag+"','"+member+"','"+founder+"','"+founder+"');";
		mysql.Update(t);
	}
	
	public void createDataEintrag(String gilde,GildenType typ){
		Stats[] stats = typ.getStats();
		String tt = "gilde,";
		String ti = "'"+gilde.toLowerCase()+"',";
		for(Stats s : stats){
			tt=tt+s.getTYP()+",";
			ti=ti+"'0',";
		}
		String t = "INSERT INTO list_gilden_"+typ.getKürzel()+"_data ("+tt.substring(0, tt.length()-1)+") VALUES ("+ti.subSequence(0, ti.length()-1)+");";
		mysql.Update(t);
	}
	
	public void CreateTable(){
			String tt = "gilde varchar(30),";
			for(Stats s : getTyp().getStats()){
				tt=tt+s.getCREATE()+",";
			}
			mysql.Update("CREATE TABLE IF NOT EXISTS list_gilden_"+typ.getKürzel()+"_data("+tt.substring(0, tt.length()-1)+")");
	}
	
}
