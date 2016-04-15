package eu.epicpvp.kcore.Versus;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import dev.wolveringer.client.Callback;
import dev.wolveringer.dataserver.gamestats.GameType;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.MySQL.MySQLErr;
import eu.epicpvp.kcore.MySQL.Events.MySQLErrorEvent;
import eu.epicpvp.kcore.Util.UtilInv;
import lombok.Getter;
import lombok.Setter;

public class PlayerKitManager{

	@Getter
	private MySQL mysql;
	@Getter
	private HashMap<Integer,PlayerKit> kits;
	@Getter
	private GameType type;
	@Getter
	@Setter
	private boolean async=false;
	
	public PlayerKitManager(MySQL mysql,GameType type){
		this.mysql=mysql;
		this.type=type;
		this.kits=new HashMap<>();
		
		this.mysql.createTable("users_"+type.getShortName()+"_kits", "playerId int,id int,content text,armor_content text");
	}
	
	public ItemStack[] check(ItemStack[] c){
		for(int i = 0; i<c.length;i++){
			if(c[i]!=null){
				if(c[i].getTypeId()==373&&UtilInv.GetData(c[i])==0){
					c[i]=null;
					continue;
				}else if(c[i].getTypeId()==137){
					c[i]=null;
					continue;
				}else if(c[i].getType()==Material.BARRIER){
					c[i]=null;
					continue;
				}else if(c[i].getType()==Material.BEDROCK){
					c[i]=null;
					continue;
				}else if(c[i].getType()==Material.OBSIDIAN){
					c[i]=null;
					continue;
				}
			}
		}
		
		return c;
	}
	
	public void addKit(int playerId,PlayerInventory inv,int id){
		inv.setContents(check(inv.getContents()));
		inv.setArmorContents(check(inv.getArmorContents()));
		delKit(playerId,id);
		
		if(kits.containsKey(playerId)){
			kits.get(playerId).content=inv.getContents();
			kits.get(playerId).armor_content=inv.getArmorContents();
		}
		
		if(isAsync()){
			getMysql().asyncInsert("users_"+type.getShortName()+"_kits", "playerId,id,content,armor_content", "'"+playerId+"','"+id+"','"+UtilInv.itemStackArrayToBase64(inv.getContents())+"','"+UtilInv.itemStackArrayToBase64(inv.getArmorContents())+"'");
		}else{	
			getMysql().Insert("users_"+type.getShortName()+"_kits", "playerId,id,content,armor_content", "'"+playerId+"','"+id+"','"+UtilInv.itemStackArrayToBase64(inv.getContents())+"','"+UtilInv.itemStackArrayToBase64(inv.getArmorContents())+"'");
		}
	}
	
	public void updateKit(int playerId,int id,PlayerInventory inv){
		inv.setContents(check(inv.getContents()));
		inv.setArmorContents(check(inv.getArmorContents()));
		if(kits.containsKey(playerId)){
			kits.get(playerId).content=inv.getContents();
			kits.get(playerId).armor_content=inv.getArmorContents();
		}
		
		if(isAsync()){
			getMysql().asyncUpdate("users_"+type.getShortName()+"_kits", "content='"+UtilInv.itemStackArrayToBase64(inv.getContents())+"', armor_content='"+UtilInv.itemStackArrayToBase64(inv.getArmorContents())+"'", "playerId='"+playerId+"' AND id='"+id+"'");
		}else{	
			getMysql().Update("users_"+type.getShortName()+"_kits", "content='"+UtilInv.itemStackArrayToBase64(inv.getContents())+"', armor_content='"+UtilInv.itemStackArrayToBase64(inv.getArmorContents())+"'", "playerId='"+playerId+"' AND id='"+id+"'");
		}
	}
	
	public void delKit(int playerId,int id){
		if(kits.containsKey(playerId)){
			kits.get(playerId).id=0;
			kits.get(playerId).content=null;
			kits.get(playerId).armor_content=null;
			kits.remove(playerId);
		}
		
		if(isAsync()){
			getMysql().asyncDelete("users_"+type.getShortName()+"_kits", "playerId='"+playerId+"' AND id='"+id+"'");
		}else{	
			getMysql().Delete("users_"+type.getShortName()+"_kits", "playerId='"+playerId+"' AND id='"+id+"'");
		}
	}
	
	public void loadAsyncKit(int playerId,int id){
		loadAsyncKit(playerId, id, null);
	}
	
	public void loadAsyncKit(int playerId,int id,Callback<PlayerKit> callback){
		if(!kits.containsKey(playerId)){
			mysql.asyncQuery("users_"+type.getShortName()+"_kits", "`content`,`armor_content`", "playerId='"+playerId+"' AND id='"+id+"'", new Callback<ResultSet>() {
				
				@Override
				public void call(ResultSet value) {
					if(value instanceof ResultSet){
						
						try {
							ResultSet rs = (ResultSet) value;
							
							kits.put(playerId, new PlayerKit());
							kits.get(playerId).id=id;
							kits.get(playerId).content=UtilInv.itemStackArrayFromBase64(rs.getString(1));
							kits.get(playerId).armor_content=UtilInv.itemStackArrayFromBase64(rs.getString(2));
								 
							if(callback!=null)callback.call(kits.get(playerId));
						} catch (SQLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}
	
	public PlayerKit getKit(int playerId,int id){
		if(kits.containsKey(playerId)){
			if(kits.get(playerId).id==id)return kits.get(playerId);
			kits.get(playerId).id=0;
			kits.get(playerId).content=null;
			kits.get(playerId).armor_content=null;
			kits.remove(playerId);
		}
		
		try
	    {
		  ResultSet rs = mysql.Query("users_"+type.getShortName()+"_kits", "`content`,`armor_content`", "playerId='"+playerId+"' AND id='"+id+"'");

	      while (rs.next()) {
	    	  kits.put(playerId, new PlayerKit());
	    	  kits.get(playerId).id=id;
	    	  kits.get(playerId).content=UtilInv.itemStackArrayFromBase64(rs.getString(1));
	    	  kits.get(playerId).armor_content=UtilInv.itemStackArrayFromBase64(rs.getString(2));
	      }

	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		
		if(kits.containsKey(playerId)){
			return kits.get(playerId);
		}else{
			return null;
		}
	}
	
}
