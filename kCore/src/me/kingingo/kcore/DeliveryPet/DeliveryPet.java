package me.kingingo.kcore.DeliveryPet;

import java.sql.ResultSet;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Enum.ServerType;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.DeliveryInventoryPage;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEnt;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilList;
import me.kingingo.kcore.Util.UtilNumber;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import ru.tehkode.libs.net.gravitydevelopment.updater.Updater.UpdateResult;

import com.avaje.ebeaninternal.server.deploy.generatedproperty.UpdateTimestampFactory;

public class DeliveryPet extends kListener{

	@Getter
	private StatsManager statsManager;
	@Getter
	private PermissionManager permissionManager;
	@Getter
	private InventoryBase base;
	@Getter
	private Entity entity;
	private HashMap<String,DeliveryObject> objects;
	private HashMap<Player,DeliveryInventoryPage> players;
	private HashMap<Player,HashMap<String,Long>> players_obj;
	private ServerType serverType;
	
	public DeliveryPet(DeliveryObject[] objects,String name,EntityType type,Location location,ServerType serverType,Hologram hm, StatsManager statsManager,PermissionManager permissionManager) {
		super(statsManager.getMysql().getInstance(), "DeliveryPet");
		statsManager.getMysql().Update("CREATE TABLE IF NOT EXISTS delivery_"+serverType.name()+"(player varchar(30),uuid varchar(30), obj varchar(30), time varchar(100))");
		
		this.entity=location.getWorld().spawnEntity(location, type);
		this.entity.setCustomName("");
		this.entity.setCustomNameVisible(true);
		hm.setName(entity, name);
		UtilEnt.setNoAI(entity, true);
		
		this.serverType=serverType;
		this.permissionManager=permissionManager;
		this.statsManager=statsManager;
		this.objects=new HashMap<>();
		for(DeliveryObject obj : objects)this.objects.put(obj.displayname, obj);
		this.players_obj=new HashMap<>();
		this.players=new HashMap<>();
		this.base=new InventoryBase(statsManager.getMysql().getInstance(), "Delivery");
	}
	
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()==UpdateType.MIN_64){
			UtilList.CleanList(players,base);
			UtilList.CleanList(players_obj);
		}
		
		if(ev.getType()==UpdateType.SEC){
			for(Player player : players.keySet()){
				if(player.isOnline()&&!players.get(player).getViewers().isEmpty()){
					for(String obj : players_obj.get(player).keySet()){
						players.get(player).getButton(objects.get(obj).slot).setDescription(descriptionUSED(player, obj));
						players.get(player).getButton(objects.get(obj).slot).refreshItemStack();
					}
				}
			}
		}
	}
	
	public void deliverlyUSE(Player player,String name){
		if(objects.get(name).displayname.equalsIgnoreCase(name)){
			statsManager.getMysql().Update("UPDATE delivery_"+serverType.name()+" SET time='"+(System.currentTimeMillis()+objects.get(name).time)+"' WHERE uuid='"+UtilPlayer.getRealUUID(player)+"' AND name='"+name+"'");
			players_obj.get(player).put(name, System.currentTimeMillis()+objects.get(name).time);
			players.get(player).getButton(objects.get(name).slot).setDescription(descriptionUSED(player,name));
			players.get(player).getButton(objects.get(name).slot).refreshItemStack();
			if(players_obj.containsKey(player))players_obj.put(player, new HashMap<String,Long>());
			objects.get(name).click.onClick(player, ActionType.R, objects.get(name));
		}
	}
	
	public String[] descriptionUSED(Player player,String name){
		return new String[]{"Du kannst das Item in "+UtilTime.formatMili(players_obj.get(player).get(name)-System.currentTimeMillis())+" benutzten"};
	}
	
	@EventHandler
	public void Open(PlayerInteractAtEntityEvent ev){
		if(ev.getRightClicked().getEntityId() == entity.getEntityId()){
			if(!players.containsKey(ev.getPlayer())){
				players.put(ev.getPlayer(), new DeliveryInventoryPage(InventorySize._45.getSize(), ev.getPlayer().getName()+" "+"Delivery",this));
				base.addPage(players.get(ev.getPlayer()));
				if(!players_obj.containsKey(ev.getPlayer()))players_obj.put(ev.getPlayer(), new HashMap<String,Long>());
				
				try
			    {
			      ResultSet rs = statsManager.getMysql().Query("SELECT obj,time FROM delivery_"+serverType.name()+" WHERE uuid='"+UtilPlayer.getRealUUID(ev.getPlayer())+"';");

			      while (rs.next()) {
			    	  if(UtilNumber.toLong(rs.getString(2))<System.currentTimeMillis()){
			    		  players_obj.get(ev.getPlayer()).put(rs.getString(1), System.currentTimeMillis()+objects.get(rs.getString(1)).time);
			  			  players.get(ev.getPlayer()).addButton(objects.get(rs.getString(1)).slot, new ButtonBase(objects.get(rs.getString(1)).click, objects.get(rs.getString(1)).material, objects.get(rs.getString(1)).displayname, descriptionUSED(ev.getPlayer(),rs.getString(1))));
			    	  }else{
			    		  players.get(ev.getPlayer()).addButton(objects.get(rs.getString(1)).slot, new ButtonBase(objects.get(rs.getString(1)).click, objects.get(rs.getString(1)).material, objects.get(rs.getString(1)).displayname, objects.get(rs.getString(1)).description));
			    	  }
			      }
			      rs.close();
			    } catch (Exception err) {
			    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,statsManager.getMysql()));
			    }
				
				if(players.get(ev.getPlayer()).getButtons().isEmpty()){
					for(DeliveryObject obj : objects.values()){
						statsManager.getMysql().Update("INSERT INTO delivery_"+serverType.name()+" (player,uuid,obj,time) VALUES ('"+ev.getPlayer().getName()+"','"+UtilPlayer.getRealUUID(ev.getPlayer())+"','"+obj.displayname+"','"+(System.currentTimeMillis()+obj.time)+"');");
						players.get(ev.getPlayer()).addButton(obj.slot, new ButtonBase(obj.click,obj.material, obj.displayname, obj.description));
					}
				}
			}
		}
	}	
}