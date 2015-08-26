package me.kingingo.kcore.DeliveryPet;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.UnixRuntime.DevFS;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.kingingo.kcore.Enum.ServerType;
import me.kingingo.kcore.Hologram.Hologram;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Pet.PetManager;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEnt;

public class DeliveryPet extends kListener{

	@Getter
	private StatsManager statsManager;
	@Getter
	private PermissionManager permissionManager;
	@Getter
	private InventoryBase base;
	@Getter
	private Entity entity;
	private DeliveryObject[] objects;
	private HashMap<Player,InventoryPageBase> players;
	
	public DeliveryPet(DeliveryObject[] objects,String name,EntityType type,Location location,ServerType serverType,Hologram hm, StatsManager statsManager,PermissionManager permissionManager) {
		super(statsManager.getMysql().getInstance(), "DeliveryPet");
		statsManager.getMysql().Update("CREATE TABLE IF NOT EXISTS delivery_"+serverType.name()+"(player varchar(30),uuid varchar(30), date varchar(30), obj varchar(30), time varchar(100))");
		
		this.entity=location.getWorld().spawnEntity(location, type);
		this.entity.setCustomName("");
		this.entity.setCustomNameVisible(true);
		hm.setName(entity, name);
		UtilEnt.setNoAI(entity, true);
		
		this.permissionManager=permissionManager;
		this.statsManager=statsManager;
		this.objects=objects;
		this.players=new HashMap<>();
		
		this.base=new InventoryBase(statsManager.getMysql().getInstance(), "Delivery");
	}
	
	@EventHandler
	public void Open(PlayerInteractAtEntityEvent ev){
		if(ev.getRightClicked().getEntityId() == entity.getEntityId()){
			if(!players.containsKey(ev.getPlayer())){
				players.put(ev.getPlayer(), new InventoryPageBase(InventorySize._45.getSize(), ev.getPlayer().getName()+" "+"Delivery"));
				base.addPage(players.get(ev.getPlayer()));
				
				for(DeliveryObject obj : objects){
					players.get(ev.getPlayer()).addButton(obj.slot, new ButtonBase(obj.click,obj.material,obj.displayname,obj.description));
				}
			}
		}
	}
	
}
