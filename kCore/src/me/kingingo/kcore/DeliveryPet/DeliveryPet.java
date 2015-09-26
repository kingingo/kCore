package me.kingingo.kcore.DeliveryPet;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Enum.ServerType;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.Hologram.nametags.NameTagMessage;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.Inventory.DeliveryInventoryPage;
import me.kingingo.kcore.Inventory.Inventory.InventoryLotto2;
import me.kingingo.kcore.Inventory.Inventory.InventoryLotto2.InventoryLotto2Type;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.Get;
import me.kingingo.kcore.Inventory.Item.LottoPackage;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.MySQL.MySQLErr;
import me.kingingo.kcore.MySQL.Events.MySQLErrorEvent;
import me.kingingo.kcore.Packet.PacketManager;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilDebug;
import me.kingingo.kcore.Util.UtilEffect;
import me.kingingo.kcore.Util.UtilEnt;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilList;
import me.kingingo.kcore.Util.UtilLocation;
import me.kingingo.kcore.Util.UtilMath;
import me.kingingo.kcore.Util.UtilNumber;
import me.kingingo.kcore.Util.UtilParticle;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilServer;
import me.kingingo.kcore.Util.UtilTime;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

public class DeliveryPet extends kListener{

	@Getter
	private InventoryBase base;
	@Getter
	private InventoryLotto2 lotto;
	@Getter
	private Creature entity;
	@Getter
	private Creature jockey;
	private HashMap<Material,DeliveryObject> objects;
	private HashMap<Player,DeliveryInventoryPage> players;
	private HashMap<Player,NameTagMessage> players_hm;
	private HashMap<Player,Integer> players_hm_reward;
	private HashMap<UUID,HashMap<Material,Long>> players_obj;
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
	@Getter
	private PacketManager packetManager;
	
