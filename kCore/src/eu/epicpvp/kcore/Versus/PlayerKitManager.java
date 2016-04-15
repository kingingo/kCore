package eu.epicpvp.kcore.Versus;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

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
	private HashMap<UUID,PlayerKit> kits;
	@Getter
	private GameType type;
	@Getter
	@Setter
	private boolean async=false;
	
	public PlayerKitManager(MySQL mysql,GameType type){
		this.mysql=mysql;
		this.type=type;
		this.kits=new HashMap<>();
		
		this.mysql.createTable("users_"+type.getKuerzel()+"_kits", "player varchar(30),uuid varchar(100),id int,content varchar(1000),armor_content varchar(1000)");
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
	
	public void addKit(UUID uuid,PlayerInventory inv,int id){
		addKit("none", uuid, inv, id);
	}
	
	public void addKit(String playerName,UUID uuid,PlayerInventory inv,int id){
		inv.setContents(check(inv.getContents()));
		inv.setArmorContents(check(inv.getArmorContents()));
		delKit(uuid,id);
		
		if(kits.containsKey(uuid)){
			kits.get(uuid).content=inv.getContents();
			kits.get(uuid).armor_content=inv.getArmorContents();
		}
		
		if(isAsync()){
			getMysql().asyncInsert("users_"+type.getKuerzel()+"_kits", "player,uuid,id,content,armor_content", "'"+playerName+"','"+uuid+"','"+id+"','"+UtilInv.itemStackArrayToBase64(inv.getContents())+"','"+UtilInv.itemStackArrayToBase64(inv.getArmorContents())+"'");
		}else{	
			getMysql().Insert("users_"+type.getKuerzel()+"_kits", "player,uuid,id,content,armor_content", "'"+playerName+"','"+uuid+"','"+id+"','"+UtilInv.itemStackArrayToBase64(inv.getContents())+"','"+UtilInv.itemStackArrayToBase64(inv.getArmorContents())+"'");
		}
	}
	
	public void updateKit(UUID uuid,int id,PlayerInventory inv){
		inv.setContents(check(inv.getContents()));
		inv.setArmorContents(check(inv.getArmorContents()));
		if(kits.containsKey(uuid)){
			kits.get(uuid).content=inv.getContents();
			kits.get(uuid).armor_content=inv.getArmorContents();
		}
		
		if(isAsync()){
			getMysql().asyncUpdate("users_"+type.getKuerzel()+"_kits", "content='"+UtilInv.itemStackArrayToBase64(inv.getContents())+"', armor_content='"+UtilInv.itemStackArrayToBase64(inv.getArmorContents())+"'", "UUID='"+uuid+"' AND id='"+id+"'");
		}else{	
			getMysql().Update("users_"+type.getKuerzel()+"_kits", "content='"+UtilInv.itemStackArrayToBase64(inv.getContents())+"', armor_content='"+UtilInv.itemStackArrayToBase64(inv.getArmorContents())+"'", "UUID='"+uuid+"' AND id='"+id+"'");
		}
	}
	
	public void delKit(UUID uuid,int id){
		if(kits.containsKey(uuid)){
			kits.get(uuid).id=0;
			kits.get(uuid).content=null;
			kits.get(uuid).armor_content=null;
			kits.remove(uuid);
		}
		
		if(isAsync()){
			getMysql().asyncDelete("users_"+type.getKuerzel()+"_kits", "UUID='"+uuid+"' AND id='"+id+"'");
		}else{	
			getMysql().Delete("users_"+type.getKuerzel()+"_kits", "UUID='"+uuid+"' AND id='"+id+"'");
		}
	}
	
	public void loadAsyncKit(UUID uuid,int id){
		loadAsyncKit(uuid, id, null);
	}
	
	public void loadAsyncKit(UUID uuid,int id,Callback<PlayerKit> callback){
		if(!kits.containsKey(uuid)){
			mysql.asyncQuery("users_"+type.getKuerzel()+"_kits", "`content`,`armor_content`", "UUID='"+uuid+"' AND id='"+id+"'", new Callback<ResultSet>() {
				
				@Override
				public void call(ResultSet value) {
					if(value instanceof ResultSet){
						
						try {
							ResultSet rs = (ResultSet) value;
							
							kits.put(uuid, new PlayerKit());
							kits.get(uuid).id=id;
							kits.get(uuid).content=UtilInv.itemStackArrayFromBase64(rs.getString(1));
							kits.get(uuid).armor_content=UtilInv.itemStackArrayFromBase64(rs.getString(2));
								 
							if(callback!=null)callback.call(kits.get(uuid));
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
	
	public PlayerKit getKit(UUID uuid,int id){
		if(kits.containsKey(uuid)){
			if(kits.get(uuid).id==id)return kits.get(uuid);
			kits.get(uuid).id=0;
			kits.get(uuid).content=null;
			kits.get(uuid).armor_content=null;
			kits.remove(uuid);
		}
		
		try
	    {
		  ResultSet rs = mysql.Query("users_"+type.getKuerzel()+"_kits", "`content`,`armor_content`", "UUID='"+uuid+"' AND id='"+id+"'");

	      while (rs.next()) {
	    	  kits.put(uuid, new PlayerKit());
	    	  kits.get(uuid).id=id;
	    	  kits.get(uuid).content=UtilInv.itemStackArrayFromBase64(rs.getString(1));
	    	  kits.get(uuid).armor_content=UtilInv.itemStackArrayFromBase64(rs.getString(2));
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