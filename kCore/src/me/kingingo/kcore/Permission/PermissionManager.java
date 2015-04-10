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
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.permissions.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionManager {
	@Getter
	private HashMap<UUID,PermissionAttachment> plist = new HashMap<>();
	

	@Getter
	private HashMap<UUID,ArrayList<String>> load = new HashMap<>();
	@Getter
	private ArrayList<UUID> load_now = new ArrayList<>();
	
	@Getter
	private HashMap<UUID,String> pgroup = new HashMap<>();
	@Getter
	private HashMap<String,Group> groups = new HashMap<>();
	@Getter
	private MySQL mysql;
	@Getter
	@Setter
	private boolean AllowTab=true;
	@Getter
	private JavaPlugin instance;
	private PacketManager packetManager;
	@Getter
	private GroupTyp typ;
	@Getter
	private PermissionListener listener;
	
	public PermissionManager(JavaPlugin instance,GroupTyp typ,PacketManager packetManager,MySQL mysql){
		this.mysql=mysql;
		this.instance=instance;
		this.packetManager=packetManager;
		this.typ=typ;
		UtilServer.essPermissionHandler();
		UtilTime.setTimeManager(this);
		this.listener=new PermissionListener(this);
	}
	
	public String getPrefix(Player p){
		if(pgroup.containsKey(UtilPlayer.getRealUUID(p)))return groups.get(pgroup.get(UtilPlayer.getRealUUID(p))).getPrefix();
		return C.cGray;
	}
	
	public List<kPermission> getPermissionList(Player p){
		
		ArrayList<kPermission> list = new ArrayList<>();
		if(plist.containsKey(UtilPlayer.getRealUUID(p))){
			for(String perm : plist.get(UtilPlayer.getRealUUID(p)).getPermissions().keySet()){
				list.add(kPermission.valueOf(perm));
			}
		}
		return list;
	}
	
	public boolean hasGroupPermission(Player p,kPermission perm){
		if(getPgroup().containsKey(UtilPlayer.getRealUUID(p))){
			if(getGroups().containsKey(getPgroup().get(UtilPlayer.getRealUUID(p)))){
				return getGroups().get(getPgroup().get(UtilPlayer.getRealUUID(p))).getPerms().contains(perm.getPermissionToString().toLowerCase());
			}
		}
		return false;
	}
	
	public boolean hasPermission(Player p,kPermission perm){
		return (p.hasPermission(perm.getPermissionToString()) || p.hasPermission(kPermission.ALL_PERMISSION.getPermissionToString()));
	}
	
	public void setGroup(UUID uuid,String group){
		setGroup(uuid, group, getTyp());
	}

	public void setGroup(Player p, String group){
		setGroup(UtilPlayer.getRealUUID(p), group, getTyp());
	}
	
	public void setGroup(UUID uuid, String group,GroupTyp typ){
		if(!getGroup(uuid).equalsIgnoreCase("default")){
			mysql.Update("UPDATE game_perm SET pgroup='" + group+ "' WHERE uuid='" + uuid + "' AND permission='none' AND prefix='none' AND (grouptyp='"+typ.name()+"' OR grouptyp='"+GroupTyp.ALL.name()+"')");
		}else{
			mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,grouptyp,uuid) values ('none','none','"+group+"','"+typ.getName()+"','"+uuid+"');");
		}
		load.remove(uuid);
		plist.remove(uuid);
		pgroup.remove(uuid);
		if(UtilPlayer.isOnline(uuid))loadPermission(uuid);
		packetManager.SendPacket("BG", new PERMISSION_USER_RELOAD(uuid));
	}
	
	public void addPermission(UUID uuid, kPermission perm,GroupTyp typ){
		addPermission(uuid, perm.getPermissionToString(), typ);
	}
	
	public void addPermission(UUID uuid, kPermission perm){
		addPermission(uuid, perm.getPermissionToString(),getTyp());
	}
	
	public void addPermission(Player p,kPermission perm){
		addPermission(p, perm.getPermissionToString());
	}
	
	public void addPermission(Player p, String perm){
		if(!plist.containsKey(UtilPlayer.getRealUUID(p)))plist.put(UtilPlayer.getRealUUID(p), p.addAttachment(getInstance()));
		plist.get(UtilPlayer.getRealUUID(p)).setPermission(perm, true);
		addPermission(UtilPlayer.getRealUUID(p), perm, getTyp());
	}
	
	public void addPermission(UUID uuid, String perm,GroupTyp typ){
		mysql.Update("INSERT INTO game_perm (prefix,permission,pgroup,grouptyp,uuid) values ('none','"+perm+"','none','"+typ.name()+"','"+uuid+"');");
	}
	
	public boolean setTabList(Player p){
		return setTabList(p, false);
	}
	
	public boolean setTabList(Player p,boolean invisble) {
		if(!isAllowTab())return false;
		String name = p.getName();
		if(p.isCustomNameVisible()){
			name=p.getCustomName();
		}
		if(pgroup.containsKey(UtilPlayer.getRealUUID(p))&&!invisble){
			String t = groups.get(pgroup.get(UtilPlayer.getRealUUID(p))).getPrefix();
			
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
		return getGroup(UtilPlayer.getRealUUID(p),getTyp());
	}
	
	public String getGroup(UUID uuid){
		return getGroup(uuid, getTyp());
	}
	
	public String getGroup(UUID uuid,GroupTyp typ){
		if(pgroup.containsKey(uuid)){
			return pgroup.get(uuid);
		}
		
		String g ="default";
		try {
			ResultSet rs = mysql.Query("SELECT pgroup FROM game_perm WHERE prefix='none' AND permission='none' AND pgroup!='none' AND uuid='"+uuid+"' AND (grouptyp='"+typ.name()+"' OR grouptyp='"+GroupTyp.ALL.name()+"')");

			while (rs.next()) {
				g=rs.getString(1).toLowerCase();
			}

			rs.close();
		} catch (Exception err) {
		  Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,mysql));
		}
		return g;
	}
	
	public void loadPermission(UUID uuid){
		String g=getGroup(uuid);
		ResultSet rs;
		System.err.println("UUID: "+uuid+" group:"+g);
		if(!groups.containsKey(g)){
				groups.put(g, new Group(g,typ));
				try
			    {
			      rs = mysql.Query("SELECT permission FROM game_perm WHERE permission!='none' AND pgroup='"+g+"' AND prefix='none' AND uuid='none' AND (grouptyp='"+getTyp().getName()+"' OR grouptyp='"+GroupTyp.ALL.name()+"')");
			      while (rs.next()){
			    	  if(rs.getString(1).contains("epicpvp.perm.group.")){
			    		  transfareGroupPermissionToGroup(g,rs.getString(1).substring("epicpvp.perm.group.".length(), rs.getString(1).length()).split(":")[0],GroupTyp.get(rs.getString(1).substring("epicpvp.perm.group.".length(), rs.getString(1).length()).split(":")[1]));
			    	  }else{
				    	  groups.get(g).add(rs.getString(1));
			    	  }
			      }
			      rs.close();
			    }
			    catch (SQLException e)
			    {
			      Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,mysql));
			    }
				
				try {
					rs = mysql.Query("SELECT prefix FROM game_perm WHERE prefix!='none' AND pgroup='"+ g.toLowerCase() + "' AND permission='none' AND uuid='none' AND (grouptyp='"+getTyp().getName()+"' OR grouptyp='"+GroupTyp.ALL.name()+"')");

					while (rs.next()) {
						groups.get(g).setPrefix(rs.getString(1));
					}

					rs.close();
				} catch (Exception err) {
					Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,mysql));
				}
		}
		if(!pgroup.containsKey(uuid))pgroup.put(uuid, g);
		
		try
	    {
	      rs = mysql.Query("SELECT permission FROM game_perm WHERE permission!='none' AND uuid='"+uuid+"' AND prefix='none' AND pgroup='none' AND (grouptyp='"+getTyp().getName()+"' OR grouptyp='"+GroupTyp.ALL.name()+"')");
	      while (rs.next()){
	    	  if(rs.getString(1).contains("epicpvp.perm.group.")){
	    		  transfareGroupPermissionToUser(uuid,rs.getString(1).substring("epicpvp.perm.group.".length(), rs.getString(1).length()).split(":")[0],GroupTyp.get(rs.getString(1).substring("epicpvp.perm.group.".length(), rs.getString(1).length()).split(":")[1]));
	    	  }else{
		    	  if(!load.containsKey(uuid))load.put(uuid, new ArrayList<String>());
		    	  load.get(uuid).add(rs.getString(1));
	    	  }
	      }
	      rs.close();
	    }
	    catch (SQLException e)
	    {
	      Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,mysql));
	    }
		
		getLoad_now().add(uuid);
	}
	
	public void transfareGroupPermissionToUser(UUID uuid,String group,GroupTyp typ){
		try
	    {
		ResultSet rs = mysql.Query("SELECT permission FROM game_perm WHERE permission!='none' AND pgroup='"+group+"' AND prefix='none' AND uuid='none' AND grouptyp='"+typ.name()+"'");
	      while (rs.next()){
	    	  if(!rs.getString(1).contains("epicpvp.perm.group.")){
		    	  if(!load.containsKey(uuid))load.put(uuid, new ArrayList<String>());
		    	  load.get(uuid).add(rs.getString(1));
	    	  }else{
	    		  transfareGroupPermissionToUser(uuid,rs.getString(1).substring("epicpvp.perm.group.".length(), rs.getString(1).length()).split(":")[0],GroupTyp.get(rs.getString(1).substring("epicpvp.perm.group.".length(), rs.getString(1).length()).split(":")[1]));
	    	  }
	      }
	      rs.close();
	    }
	    catch (SQLException e)
	    {
	      Bukkit.getServer().getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,mysql));
	    }
	}
	
	public void transfareGroupPermissionToGroup(String g,String group,GroupTyp typ){
		try
	    {
		ResultSet rs = mysql.Query("SELECT permission FROM game_perm WHERE permission!='none' AND pgroup='"+group+"' AND prefix='none' AND uuid='none' AND grouptyp='"+typ.name()+"'");
	      while (rs.next()){
	    	  if(!rs.getString(1).contains("epicpvp.perm.group.")){
	 		     groups.get(g).add(rs.getString(1));
	    	  }else{
	    		  transfareGroupPermissionToGroup(g,rs.getString(1).substring("epicpvp.perm.group.".length(), rs.getString(1).length()).split(":")[0],GroupTyp.get(rs.getString(1).substring("epicpvp.perm.group.".length(), rs.getString(1).length()).split(":")[1]));
	    	  }
	      }
	      rs.close();
	    }
	    catch (SQLException e)
	    {
	      Bukkit.getServer().getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,mysql));
	    }
	}
	
	public void loadGroup(String g){
		if(!groups.containsKey(g)){
				groups.put(g, new Group(g,typ));
				try
			    {
				ResultSet rs = mysql.Query("SELECT permission FROM game_perm WHERE permission!='none' AND pgroup='"+g+"' AND prefix='none' AND uuid='none' AND (grouptyp='"+getTyp().getName()+"' OR grouptyp='"+GroupTyp.ALL.name()+"')");
			      while (rs.next()){
			    	  if(rs.getString(1).contains("epicpvp.perm.group.")){
			    		  transfareGroupPermissionToGroup(g,rs.getString(1).substring("epicpvp.perm.group.".length(), rs.getString(1).length()).split(":")[0],GroupTyp.get(rs.getString(1).substring("epicpvp.perm.group.".length(), rs.getString(1).length()).split(":")[1]));
			    	  }else{
				    	  groups.get(g).add(rs.getString(1));
			    	  }
			      }
			      rs.close();
			    }
			    catch (SQLException e)
			    {
			      Bukkit.getServer().getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,e,mysql));
			    }
				
				try {
					ResultSet rs = mysql.Query("SELECT prefix FROM game_perm WHERE prefix!='none' AND pgroup='"+ g.toLowerCase() + "' AND permission='none' AND uuid='none' AND (grouptyp='"+getTyp().getName()+"' OR grouptyp='"+GroupTyp.ALL.name()+"')");

					while (rs.next()) {
						groups.get(g).setPrefix(rs.getString(1));
					}

					rs.close();
				} catch (Exception err) {
					Bukkit.getServer().getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,mysql));
				}
		}
	}
	
	public void removePermission(UUID uuid, kPermission perm){
		removePermission(uuid, perm.getPermissionToString());
	}
	
	public void removePermission(Player p, kPermission perm){
		if(plist.containsKey(UtilPlayer.getRealUUID(p))){
			plist.get(UtilPlayer.getRealUUID(p)).unsetPermission(perm.getPermissionToString());
		}
		removePermission(UtilPlayer.getRealUUID(p), perm.getPermissionToString());
	}

	public void removePermission(UUID uuid, String perm){
		removePermission(uuid, perm, getTyp());
	}
	
	public void removePermission(UUID uuid, String perm,GroupTyp typ){
		 mysql.Update("DELETE FROM game_perm WHERE uuid='" + uuid + "' AND prefix='none' AND permission='"+perm+"' AND (grouptyp='"+typ.name()+"' OR grouptyp='"+GroupTyp.ALL.name()+"')");
	}
	
}
