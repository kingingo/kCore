package me.kingingo.kcore.Permission;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

public class PermissionManager_old1 {
	@Getter
	private HashMap<UUID,List<Permission>> plist = new HashMap<>();
	@Getter
	private HashMap<UUID,String> pgroup = new HashMap<>();
	@Getter
	private HashMap<String,List<Permission>> groups = new HashMap<>();
	@Getter
	private HashMap<String,String> gprefix = new HashMap<>();
	@Getter
	private MySQL mysql;
	@Getter
	@Setter
	private boolean AllowTab=true;
	@Getter
	private JavaPlugin instance;
	private PacketManager packetManager;
	
	public PermissionManager_old1(JavaPlugin instance,PacketManager packetManager,MySQL mysql){
		this.mysql=mysql;
		this.instance=instance;
		this.packetManager=packetManager;
		//Bukkit.getPluginManager().registerEvents(new PermissionListener(this), getInstance());
	}
	
	public String getPrefix(Player p){
		if(pgroup.containsKey(UtilPlayer.getRealUUID(p))&&gprefix.containsKey(pgroup.get(UtilPlayer.getRealUUID(p))))return gprefix.get(pgroup.get(UtilPlayer.getRealUUID(p)));
		return C.cGray;
	}
	
	public List<Permission> getPermissionList(Player p){
		ArrayList<Permission> list = new ArrayList<>();
		if(pgroup.containsKey(UtilPlayer.getRealUUID(p))){
			for(Permission perm : groups.get(p)){
				list.add(perm);
			}
		}
		if(plist.containsKey(UtilPlayer.getRealUUID(p))){
			for(Permission perm : plist.get(UtilPlayer.getRealUUID(p))){
				list.add(perm);
			}
		}
		return list;
	}
	
	public boolean hasPermission(Player p,Permission perm){
		if(pgroup.containsKey(UtilPlayer.getRealUUID(p))&& (groups.get(pgroup.get(UtilPlayer.getRealUUID(p))).contains(perm)||groups.get(pgroup.get(UtilPlayer.getRealUUID(p))).contains(Permission.ALL_PERMISSION)) )return true;
		if(plist.containsKey(UtilPlayer.getRealUUID(p))&& (plist.get(UtilPlayer.getRealUUID(p)).contains(perm)||plist.get(UtilPlayer.getRealUUID(p)).contains(Permission.ALL_PERMISSION)) )return true;
		return false;
	}
	
