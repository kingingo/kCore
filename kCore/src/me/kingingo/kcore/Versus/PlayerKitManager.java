package me.kingingo.kcore.Versus;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Enum.GameType;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.Util.UtilInv;

import org.bukkit.Bukkit;
import org.bukkit.inventory.PlayerInventory;

public class PlayerKitManager{

	@Getter
	private MySQL mysql;
	@Getter
	private HashMap<UUID,PlayerKit> kits;
	@Getter
	private GameType type;
	
	public PlayerKitManager(MySQL mysql,GameType type){
		this.mysql=mysql;
		this.type=type;
		this.kits=new HashMap<>();
		
		this.mysql.createTable("users_"+type.getKürzel()+"_kits", "player varchar(30),uuid varchar(30),id int,kit varchar(300),kitname varchar(30)");
	}
	
	public void addKit(UUID uuid,PlayerInventory inv,String name,int id){
		addKit("none", uuid, inv, name, id);
	}
	
	public void addKit(String playerName,UUID uuid,PlayerInventory inv,String name,int id){
		delKit(uuid,id);
		getMysql().Insert("users_"+type.getKürzel()+"_kits", "player,uuid,id,kit,kitname", playerName+","+uuid+","+id+","+UtilInv.PlayerInventorytoBase64(inv)+","+name);
	}
	
	public void updateKit(UUID uuid,int id,PlayerInventory inv){
		getMysql().Update("users_"+type.getKürzel()+"_kits", "kit='"+UtilInv.PlayerInventorytoBase64(inv)+"'", "UUID='"+uuid+"' AND id='"+id+"'");
	}
	
	public void delKit(UUID uuid,int id){
		getMysql().Delete("users_"+type.getKürzel()+"_kits", "UUID="+uuid+" AND id='"+id+"'");
	}
	
	public PlayerKit getKit(UUID uuid,int id){
		if(kits.containsKey(uuid)){
			if(kits.get(uuid).id==id)return kits.get(uuid);
			kits.get(uuid).id=0;
			kits.get(uuid).inventory=null;
			kits.get(uuid).name=null;
			kits.remove(uuid);
		}
		
		
		try
	    {
		  ResultSet rs = mysql.Query("users_"+type.getKürzel()+"_kits", "`kit`,`kitname`", "UUID='"+uuid+"' AND id='"+id+"'");

	      while (rs.next()) {
	    	  kits.put(uuid, new PlayerKit());
	    	  kits.get(uuid).id=id;
	    	  kits.get(uuid).inventory=UtilInv.PlayerInventoryfromBase64(rs.getString(1));
	    	  kits.get(uuid).name=rs.getString(2);
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		
		if(kits.containsKey(uuid)){
			return kits.get(uuid);
		}else{
			return null;
		}
	}
	
}
