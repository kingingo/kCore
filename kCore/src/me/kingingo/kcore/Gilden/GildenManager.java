package me.kingingo.kcore.Gilden;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.PlayerStats.Stats;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GildenManager implements Listener {

	@Getter
	private JavaPlugin instance;
	@Getter
	private HashMap<String,String> gilden_player = new HashMap<>();
	@Getter
	private HashMap<String,String> gilden_tag = new HashMap<>();
	@Getter
	private HashMap<String,HashMap<GildenTyp,HashMap<Stats,Object>>> gilden_data = new HashMap<>();
	@Getter
	private HashMap<String,HashMap<GildenTyp,ArrayList<Stats>>> gilden_data_musst_saved = new HashMap<>();
	@Getter
	private HashMap<String,String> gilden_owner = new HashMap<>();
	@Getter
	private HashMap<Player,String> gilden_einladung = new HashMap<>();
	@Getter
	private MySQL mysql;
	@Getter
	private GildenTyp typ;
	@Getter
	private HashMap<Player,Long> teleport = new HashMap<>();
	@Getter
	private HashMap<Player,Location> teleport_loc = new HashMap<>();
	
	public GildenManager(JavaPlugin instance,MySQL mysql,GildenTyp typ){
		this.instance=instance;
		this.typ=typ;
		this.mysql=mysql;
		mysql.Update("CREATE TABLE IF NOT EXISTS list_gilden(gilde varchar(30),gildentag varchar(30),member int,founder varchar(30),owner varchar(30))");
		mysql.Update("CREATE TABLE IF NOT EXISTS list_gilden_user(player varchar(30),UUID varchar(100),gilde varchar(30))");
		CreateTable();
		Bukkit.getPluginManager().registerEvents(this, getInstance());
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void DamageTeam(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player && ev.getDamager() instanceof Player){
			Player attack = (Player)ev.getDamager();
			Player defend = (Player)ev.getEntity();
			if(isPlayerInGilde(attack.getName())&&isPlayerInGilde(defend.getName())){
				if(getPlayerGilde(attack.getName()).equalsIgnoreCase(getPlayerGilde(defend.getName()))){
					ev.setCancelled(true);
				}
			}
		}
	}
	
	ArrayList<Player> TP = new ArrayList<>();
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FAST)return;
		if(teleport.isEmpty())return;
		for(Player p : teleport.keySet()){
			if(teleport.get(p) <= System.currentTimeMillis()){
				TP.add(p);
				if(teleport_loc.get(p).getX()==p.getLocation().getX()&&teleport_loc.get(p).getY()==p.getLocation().getY()&&teleport_loc.get(p).getZ()==p.getLocation().getZ()){
					TeleportToHome(p);
				}else{
					p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_TELEPORT_CANCELLED.getText());
				}
			}
		}
		for(Player p : TP){
			teleport.remove(p);
			teleport_loc.remove(p);
		}
	}
	
	public void TeleportToHome(Player p){
		if(!isPlayerInGilde(p.getName())){
			p.sendMessage(Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_IS_NOT_IN_GILDE.getText());
			return;
		}
		String g = getPlayerGilde(p.getName());
		String w = getString(Stats.WORLD, g, getTyp());
		int x = getInt(Stats.LOC_X, g, typ);
		int y = getInt(Stats.LOC_Y, g, typ);
		int z = getInt(Stats.LOC_Z, g, typ);
		Location loc = new Location(Bukkit.getWorld(w),x,y,z);
		p.teleport(loc);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerChat(AsyncPlayerChatEvent ev) {
		Player p = ev.getPlayer();
		if(isPlayerInGilde(p.getName())){
			String g = getPlayerGilde(p.getName());
			String tag = getTag(g);
			p.setDisplayName(p.getDisplayName().replace(p.getName(), tag) + ChatColor.RESET + p.getName());
		}
	}
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent ev){
		if(!isPlayerInGilde(ev.getPlayer().getName()))return;
		sendGildenChat(getPlayerGilde(ev.getPlayer().getName()), Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_LEAVE.getText(ev.getPlayer().getName()));
	}
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent ev){
		if(!isPlayerInGilde(ev.getPlayer().getName()))return;
		if(gilden_player.containsKey(ev.getPlayer().getName()))gilden_player.remove(ev.getPlayer().getName());
		getPlayerGilde(ev.getPlayer().getName());
		sendGildenChat(getPlayerGilde(ev.getPlayer().getName()), Text.GILDE_PREFIX.getText()+Text.GILDE_PLAYER_JOIN.getText(ev.getPlayer().getName()));
	}
	
	public void sendGildenChat(String gilde,String msg){
		Player p;
		for(String n : gilden_player.keySet()){
			if(gilden_player.get(n).equalsIgnoreCase(gilde)){
				if(UtilPlayer.isOnline(n)){
					p = Bukkit.getPlayer(n);
					p.sendMessage(msg);
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
		ArrayList<String> l = new ArrayList<>();
		getMember(name);
		for(String n : getGilden_player().keySet()){
			if( getGilden_player().get(n).equalsIgnoreCase(name)){
				l.add(n);
			}
		}
		for(String n : l){
			removePlayerEintrag(n);
		}
		mysql.Update("DELETE FROM list_gilden WHERE gilde='" + name.toLowerCase() + "'");
		mysql.Update("DELETE FROM list_gilden_data WHERE gilde='" + name.toLowerCase() + "'");
		mysql.Update("DELETE FROM list_gilden_user WHERE gilde='" + name.toLowerCase() + "'");
	}
	
	public void removePlayerEintrag(String name){
		mysql.Update("DELETE FROM list_gilden_user WHERE player='" + name.toLowerCase() + "'");
	}
	
	public void createPlayerEintrag(String name,String UUID,String gilde){
		gilden_player.put(name, gilde);
		mysql.Update("INSERT INTO list_gilden_user (player,uuid,gilde) VALUES ('"+name.toLowerCase()+"','"+UUID+"','"+gilde.toLowerCase()+"');");
	}
	
	public String getPlayerGilde(String name){
		if(gilden_player.containsKey(name))return gilden_player.get(name);
		String g  = "";
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gilde` FROM `list_gilden_user` WHERE player='"+name.toLowerCase()+"'");

	      while (rs.next()) {
	    		g=rs.getString(1);
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
	    }
		gilden_player.put(name,g);
		
		return g;
	}
	
	public boolean isPlayerInGilde(String name){
		if(gilden_player.containsKey(name))return true;
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gilde` FROM `list_gilden_user` WHERE player='"+name.toLowerCase()+"'");

	      while (rs.next()) {
	    		return true;
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
	    }
		return false;
	}
	
	public boolean ExistGildeData(String gilde,GildenTyp typ){
		boolean done = false;
		if(gilden_data.containsKey(gilde))return true;
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `typ` FROM `list_gilden_data` WHERE gilde='"+gilde.toLowerCase()+"' AND typ='"+typ.getKürzel()+"'");

	      while (rs.next()) {
	    		  done=true;
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
	    }
		
		if(!done)createDataEintrag(gilde, typ);
		
		gilden_data.put(gilde,new HashMap<GildenTyp,HashMap<Stats,Object>>());
		gilden_data.get(gilde).put(typ, new HashMap<Stats,Object>());
		return done;
	}
	
	public void UpdateGilde(String gilde,GildenTyp typ){
		if(!gilden_data_musst_saved.containsKey(gilde))return;
		if(!gilden_data_musst_saved.get(gilde).containsKey(typ))return;
		for(Stats s : gilden_data.get(gilde).get(typ).keySet()){
			if(!gilden_data_musst_saved.get(gilde).get(typ).contains(s))continue;;
			if(!s.isMysql())continue;
			Object o = gilden_data.get(gilde).get(typ).get(s);
			if(o instanceof Integer){
				mysql.Update("UPDATE list_gilden_data SET "+s.getTYP()+"='"+((Integer)o)+"' WHERE gilde='" + gilde.toLowerCase() + "' AND typ='"+typ.getKürzel()+"'");
			}else if(o instanceof String){
				mysql.Update("UPDATE list_gilden_data SET "+s.getTYP()+"='"+((String)o)+"' WHERE gilde='" + gilde.toLowerCase() + "' AND typ='"+typ.getKürzel()+"'");
			}
		}
	}
	
	public boolean ExistGilde(String gilde){
		boolean done = false;
		if(gilden_tag.containsKey(gilde))return true;
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gilde` FROM `list_gilden` WHERE gilde='"+gilde.toLowerCase()+"'");

	      while (rs.next()) {
	    		  done=true;
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
	    }
		
		if(done)gilden_tag.put(gilde, getTag(gilde));
		return done;
	}
	
	public String getTag(String gilde){
		String tag = "";
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gildentag` FROM `list_gilden` WHERE gilde='"+gilde.toLowerCase()+"'");

	      while (rs.next()) {
	    		  tag=rs.getString(1);
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
	    }
		return tag;
	}
	
	public void getMember(String gilde){
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `player` FROM `list_gilden_user` WHERE gilde='"+gilde.toLowerCase()+"'");

	      while (rs.next()) {
	    	  if(gilden_player.containsKey(rs.getString(1)))continue;
	    	 gilden_player.put(rs.getString(1), gilde);
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
	    }
	}
	
	public void setInt(String gilde,GildenTyp typ,int i,Stats s){
		if(ExistGilde(gilde))return ;
		ExistGildeData(gilde, typ);
		gilden_data.get(gilde).get(typ).put(s, i);
		if(!gilden_data_musst_saved.containsKey(gilde))gilden_data_musst_saved.put(gilde, new HashMap<GildenTyp,ArrayList<Stats>>());
		if(!gilden_data_musst_saved.get(gilde).containsKey(typ))gilden_data_musst_saved.get(gilde).put(typ, new ArrayList<Stats>());
		if(!gilden_data_musst_saved.get(gilde).get(typ).contains(s))gilden_data_musst_saved.get(gilde).get(typ).add(s);
	}
	
	public Integer getRank(String gilde,GildenTyp typ,Stats s){
		if(ExistGilde(gilde))return null;
		ExistGildeData(gilde, typ);
		
		boolean done = false;
		int n = -1;
		
		try
	    {
	      ResultSet rs = mysql.Query("SELECT `gilde`,`typ` FROM `list_gilden_data` WHERE typ='"+typ.getKürzel()+"' ORDER BY `"+s.getTYP()+"` DESC;");

	      while ((rs.next()) && (!done)) {
	    	 n++;
	        if (rs.getString(1).equalsIgnoreCase(gilde)) {
	          done = true;
	        }
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
	    }
		
	    return n;
	}
	
	public void setString(String gilde,GildenTyp typ,String i,Stats s){
		if(ExistGilde(gilde))return ;
		ExistGildeData(gilde, typ);
		gilden_data.get(gilde).get(typ).put(s, i);
		if(!gilden_data_musst_saved.containsKey(gilde))gilden_data_musst_saved.put(gilde, new HashMap<GildenTyp,ArrayList<Stats>>());
		if(!gilden_data_musst_saved.get(gilde).containsKey(typ))gilden_data_musst_saved.get(gilde).put(typ, new ArrayList<Stats>());
		if(!gilden_data_musst_saved.get(gilde).get(typ).contains(s))gilden_data_musst_saved.get(gilde).get(typ).add(s);
	}
	
	public String getOwner(String gilde){
		if(ExistGilde(gilde))return null;
		ExistGildeData(gilde, typ);

		String i = "";
		try{
			ResultSet rs = mysql.Query("SELECT owner FROM list_gilden WHERE gilde= '"+gilde.toLowerCase()+"'");
			while(rs.next()){
				i=rs.getString(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
		}
		return i;
	}
	
	public String getString(Stats s,String gilde,GildenTyp typ){
		if(ExistGilde(gilde))return null;
		ExistGildeData(gilde, typ);
		if(gilden_data.containsKey(gilde)&&gilden_data.get(gilde).containsKey(s)){
			return (String)gilden_data.get(gilde).get(typ).get(s);
		}
		String i = "";
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM list_gilden_data WHERE gilde= '"+gilde.toLowerCase()+"' AND typ='"+typ.getKürzel()+"'");
			while(rs.next()){
				i=rs.getString(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
		}
		
		if(!gilden_data.containsKey(gilde))gilden_data.put(gilde, new HashMap<GildenTyp,HashMap<Stats,Object>>());
		if(!gilden_data.get(gilde).containsKey(typ))gilden_data.get(gilde).put(typ, new HashMap<Stats,Object>());
		gilden_data.get(gilde).get(typ).put(s, i);
		return i;
	}
	
	public Integer getInt(Stats s,String gilde,GildenTyp typ){
		if(ExistGilde(gilde))return null;
		ExistGildeData(gilde, typ);
		if(gilden_data.containsKey(gilde)&&gilden_data.get(gilde).containsKey(s)){
			return (int)gilden_data.get(gilde).get(typ).get(s);
		}
		int i = -1;
		try{
			ResultSet rs = mysql.Query("SELECT "+s.getTYP()+" FROM list_gilden_data WHERE gilde= '"+gilde.toLowerCase()+"' AND typ='"+typ.getKürzel()+"'");
			while(rs.next()){
				i=rs.getInt(1);
			}
			rs.close();
		}catch (Exception err){
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
		}
		
		if(!gilden_data.containsKey(gilde))gilden_data.put(gilde, new HashMap<GildenTyp,HashMap<Stats,Object>>());
		if(!gilden_data.get(gilde).containsKey(typ))gilden_data.get(gilde).put(typ, new HashMap<Stats,Object>());
		gilden_data.get(gilde).get(typ).put(s, i);
		return i;
	}
	
	public void createGildenEintrag(String gilde,String gildentag,int member,String founder){
		String t = "INSERT INTO list_gilden (gilde,gildentag,member,founder,owner) VALUES ('"+gilde.toLowerCase()+"','"+gildentag+"','"+member+"','"+founder+"','"+founder+"');";
		mysql.Update(t);
		createDataEintrag(gilde, getTyp());
	}
	
	public void createDataEintrag(String gilde,GildenTyp typ){
		Stats[] stats = typ.getStats();
		String tt = "gilde,typ,";
		String ti = "'"+gilde.toLowerCase()+"','"+typ.getKürzel()+"',";
		for(Stats s : stats){
			tt=tt+s.getTYP()+",";
			ti=ti+"'0',";
		}
		String t = "INSERT INTO list_gilden_data ("+tt.substring(0, tt.length()-1)+") VALUES ("+ti.subSequence(0, ti.length()-1)+");";
		mysql.Update(t);
	}
	
	public void CreateTable(){
		String tt = "gilde varchar(30),typ varchar(30),";
		for(Stats s : getTyp().getStats()){
			tt=tt+s.getCREATE()+",";
		}
		String t = "CREATE TABLE IF NOT EXISTS list_gilden_data("+tt.substring(0, tt.length()-1)+")";
		mysql.Update(t);
	}
	
}
