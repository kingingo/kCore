package me.kingingo.kcore.DeliveryPet;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Enum.ServerType;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.DeliveryInventoryPage;
import me.kingingo.kcore.Inventory.Inventory.InventoryLotto2;
import me.kingingo.kcore.Inventory.Inventory.InventoryLotto2.InventoryLotto2Type;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.ButtonOpenInventory;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.LottoPackage;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEnt;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilList;
import me.kingingo.kcore.Util.UtilMath;
import me.kingingo.kcore.Util.UtilNumber;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import ru.tehkode.libs.net.gravitydevelopment.updater.Updater.UpdateResult;

import com.avaje.ebeaninternal.server.deploy.generatedproperty.UpdateTimestampFactory;

public class DeliveryPet extends kListener{

	@Getter
	private InventoryBase base;
	@Getter
	private InventoryLotto2 lotto;
	@Getter
	private Creature entity;
	private HashMap<String,DeliveryObject> objects;
	private HashMap<Player,DeliveryInventoryPage> players;
	private HashMap<UUID,HashMap<String,Long>> players_obj;
	private ServerType serverType;
	@Getter
	private HashMap<InventoryLotto2Type, ArrayList<LottoPackage>> packages;
	@Getter
	private MySQL mysql;
	@Getter
	private Hologram hologramm;
	@Getter
	private Location location;
	@Getter
	private String name;
	@Getter
	private EntityType type;
	
	public DeliveryPet(DeliveryObject[] objects,String name,EntityType type,Location location,ServerType serverType,Hologram hm,MySQL mysql) {
		super(mysql.getInstance(), "DeliveryPet");
		this.mysql=mysql;
		this.type=type;
		this.location=location;
		this.name=name;
		getMysql().Update("CREATE TABLE IF NOT EXISTS delivery_"+serverType.name()+"(player varchar(30),uuid varchar(100), obj varchar(30), time varchar(100))");
		this.serverType=serverType;
		this.hologramm=hm;
		this.objects=new HashMap<>();
		for(DeliveryObject obj : objects)this.objects.put(obj.displayname, obj);
		this.players_obj=new HashMap<>();
		this.players=new HashMap<>();
		this.base=new InventoryBase(getMysql().getInstance(), "Delivery");
		this.lotto=new InventoryLotto2("Play a Round!", getMysql().getInstance());
		this.base.addPage(lotto);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		if(this.entity==null||this.entity.isDead()){
			this.entity=(Creature)location.getWorld().spawnCreature(location, type);
			this.entity.setCustomName("");
			this.entity.setCustomNameVisible(true);
			getHologramm().setName(entity, name);
			UtilEnt.setNoAI(entity, true);
			UtilEnt.ClearGoals(entity);
			
			if(entity.getType()==EntityType.ENDERMAN){
				Enderman e = (Enderman) this.entity;
				e.setCanPickupItems(false);
				e.setRemoveWhenFarAway(false);
			}
		}
	}
	
	@EventHandler
	public void teleport(EntityTeleportEvent ev){
		if(ev.getEntity().getEntityId()==this.entity.getEntityId()){
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev){
		if(ev.getEntity().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
		if(ev.getDamager().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
	}
	
	@EventHandler
	public void Damage(EntityDamageEvent ev){
		if(ev.getEntity().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
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
					for(String obj : players_obj.get(UtilPlayer.getRealUUID(player)).keySet()){
						if(players_obj.get(UtilPlayer.getRealUUID(player)).get(obj)>System.currentTimeMillis()){
							players.get(player).getButton(objects.get(obj).slot).setDescription(objects.get(obj).description);
							players.get(player).getButton(objects.get(obj).slot).setMaterial(objects.get(obj).material );
						}else{
							players.get(player).getButton(objects.get(obj).slot).setDescription(descriptionUSED(player, obj));
						}
						players.get(player).getButton(objects.get(obj).slot).refreshItemStack();
					}
				}
			}
		}
	}
	
	public void deliverlyUSE(Player player,String name){
		if(objects.get(name).displayname.equalsIgnoreCase(name)){
			getMysql().Update("UPDATE delivery_"+serverType.name()+" SET time='"+(System.currentTimeMillis()+objects.get(name).time)+"' WHERE uuid='"+UtilPlayer.getRealUUID(player)+"' AND obj='"+name+"'");
			players_obj.get(UtilPlayer.getRealUUID(player)).remove(name);
			players_obj.get(UtilPlayer.getRealUUID(player)).put(name, System.currentTimeMillis()+objects.get(name).time);
			players.get(player).getButton(objects.get(name).slot).setDescription(descriptionUSED(player,name));
			players.get(player).getButton(objects.get(name).slot).setMaterial(Material.REDSTONE_BLOCK);
			players.get(player).getButton(objects.get(name).slot).refreshItemStack();
			objects.get(name).click.onClick(player, ActionType.R, objects.get(name));
		}
	}
	
	public String[] descriptionUSED(Player player,String name){
		return new String[]{"§7Du kannst das Item in §c"+UtilTime.formatMili(players_obj.get(UtilPlayer.getRealUUID(player)).get(name)-System.currentTimeMillis())+"§7 benutzten"};
	}
	
	public LottoPackage[] randomPackages(Player player){
		LottoPackage[] ps = new LottoPackage[18];
		
		int common=6;
		int uncommon=5;
		int rare=4;
		int legendary=2;
		int divine=0;
		
		if(UtilMath.r( 100000 ) == 5356){
			divine++;
		}else{
			legendary++;
		}
		
		for(int i = 0; i<ps.length; i++){
			if(common != 0){
				ps[i]=packages.get(InventoryLotto2Type.COMMON).get( UtilMath.r(packages.get(InventoryLotto2Type.COMMON).size()) );
				
				if(ps[i].hasPlayer(player)){
					ps[i]=null;
					i--;
				}else{
					common--;
				}
			}else if(uncommon != 0){
				ps[i]=packages.get(InventoryLotto2Type.UNCOMMON).get( UtilMath.r(packages.get(InventoryLotto2Type.UNCOMMON).size()) );

				if(ps[i].hasPlayer(player)){
					ps[i]=null;
					i--;
				}else{
					uncommon--;
				}
			}else if(rare != 0){
				ps[i]=packages.get(InventoryLotto2Type.RARE).get( UtilMath.r(packages.get(InventoryLotto2Type.RARE).size()) );

				if(ps[i].hasPlayer(player)){
					ps[i]=null;
					i--;
				}else{
					rare--;
				}
			}else if(legendary != 0){
				ps[i]=packages.get(InventoryLotto2Type.LEGENDARY).get( UtilMath.r(packages.get(InventoryLotto2Type.LEGENDARY).size()) );
				
				if(ps[i].hasPlayer(player)){
					ps[i]=null;
					i--;
				}else{
					legendary--;
				}
			}else if(divine != 0){
				ps[i]=packages.get(InventoryLotto2Type.DIVINE).get( UtilMath.r(packages.get(InventoryLotto2Type.DIVINE).size()) );
				
				if(ps[i].hasPlayer(player)){
					ps[i]=null;
					i--;
				}else{
					divine--;
				}
			}
		}
		
		LottoPackage[] ps1 =new LottoPackage[18];
		int r;
		for(int i = 0; i<ps.length; i++){
			r=UtilMath.r(ps.length);
			if(ps1[r]!=null){
				i--;
				continue;
			}
			ps1[r]=ps[i];
		}
		
		return ps1;
	}
	
	@EventHandler
	public void Login(AsyncPlayerPreLoginEvent ev){
		if(!players_obj.containsKey(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId())))players_obj.put(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()), new HashMap<String,Long>());
		
