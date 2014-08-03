package me.kingingo.kcore.Permission;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.ScoreboardManager.PlayerScoreboard;
import me.kingingo.kcore.Util.C;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionManager {
	private HashMap<Player,List<Permission>> list = new HashMap<>();
	private HashMap<Player,String> prefix = new HashMap<>();
	private MySQL mysql;
	@Getter
	private JavaPlugin instance;
	
	public PermissionManager(JavaPlugin instance,MySQL mysql){
		this.mysql=mysql;
		this.instance=instance;
		Bukkit.getPluginManager().registerEvents(new PermissionListener(this), getInstance());
	}
	
	public String getPrefix(Player p){
		if(prefix.containsKey(p))return prefix.get(p);
		return C.cGray;
	}
	
	public List<Permission> getPermissionList(Player p){
		if(list.containsKey(p))return list.get(p);
		return null;
	}
	
	public boolean hasPermission(Player p,Permission perm){
		if(list.containsKey(p)&&(list.get(p).contains(perm)||list.get(p).contains(Permission.ALL_PERMISSION)))return true;
		return false;
	}
	
	public void setPrefix(Player p,String prefix){
		this.prefix.put(p,prefix);
	}
	
	public void setGroup(String p, String group){
		if(getGroup(p)!=null){
			mysql.Update("UPDATE game_perm SET pgroup='" + group+ "' WHERE user='" + p.toLowerCase() + "'");
		}else{
			mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','none','"+group+"','"+p.toLowerCase()+"');");
		}
	}
	
	public void setGroup(Player p, String group){
		if(getGroup(p.getName())!=null){
			mysql.Update("UPDATE game_perm SET pgroup='" + group+ "' WHERE user='" + p.getName().toLowerCase() + "'");
		}else{

			mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','none','"+group+"','"+p.getName().toLowerCase()+"');");
		}
	}
	
	
	public void addPermission(String p, String perm){
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','"+perm+"','none','"+p.toLowerCase()+"');");
	}
	
	public void addPermission(Player p, String perm){
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','"+perm+"','none','"+p.getName().toLowerCase()+"');");
	}
	
	public void addPermission(String p, Permission perm){
		list.get(p).add(perm);
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','"+perm.getPermissionToString()+"','none','"+p.toLowerCase()+"');");
	}
	
	public void addPermission(Player p, Permission perm){
		if(!list.containsKey(p))list.put(p, new ArrayList<Permission>());
		list.get(p).add(perm);
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','"+perm.getPermissionToString()+"','none','"+p.getName().toLowerCase()+"');");
	}
	
	public void setTabList(Player p){	
		if(prefix.containsKey(p)){
			String t = prefix.get(p);
			int i = t.indexOf("§");
			t=""+t.toCharArray()[i]+t.toCharArray()[i+1];
			if(p.getName().length()>13){
				try{
					p.setPlayerListName(t+p.getName().subSequence(0, 13));
				}catch(IllegalArgumentException e){
					p.setPlayerListName(t+p.getName().subSequence(0, 12));
				}
			}else{
					p.setPlayerListName(t+p.getName());
			}
		}else{
			if(p.getName().length()>13){
				try{
					p.setPlayerListName("§7"+p.getName().subSequence(0, 13));
				}catch(IllegalArgumentException e){
					//((CraftServer)Bukkit.getServer()).getHandle().players.remove("§7"+p.getName().subSequence(0, 13));
					p.setPlayerListName("§7"+p.getName().subSequence(0, 12));
				}
			}else{
				p.setPlayerListName("§7"+p.getName());
			}
		}
	}
	
	public String getGroup(Player p){
		String g =null;
		try {
			ResultSet rs = mysql.Query("SELECT pgroup FROM game_perm WHERE user='"+ p.getName().toLowerCase() + "' AND prefix='none' AND permission='none'");

			while (rs.next()) {
				g=rs.getString(1);
			}

			rs.close();
		} catch (Exception err) {
		  Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
		}
		return g;
	}
	
	public String getGroup(String p){
		String g =null;
		try {
			ResultSet rs = mysql.Query("SELECT pgroup FROM game_perm WHERE user='"+ p.toLowerCase() + "' AND prefix='none' AND permission='none'");

			while (rs.next()) {
				g=rs.getString(1);
			}

			rs.close();
		} catch (Exception err) {
		  Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
		}
		return g;
	}
	
	public void loadPermission(Player p){
		String g="default";
		Permission permission;
		List<Permission> perm = new ArrayList<>();
		ResultSet rs;
		
		try {
			rs = mysql.Query("SELECT pgroup FROM game_perm WHERE user='"+ p.getName().toLowerCase() + "' AND prefix='none' AND permission='none'");

			while (rs.next()) {
				g = rs.getString(1);
			}

			rs.close();
		} catch (Exception err) {
		  Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
		}
		
		try
	    {
	      
	      rs = mysql.Query("SELECT permission FROM game_perm WHERE user='"+p.getName()+"' AND prefix='none' AND pgroup='none'");
	      while (rs.next()){
	    	  permission=Permission.isPerm(rs.getString(1));
	    	  if(permission==Permission.NONE)continue;
	    	  perm.add(permission);  
	      }
	      rs.close();
	    }
	    catch (SQLException e)
	    {
	      Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e));
	    }
		
		try
	    {
	      
	      rs = mysql.Query("SELECT permission FROM game_perm WHERE pgroup='"+g+"' AND prefix='none'");
	      while (rs.next()){
	    	  permission=Permission.isPerm(rs.getString(1));
	    	  if(permission==Permission.NONE)continue;
	    	  perm.add(permission);  
	      }
	      rs.close();
	    }
	    catch (SQLException e)
	    {
	      Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e));
	    }
		
		try {
			rs = mysql.Query("SELECT prefix FROM game_perm WHERE pgroup='"+ g.toLowerCase() + "' AND permission='none' AND user='none'");

			while (rs.next()) {
				setPrefix(p,rs.getString(1));
			}

			rs.close();
		} catch (Exception err) {
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
		}
		
		if(!perm.isEmpty())list.put(p, perm);
	}
	
	public void loadPermissions(Player p, List<Permission> permission){
		if(p instanceof Player && !permission.isEmpty())list.put(p, permission);
	}
	
	public void removePermission(String p, Permission perm){
		 mysql.Update("DELETE FROM game_perm WHERE user='" + p.toLowerCase() + "' AND permission='"+perm.getPermissionToString()+"'");
	}
	
	public void removePermission(Player p, Permission perm){
		if(list.containsKey(p)){
			if(list.get(p).contains(perm)){
				list.get(p).remove(perm);
			}
		}
		 mysql.Update("DELETE FROM game_perm WHERE user='" + p.getName().toLowerCase() + "' AND permission='"+perm.getPermissionToString()+"'");
	}
	
	public void removePermission(String p, String perm){
		 mysql.Update("DELETE FROM game_perm WHERE user='" + p.toLowerCase() + "' AND permission='"+perm+"'");
	}
	
	public void removePermission(Player p, String perm){
		 mysql.Update("DELETE FROM game_perm WHERE user='" + p.getName().toLowerCase() + "' AND permission='"+perm+"'");
	}
	
	public void removePermissions(Player p){
		if(list.containsKey(p))list.remove(p);
	}
	
}