	public DeliveryPet(HashMap<InventoryLotto2Type, ArrayList<LottoPackage>> pack, DeliveryObject[] objects,String name,EntityType type,Location location,ServerType serverType,Hologram hm,MySQL mysql) {
		super(mysql.getInstance(), "DeliveryPet");
		this.packages=pack;
		this.packetManager=packetManager;
		this.mysql=mysql;
		this.type=type;
		this.location=location;
		this.name=name;
		getMysql().Update("CREATE TABLE IF NOT EXISTS delivery_"+serverType.name()+"(player varchar(30),uuid varchar(100), obj varchar(30), time varchar(100), date varchar(100))");
		this.serverType=serverType;
		this.hologramm=hm;
		this.objects=new HashMap<>();
		for(DeliveryObject obj : objects)this.objects.put(obj.material, obj);
		this.players_obj=new HashMap<>();
		this.players=new HashMap<>();
		this.players_hm=new HashMap<>();
		this.players_hm_reward=new HashMap<>();
		this.base=new InventoryBase(getMysql().getInstance(), "Delivery");
		if(pack!=null)this.lotto=new InventoryLotto2("Play a Round!",new Get(){

			@Override
			public Object onGet(Player player) {
				LottoPackage[] ps = new LottoPackage[18];
				
				int common=8;
				int uncommon=5;
				int rare=3;
				int legendary=1;
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
				
				LottoPackage[] ps1 =new LottoPackage[ps.length];
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
			
		},4,7, getMysql().getInstance());
		if(pack!=null)this.base.addPage(lotto);
		createPet();
		UtilServer.createDeliveryPet(this);
	}
	
	public void createPet(){
		if(this.jockey==null||this.jockey.isDead()){
			this.jockey=(Creature)getLocation().getWorld().spawnCreature(getLocation(), getType());
			this.jockey.setCustomName("");
			this.jockey.setCanPickupItems(false);
			this.jockey.setCustomNameVisible(true);
			this.jockey.setRemoveWhenFarAway(false);
			UtilEnt.setNoAI(jockey, true);
			
			if(jockey.getType()==EntityType.ENDERMAN){
				Enderman e = (Enderman) this.jockey;
				e.setCanPickupItems(false);
				e.setRemoveWhenFarAway(false);
				e.setTarget(null);
			}else if(jockey.getType()==EntityType.CHICKEN){
				this.entity=(Creature)getLocation().getWorld().spawnCreature(getLocation(), EntityType.ZOMBIE);
				this.entity.setCustomName("");
				this.entity.setCustomNameVisible(true);
				this.entity.setRemoveWhenFarAway(false);
				this.entity.setCanPickupItems(false);
				Zombie zombie = (Zombie)entity;
				zombie.setBaby(true);
				zombie.setVillager(false);
				UtilEnt.setNoAI(entity, true);
				this.jockey.setPassenger(this.entity);
			}
		}
	}
	
	public void onDisable(){
		this.jockey.remove();
		if(this.entity!=null)this.entity.remove();
	}
	
	@EventHandler
	public void join(PlayerJoinEvent ev){
		if(players_hm.containsKey(ev.getPlayer())){
			players_hm.get(ev.getPlayer()).sendToPlayer(ev.getPlayer());
		}else{
			players_hm_reward.put(ev.getPlayer(), getRewards(ev.getPlayer()));
			players_hm.put(ev.getPlayer(), getHologramm().setName( (this.entity!=null?this.entity:this.jockey) , ev.getPlayer(), new String[]{Language.getText(ev.getPlayer(), (players_hm_reward.get(ev.getPlayer())>1?"DELIVERY_HM_1_MORE":"DELIVERY_HM_1"),"§f§l"+players_hm_reward.get(ev.getPlayer())),name,Language.getText(ev.getPlayer(),"DELIVERY_HM_3")}));
		}
	}
	
	public void playEffect(){
		UtilEffect.playHelix(getLocation(),UtilParticle.FIREWORKS_SPARK);
		
		try{
			Entity e;
			for(Player player : UtilPlayer.getInRadius(this.jockey.getLocation(), 8).keySet()){
				e=this.jockey.getLocation().getWorld().spawnEntity(this.jockey.getLocation().add(0, 3, 0), EntityType.EGG);
				e.setVelocity( e.getVelocity().add(UtilLocation.calculateVector(e.getLocation(), player.getEyeLocation())) );
				UtilPlayer.addPotionEffect(player, PotionEffectType.SPEED, 10, 2);
			}
		}catch(IllegalStateException e){
			
		}
	}
	
	public int getRewards(Player player){
		int i = 0;
		
		for(Material obj : players_obj.get(UtilPlayer.getRealUUID(player)).keySet()){
			if(!(players_obj.get(UtilPlayer.getRealUUID(player)).get(obj) > System.currentTimeMillis())){
				i++;
			}
		}
		
		return i;
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent ev){
		if(players.containsKey(ev.getPlayer()))players.remove(ev.getPlayer());
		if(players_obj.containsKey(UtilPlayer.getRealUUID(ev.getPlayer())))players_obj.remove(UtilPlayer.getRealUUID(ev.getPlayer()));
	}
	
	@EventHandler
	public void teleport(EntityTeleportEvent ev){
		if(ev.getEntity().getEntityId()==this.jockey.getEntityId()){
			ev.setCancelled(true);
		}
		if(this.entity!=null&&ev.getEntity().getEntityId()==this.entity.getEntityId()){
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void flame(EntityCombustEvent ev){
		if(ev.getEntity().getEntityId()==this.jockey.getEntityId())ev.setCancelled(true);
		if(this.entity!=null&&ev.getEntity().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
	}
	
	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev){
		if(ev.getEntity().getEntityId()==this.jockey.getEntityId())ev.setCancelled(true);
		if(ev.getDamager().getEntityId()==this.jockey.getEntityId())ev.setCancelled(true);
		if(this.entity!=null&&ev.getEntity().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
		if(this.entity!=null&&ev.getDamager().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
	}
	
	@EventHandler
	public void Damage(EntityDamageEvent ev){
		if(ev.getEntity().getEntityId()==this.jockey.getEntityId())ev.setCancelled(true);
		if(this.entity!=null&&ev.getEntity().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
	}
	
	@EventHandler
	public void target(EntityTargetEvent ev){
		if(ev.getEntity().getEntityId()==this.jockey.getEntityId())ev.setCancelled(true);
		if(this.entity!=null&&ev.getEntity().getEntityId()==this.entity.getEntityId())ev.setCancelled(true);
	}
	
	@EventHandler
	public void Updater(UpdateEvent ev){
		if(ev.getType()==UpdateType.MIN_64){
			UtilList.CleanList(players,base);
			UtilList.CleanList(players_obj);
			UtilList.CleanList(players_hm);
			UtilList.CleanList(players_hm_reward);
		}
		
		if(ev.getType()==UpdateType.MIN_04){
			createPet();
		}
		
		if(ev.getType()==UpdateType.SEC){
			for(Player player : players_hm.keySet()){
				if(players_hm_reward.containsKey(player)){
					if(players_hm_reward.get(player)==0){
						if(!players_hm.get(player).getLines()[0].startsWith("§7")){
							players_hm.get(player).setLines(0, Language.getText(player, (players_hm_reward.get(player)>1?"DELIVERY_HM_1_MORE":"DELIVERY_HM_1"),"§7"+players_hm_reward.get(player)));
							players_hm.get(player).clear(player);
							players_hm.get(player).sendToPlayer(player);
						}
					}else{
						if(players_hm.get(player).getLines()[0].startsWith("§f§l")||players_hm.get(player).getLines()[0].startsWith("§7")){
							players_hm.get(player).setLines(0, Language.getText(player, (players_hm_reward.get(player)>1?"DELIVERY_HM_1_MORE":"DELIVERY_HM_1"),"§c§l"+players_hm_reward.get(player)));
						}else{
							players_hm.get(player).setLines(0, Language.getText(player, (players_hm_reward.get(player)>1?"DELIVERY_HM_1_MORE":"DELIVERY_HM_1"),"§f§l"+players_hm_reward.get(player)));
						}
						players_hm.get(player).clear(player);
						players_hm.get(player).sendToPlayer(player);
					}
				}
			}
		}
		
		if(ev.getType()==UpdateType.SEC){
			for(Player player : players.keySet()){
				if(player.isOnline()&&!players.get(player).getViewers().isEmpty()){
					if(players_obj.containsKey( UtilPlayer.getRealUUID(player) )){
						for(Material obj : players_obj.get(UtilPlayer.getRealUUID(player)).keySet()){
							if(players_obj.get(UtilPlayer.getRealUUID(player)).get(obj) > System.currentTimeMillis()){
								players.get(player).getButton(objects.get(obj).slot).setDescription(descriptionUSED(player, obj));
								if(players.get(player).getButton(objects.get(obj).slot).getItemStack().getType()!=Material.REDSTONE_BLOCK)players.get(player).getButton(objects.get(obj).slot).setMaterial(Material.REDSTONE_BLOCK);
								players.get(player).getButton(objects.get(obj).slot).refreshItemStack();
							}else{
								if(players.get(player).getButton(objects.get(obj).slot).getItemStack().getType()==Material.REDSTONE_BLOCK){
									players.get(player).getButton(objects.get(obj).slot).setDescription(objects.get(obj).description);
									players.get(player).getButton(objects.get(obj).slot).setMaterial(objects.get(obj).material,objects.get(obj).data);	
									players.get(player).getButton(objects.get(obj).slot).refreshItemStack();
								}
							}
						}
					}else{
						Log("UpdaterEvent Spieler "+player.getName()+" nicht gefunden...");
					}
				}
			}
		}
	}
	
	public void deliveryBlock(Player player,Material name){
		getMysql().Update("UPDATE delivery_"+serverType.name()+" SET time='"+(System.currentTimeMillis()+objects.get(name).getTime())+"', date='"+UtilTime.when((System.currentTimeMillis()+objects.get(name).getTime()))+"' WHERE uuid='"+UtilPlayer.getRealUUID(player)+"' AND obj='"+name+"'");
		players_obj.get(UtilPlayer.getRealUUID(player)).remove(objects.get(name).material);
		players_obj.get(UtilPlayer.getRealUUID(player)).put(objects.get(name).material, System.currentTimeMillis()+objects.get(name).getTime());
		players_hm_reward.remove(player);
		players_hm_reward.put(player, getRewards(player));
		playEffect();
	}
	
	public void deliveryUSE(String player, UUID uuid,Material name){
		if(objects.get(name).material==name){
			if(players_obj.containsKey(uuid))players_obj.remove(uuid);
			getMysql().Update("UPDATE delivery_"+serverType.name()+" SET time='"+(System.currentTimeMillis()+objects.get(name).getTime())+"', date='"+UtilTime.when((System.currentTimeMillis()+objects.get(name).getTime()))+"' WHERE uuid='"+UtilPlayer.getRealUUID(player,uuid)+"' AND obj='"+name+"'");
		}
	}
	
	public void deliveryUSE(Player player,Material name,boolean b){
		if(objects.containsKey(name)){
			if(objects.get(name).material==name){
				if(objects.get(name).permission != null){
					if(!player.hasPermission(objects.get(name).permission.getPermissionToString())){
						return;
					}
				}
				
				if(b || objects.get(name).byClickBlock){
					deliveryBlock(player,name);
				}
				objects.get(name).click.onClick(player, ActionType.R, objects.get(name));
			}
		}else{
			UtilDebug.debug("deliveryUSE(Player,String,boolean)", new String[]{"objects.containsKey(name) == FALSE","Material: "+name} );
		}
	}
	
	public String[] descriptionUSED(Player player,Material obj){
		return new String[]{Language.getText(player, "DELIVERY_USED", UtilTime.formatMili( players_obj.get(UtilPlayer.getRealUUID(player)).get(obj)-System.currentTimeMillis() ))};
	}
	
	@EventHandler
	public void Login(AsyncPlayerPreLoginEvent ev){
		if(!players_obj.containsKey(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId())))players_obj.put(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()), new HashMap<Material,Long>());
		
		try
	    {
	      ResultSet rs = getMysql().Query("SELECT obj,time FROM delivery_"+serverType.name()+" WHERE uuid='"+UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId())+"';");

	      while (rs.next()) {
	    	  players_obj.get(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId())).put(Material.valueOf(rs.getString(1)), UtilNumber.toLong(rs.getString(2)));
	      }
	      rs.close();
	    } catch (Exception err) {
	    	Bukkit.getPluginManager().callEvent(new MySQLErrorEvent(MySQLErr.QUERY,err,getMysql()));
	    }
		
		if(players_obj.get(UtilPlayer.getRealUUID(ev.getName(),ev.getUniqueId())).isEmpty()){
			for(DeliveryObject obj : objects.values()){
				getMysql().Update("INSERT INTO delivery_"+serverType.name()+" (player,uuid,obj,time,date) VALUES ('"+ev.getName()+"','"+UtilPlayer.getRealUUID(ev.getName(),ev.getUniqueId())+"','"+obj.material+"','"+(System.currentTimeMillis())+"','"+UtilTime.when(System.currentTimeMillis())+"');");
				players_obj.get(UtilPlayer.getRealUUID(ev.getName(),ev.getUniqueId())).put(obj.material, System.currentTimeMillis());
			}
		}else{
			for(Material obj : objects.keySet()){
				if(!players_obj.get(UtilPlayer.getRealUUID(ev.getName(),ev.getUniqueId())).containsKey(obj) ){
					getMysql().Update("INSERT INTO delivery_"+serverType.name()+" (player,uuid,obj,time,date) VALUES ('"+ev.getName()+"','"+UtilPlayer.getRealUUID(ev.getName(),ev.getUniqueId())+"','"+objects.get(obj).material+"','"+(System.currentTimeMillis())+"','"+UtilTime.when(System.currentTimeMillis())+"');");
					players_obj.get(UtilPlayer.getRealUUID(ev.getName(),ev.getUniqueId())).put(objects.get(obj).material, System.currentTimeMillis());
				}
			}
		}
	}
	
	@EventHandler
	public void Open(PlayerInteractAtEntityEvent ev){
		if(ev.getRightClicked().getEntityId() == jockey.getEntityId()||(this.entity!=null&&ev.getRightClicked().getEntityId() == entity.getEntityId())){
			ev.setCancelled(true);
			if(!players.containsKey(ev.getPlayer())){
				players.put(ev.getPlayer(), new DeliveryInventoryPage(InventorySize._45.getSize(), ev.getPlayer().getName()+" "+"Delivery",this));
				base.addPage(players.get(ev.getPlayer()));
				
				if(this.packages!=null){
					players.get(ev.getPlayer()).addButton(31, new ButtonBase(new Click(){

						@Override
						public void onClick(Player player, ActionType type, Object object) {
							if(lotto.getWin()==null){
								lotto.newRound(player);
							}else{
								player.sendMessage(Language.getText(player, "PREFIX")+ Language.getText(player, "DELIVERY_LOTTO_USED"));
							}
						}
						
					}, Material.JUKEBOX, "§7Lotto"));	
				}
				
				if(!players_obj.containsKey(UtilPlayer.getRealUUID(ev.getPlayer()))){
					Log("players_obj Spieler "+ev.getPlayer()+" nicht gefunden!");
				}else if(players_obj.get(UtilPlayer.getRealUUID(ev.getPlayer())).isEmpty()){
					Log("players_obj Spieler "+ev.getPlayer()+" liste ist leer!");
				}else{
					for(Material obj : players_obj.get(UtilPlayer.getRealUUID(ev.getPlayer())).keySet()){
						if(players_obj.get(UtilPlayer.getRealUUID(ev.getPlayer())).get(obj)>System.currentTimeMillis()){
				    		  players.get(ev.getPlayer()).addButton(objects.get(obj).slot, new ButtonBase(objects.get(obj).click, Material.REDSTONE_BLOCK, objects.get(obj).displayname, descriptionUSED(ev.getPlayer(),obj)));
				    	  }else{
				    		  players.get(ev.getPlayer()).addButton(objects.get(obj).slot, new ButtonBase(objects.get(obj).click, objects.get(obj).material, objects.get(obj).data, objects.get(obj).displayname, objects.get(obj).description));
				    	  }
					}
				}
				
				players.get(ev.getPlayer()).fill(Material.STAINED_GLASS_PANE, 7);
			}
			ev.getPlayer().openInventory(players.get(ev.getPlayer()));
		}
	}	
}