		try
	    {
	      ResultSet rs = getMysql().Query("SELECT obj,time FROM delivery_"+serverType.name()+" WHERE uuid='"+UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId())+"';");

	      while (rs.next()) {
	    	  players_obj.get(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId())).put(rs.getString(1), System.currentTimeMillis()+objects.get(rs.getString(1)).time);
	      }
	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		
		if(players_obj.get(UtilPlayer.getRealUUID(ev.getName(),ev.getUniqueId())).isEmpty()){
			for(DeliveryObject obj : objects.values()){
				getMysql().Update("INSERT INTO delivery_"+serverType.name()+" (player,uuid,obj,time) VALUES ('"+ev.getName()+"','"+UtilPlayer.getRealUUID(ev.getName(),ev.getUniqueId())+"','"+obj.displayname+"','"+(System.currentTimeMillis()+obj.time)+"');");
				players_obj.get(UtilPlayer.getRealUUID(ev.getName(),ev.getUniqueId())).put(obj.displayname, obj.time+System.currentTimeMillis());
			}
		}
	}
	
	@EventHandler
	public void Open(PlayerInteractAtEntityEvent ev){
		if(ev.getRightClicked().getEntityId() == entity.getEntityId()){
			ev.setCancelled(true);
			if(!players.containsKey(ev.getPlayer())){
				players.put(ev.getPlayer(), new DeliveryInventoryPage(InventorySize._45.getSize(), ev.getPlayer().getName()+" "+"Delivery",this));
				base.addPage(players.get(ev.getPlayer()));
				
				players.get(ev.getPlayer()).addButton(31, new ButtonBase(new Click(){

					@Override
					public void onClick(Player player, ActionType type, Object object) {
						if(lotto.getWin()==null){
							LottoPackage[] list = randomPackages(player);
							LottoPackage win = list[UtilMath.r(list.length)];
							lotto.setList(list);
							lotto.newRound(win);
						}else{
							player.sendMessage(Language.getText(player, "PREFIX")+" §cBESETZT");
						}
					}
					
				}, Material.JUKEBOX, "Lotto"));
				
				for(String obj : players_obj.get(UtilPlayer.getRealUUID(ev.getPlayer())).keySet()){
					if(players_obj.get(UtilPlayer.getRealUUID(ev.getPlayer())).get(obj)>System.currentTimeMillis()){
			    		  players.get(ev.getPlayer()).addButton(objects.get(obj).slot, new ButtonBase(objects.get(obj).click, Material.REDSTONE_BLOCK, objects.get(obj).displayname, descriptionUSED(ev.getPlayer(),obj)));
			    	  }else{
			    		  players.get(ev.getPlayer()).addButton(objects.get(obj).slot, new ButtonBase(objects.get(obj).click, objects.get(obj).material, objects.get(obj).displayname, objects.get(obj).description));
			    	  }
				}
				
				players.get(ev.getPlayer()).fill(Material.STAINED_GLASS_PANE, 7);
			}
			ev.getPlayer().openInventory(players.get(ev.getPlayer()));
		}
	}	
}