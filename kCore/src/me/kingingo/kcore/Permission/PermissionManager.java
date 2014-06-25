package me.kingingo.kcore.Permission;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.Util.C;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionManager {
	private HashMap<Player,List<Permission>> list = new HashMap<>();
	private HashMap<Player,String> prefix = new HashMap<>();
	private MySQL mysql;
	
	public PermissionManager(JavaPlugin plugin,MySQL mysql){
		this.mysql=mysql;
		Bukkit.getPluginManager().registerEvents(new PermissionListener(this), plugin);
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
	
	public void addPermission(String p, Permission perm){
		list.get(p).add(perm);
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','"+perm.getPermissionToString()+"','none','"+p.toLowerCase()+"');");
	}
	
	public void addPermission(Player p, Permission perm){
		list.get(p).add(perm);
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','"+perm.getPermissionToString()+"','none','"+p.getName().toLowerCase()+"');");
	}
	
	public void loadPermission(Player p){
		String g="default";
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
	    	  perm.add(Permission.isPerm(rs.getString(1)));  
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
	      while (rs.next())
	      {
	    	  perm.add(Permission.isPerm(rs.getString(1)));
	      }
	      rs.close();
	    }
	    catch (SQLException e)
	    {
	      Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e));
	    }
		
		try {
			rs = mysql.Query("SELECT prefix FROM game_perm WHERE pgroup='"+ g.toLowerCase() + "' AND permission='none'");

			while (rs.next()) {
				setPrefix(p,rs.getString(1));
			}

			rs.close();
		} catch (Exception err) {
			Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err));
		}
		
		if(!list.isEmpty())list.put(p, perm);
	}
	
	public void loadPermissions(Player p, List<Permission> permission){
		if(p instanceof Player && !permission.isEmpty())list.put(p, permission);
	}
	
	public void removePermissions(Player p){
		if(list.containsKey(p))list.remove(p);
	}
	
}