	public void setGroup(UUID uuid, String group){
		if(getGroup(uuid)!=null){
			mysql.Update("UPDATE game_perm SET pgroup='" + group+ "' WHERE uuid='" + uuid + "' AND permission='none'");
		}else{
			mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,uuid) values ('none','none','"+group+"','"+uuid+"');");
		}
		packetManager.SendPacket("BG", new PERMISSION_USER_RELOAD(uuid));
	}
	
	public void setGroup(Player p, String group){
		if(getGroup(UtilPlayer.getRealUUID(p))!=null){
			mysql.Update("UPDATE game_perm SET pgroup='" + group+ "' WHERE uuid='" + UtilPlayer.getRealUUID(p) + "' AND permission='none'");
		}else{
			mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,uuid) values ('none','none','"+group+"','"+UtilPlayer.getRealUUID(p)+"');");
		}
		pgroup.remove(UtilPlayer.getRealUUID(p));
		pgroup.put(UtilPlayer.getRealUUID(p), group);
		packetManager.SendPacket("BG", new PERMISSION_USER_RELOAD(UtilPlayer.getRealUUID(p)));
	}
	
	
	public void addPermission(UUID uuid, String perm){
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,uuid) values ('none','"+perm+"','none','"+uuid+"');");
	}
	
	public void addPermission(Player p, String perm){
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,uuid) values ('none','"+perm+"','none','"+UtilPlayer.getRealUUID(p)+"');");
	}
	
	public void addPermission(UUID uuid, Permission perm){
		if(!plist.containsKey(uuid))plist.put(uuid, new ArrayList<Permission>());
		plist.get(uuid).add(perm);
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,uuid) values ('none','"+perm.getPermissionToString()+"','none','"+uuid+"');");
	}
	
	public void addPermission(Player p, Permission perm){
		if(!plist.containsKey(UtilPlayer.getRealUUID(p)))plist.put(UtilPlayer.getRealUUID(p), new ArrayList<Permission>());
		plist.get(UtilPlayer.getRealUUID(p)).add(perm);
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,uuid) values ('none','"+perm.getPermissionToString()+"','none','"+UtilPlayer.getRealUUID(p)+"');");
	}
	
	public boolean setTabList(Player p){
		return setTabList(p, false);
	}
	
	public boolean setTabList(Player p,boolean invisble){
		if(!isAllowTab())return false;
		String name = p.getName();
		if(p.isCustomNameVisible()){
			name=p.getCustomName();
		}
		if(pgroup.containsKey(UtilPlayer.getRealUUID(p))&&gprefix.containsKey(pgroup.get(UtilPlayer.getRealUUID(p)))&&!invisble){
			String t = gprefix.get(pgroup.get(UtilPlayer.getRealUUID(p)));
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
			ResultSet rs = mysql.Query("SELECT pgroup FROM game_perm WHERE uuid='"+ UtilPlayer.getRealUUID(p) + "' AND prefix='none' AND permission='none'");

			while (rs.next()) {
				g=rs.getString(1);
			}

			rs.close();
		} catch (Exception err) {
		  Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,mysql));
		}
		return g;
	}
	
	public String getGroup(UUID uuid){
		String g =null;
		try {
			ResultSet rs = mysql.Query("SELECT pgroup FROM game_perm WHERE uuid='"+ uuid + "' AND prefix='none' AND permission='none'");

			while (rs.next()) {
				g=rs.getString(1);
			}

			rs.close();
		} catch (Exception err) {
		  Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,mysql));
		}
		return g;
	}
	
	public void loadPermission(UUID uuid){
		String g=getGroup(uuid);
		Permission permission;
		ResultSet rs;
		if(g==null)g="default";
		System.err.println("UUID: "+uuid+" group:"+g);
		if(!groups.containsKey(g)){
			groups.put(g, new ArrayList<Permission>());
			try
		    {
		      rs = mysql.Query("SELECT permission FROM game_perm WHERE pgroup='"+g+"' AND prefix='none' AND uuid='none'");
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
				rs = mysql.Query("SELECT prefix FROM game_perm WHERE pgroup='"+ g.toLowerCase() + "' AND permission='none' AND uuid='none'");

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
	      rs = mysql.Query("SELECT permission FROM game_perm WHERE uuid='"+uuid+"' AND prefix='none' AND pgroup='none'");
	      while (rs.next()){
	    	  permission=Permission.isPerm(rs.getString(1));
	    	  if(permission==Permission.NONE)continue;
	    	  if(!plist.containsKey(uuid))plist.put(uuid, new ArrayList<Permission>());
	    	  plist.get(uuid).add(permission);
	      }
	      rs.close();
	    }
	    catch (SQLException e)
	    {
	      Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,mysql));
	    }
		
		pgroup.put(uuid, g);
	}
	
	public void loadGroup(String g){
		if(!groups.containsKey(g)){
			Permission permission;
			groups.put(g, new ArrayList<Permission>());
			try
		    {
			ResultSet rs = mysql.Query("SELECT permission FROM game_perm WHERE pgroup='"+g+"' AND prefix='none' AND uuid='none'");
		      while (rs.next()){
		    	  permission=Permission.isPerm(rs.getString(1));
		    	  if(permission==Permission.NONE)continue;
		    	  groups.get(g).add(permission);
		      }
		      rs.close();
		    }
		    catch (SQLException e)
		    {
		      Bukkit.getServer().getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,mysql));
		    }
		}
		if(!gprefix.containsKey(g)){
			try {
				ResultSet rs = mysql.Query("SELECT prefix FROM game_perm WHERE pgroup='"+ g.toLowerCase() + "' AND permission='none' AND uuid='none'");

				while (rs.next()) {
					gprefix.put(g, rs.getString(1));
				}

				rs.close();
			} catch (Exception err) {
				Bukkit.getServer().getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,mysql));
			}
		}
	}
	
	public void removePermission(UUID uuid, Permission perm){
		 mysql.Update("DELETE FROM game_perm WHERE uuid='" + uuid + "' AND permission='"+perm.getPermissionToString()+"'");
	}
	
	public void removePermission(Player p, Permission perm){
		if(plist.containsKey(UtilPlayer.getRealUUID(p))){
			if(plist.get(UtilPlayer.getRealUUID(p)).contains(perm)){
				plist.get(UtilPlayer.getRealUUID(p)).remove(perm);
			}
		}
		 mysql.Update("DELETE FROM game_perm WHERE uuid='" + UtilPlayer.getRealUUID(p) + "' AND permission='"+perm.getPermissionToString()+"'");
	}
	
	public void removePermission(UUID uuid, String perm){
		 mysql.Update("DELETE FROM game_perm WHERE uuid='" + uuid + "' AND permission='"+perm+"'");
	}
	
	public void removePermission(Player p, String perm){
		 mysql.Update("DELETE FROM game_perm WHERE uuid='" + UtilPlayer.getRealUUID(p) + "' AND permission='"+perm+"'");
	}
	
	public void removePermissions(Player p){
		if(plist.containsKey(UtilPlayer.getRealUUID(p)))plist.remove(UtilPlayer.getRealUUID(p));
	}
	
}
