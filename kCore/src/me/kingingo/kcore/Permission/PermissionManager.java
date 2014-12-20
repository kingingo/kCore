package me.kingingo.kcore.Permission;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Packet.Packets.PERMISSION_USER_RELOAD;
import me.kingingo.kcore.Util.C;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionManager {
	@Getter
	private HashMap<String,List<Permission>> plist = new HashMap<>();
	@Getter
	private HashMap<String,String> pgroup = new HashMap<>();
	@Getter
	private HashMap<String,List<Permission>> groups = new HashMap<>();
	@Getter
	private HashMap<String,String> gprefix = new HashMap<>();
	@Getter
	private MySQL mysql;
	@Getter
	@Setter
	private boolean SetAllowTab=true;
	@Getter
	private JavaPlugin instance;
	private PacketManager packetManager;
	
	public PermissionManager(JavaPlugin instance,PacketManager packetManager,MySQL mysql){
		this.mysql=mysql;
		this.instance=instance;
		this.packetManager=packetManager;
		Bukkit.getPluginManager().registerEvents(new PermissionListener(this), getInstance());
	}
	
	public String getPrefix(Player p){
		if(pgroup.containsKey(p.getName().toLowerCase())&&gprefix.containsKey(pgroup.get(p.getName().toLowerCase())))return gprefix.get(pgroup.get(p.getName().toLowerCase()));
		return C.cGray;
	}
	
	public List<Permission> getPermissionList(Player p){
		ArrayList<Permission> list = new ArrayList<>();
		if(pgroup.containsKey(p.getName().toLowerCase())){
			for(Permission perm : groups.get(p)){
				list.add(perm);
			}
		}
		if(plist.containsKey(p.getName().toLowerCase())){
			for(Permission perm : plist.get(p.getName().toLowerCase())){
				list.add(perm);
			}
		}
		return list;
	}
	
	public boolean hasPermission(Player p,Permission perm){
		if(pgroup.containsKey(p.getName().toLowerCase())&& (groups.get(pgroup.get(p.getName().toLowerCase())).contains(perm)||groups.get(pgroup.get(p.getName().toLowerCase())).contains(Permission.ALL_PERMISSION)) )return true;
		if(plist.containsKey(p.getName().toLowerCase())&& (plist.get(p.getName().toLowerCase()).contains(perm)||plist.get(p.getName().toLowerCase()).contains(Permission.ALL_PERMISSION)) )return true;
		return false;
	}
	
	public void setGroup(String p, String group){
		if(getGroup(p)!=null){
			mysql.Update("UPDATE game_perm SET pgroup='" + group+ "' WHERE user='" + p.toLowerCase() + "'");
		}else{
			mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','none','"+group+"','"+p.toLowerCase()+"');");
		}
		packetManager.SendPacket("BG", new PERMISSION_USER_RELOAD(p.toLowerCase()));
	}
	
	public void setGroup(Player p, String group){
		if(getGroup(p.getName())!=null){
			mysql.Update("UPDATE game_perm SET pgroup='" + group+ "' WHERE user='" + p.getName().toLowerCase() + "'");
		}else{
			mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','none','"+group+"','"+p.getName().toLowerCase()+"');");
		}
		pgroup.remove(p.getName().toLowerCase());
		pgroup.put(p.getName().toLowerCase(), group);
		packetManager.SendPacket("BG", new PERMISSION_USER_RELOAD(p.getName().toLowerCase()));
	}
	
	
	public void addPermission(String p, String perm){
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','"+perm+"','none','"+p.toLowerCase()+"');");
	}
	
	public void addPermission(Player p, String perm){
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','"+perm+"','none','"+p.getName().toLowerCase()+"');");
	}
	
	public void addPermission(String p, Permission perm){
		if(!plist.containsKey(p.toLowerCase()))plist.put(p.toLowerCase(), new ArrayList<Permission>());
		plist.get(p.toLowerCase()).add(perm);
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','"+perm.getPermissionToString()+"','none','"+p.toLowerCase()+"');");
	}
	
	public void addPermission(Player p, Permission perm){
		if(!plist.containsKey(p.getName().toLowerCase()))plist.put(p.getName().toLowerCase(), new ArrayList<Permission>());
		plist.get(p.getName().toLowerCase()).add(perm);
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,user) values ('none','"+perm.getPermissionToString()+"','none','"+p.getName().toLowerCase()+"');");
	}
	
	public boolean setTabList(Player p){
		return setTabList(p, false);
	}
	
	public boolean setTabList(Player p,boolean invisble){
		if(!isSetAllowTab())return false;
		String name = p.getName();
		if(p.isCustomNameVisible()){
			name=p.getCustomName();
		}
		if(pgroup.containsKey(p.getName().toLowerCase())&&gprefix.containsKey(pgroup.get(p.getName().toLowerCase()))&&!invisble){
			String t = gprefix.get(pgroup.get(p.getName().toLowerCase()));
			int i = t.indexOf("§");
			t=""+t.toCharArray()[i]+t.toCharArray()[i+1];
			
			if(name.length()>13){
				try{
					UtilPlayer.setPlayerListName(p,t+name.subSequence(0, 13));
				}catch(IllegalArgumentException e){
					UtilPlayer.setPlayerListName(p,t+name.subSequence(0, 12));
				}
			}else{
				UtilPlayer.setPlayerListName(p,t+name);
			}
		}else{
			if(name.length()>13){
				try{
					UtilPlayer.setPlayerListName(p,"§7"+name.subSequence(0, 13));
				}catch(IllegalArgumentException e){
					UtilPlayer.setPlayerListName(p,"§7"+name.subSequence(0, 12));
				}
			}else{
				UtilPlayer.setPlayerListName(p,"§7"+name);
			}
		}
		return true;
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
		  Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,mysql));
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
		  Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,mysql));
		}
		return g;
	}
	
	public void loadPermission(String p){
		String g=getGroup(p);
		Permission permission;
		ResultSet rs;
		if(g==null)g="default";
		
		if(!groups.containsKey(g)){
			groups.put(g, new ArrayList<Permission>());
			try
		    {
		      rs = mysql.Query("SELECT permission FROM game_perm WHERE pgroup='"+g+"' AND prefix='none' AND user='none'");
		      while (rs.next()){
		    	  permission=Permission.isPerm(rs.getString(1));
		    	  if(permission==Permission.NONE)continue;
		    	  groups.get(g).add(permission);
		      }
		      rs.close();
		    }
		    catch (SQLException e)
		    {
		      Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,mysql));
		    }
		}
		
		if(!gprefix.containsKey(g)){
			try {
				rs = mysql.Query("SELECT prefix FROM game_perm WHERE pgroup='"+ g.toLowerCase() + "' AND permission='none' AND user='none'");

				while (rs.next()) {
					gprefix.put(g, rs.getString(1));
				}

				rs.close();
			} catch (Exception err) {
				Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,mysql));
			}
		}
		
		try
	    {
	      rs = mysql.Query("SELECT permission FROM game_perm WHERE user='"+p.toLowerCase()+"' AND prefix='none' AND pgroup='none'");
	      while (rs.next()){
	    	  permission=Permission.isPerm(rs.getString(1));
	    	  if(permission==Permission.NONE)continue;
	    	  if(!plist.containsKey(p.toLowerCase()))plist.put(p.toLowerCase(), new ArrayList<Permission>());
	    	  plist.get(p.toLowerCase()).add(permission);
	      }
	      rs.close();
	    }
	    catch (SQLException e)
	    {
	      Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,mysql));
	    }
		
		pgroup.put(p.toLowerCase(), g);
	}
	
	public void removePermission(String p, Permission perm){
		 mysql.Update("DELETE FROM game_perm WHERE user='" + p.toLowerCase() + "' AND permission='"+perm.getPermissionToString()+"'");
	}
	
	public void removePermission(Player p, Permission perm){
		if(plist.containsKey(p.getName().toLowerCase())){
			if(plist.get(p.getName().toLowerCase()).contains(perm)){
				plist.get(p.getName().toLowerCase()).remove(perm);
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
		if(plist.containsKey(p.getName().toLowerCase()))plist.remove(p.getName().toLowerCase());
	}
	
}
