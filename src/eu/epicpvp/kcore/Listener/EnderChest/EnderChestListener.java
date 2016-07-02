package eu.epicpvp.kcore.Listener.EnderChest;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Permission.Events.PlayerLoadPermissionEvent;
import eu.epicpvp.kcore.UserDataConfig.UserDataConfig;
import eu.epicpvp.kcore.UserDataConfig.Events.UserDataConfigRemoveEvent;
import eu.epicpvp.kcore.Util.UtilEvent;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.kConfig.kConfig;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.InventoryEnderChest;
import net.minecraft.server.v1_8_R3.InventorySubcontainer;

public class EnderChestListener extends kListener{

	private UserDataConfig userData;
	private kConfig config;
	
	public EnderChestListener(UserDataConfig userData) {
		super(userData.getInstance(),"EnderChestListener");
		this.userData=userData;
		if(Bukkit.getPluginManager().getPermission(PermissionType.ENDERCHEST_USE.getPermissionToString())==null){
			  Bukkit.getPluginManager().addPermission(new Permission(PermissionType.ENDERCHEST_USE.getPermissionToString()));
		}
	}
	
	@EventHandler
	public void Interact(PlayerInteractEvent ev){
		if(UtilEvent.isAction(ev, ActionType.RIGHT_BLOCK)){
			if(!ev.getPlayer().hasPermission(PermissionType.ENDERCHEST_USE.getPermissionToString())){
				ev.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void LoadPerm(PlayerLoadPermissionEvent ev){
		if(ev.getPlayer().hasPermission(PermissionType.ENDERCHEST_ADDON.getPermissionToString())&&userData.contains(ev.getPlayer())){
			config=userData.getConfig(ev.getPlayer());
			if(!config.isSet("Enderchest")){
				ItemStack[] items = new ItemStack[54];
				for(int i = 0; i< ev.getPlayer().getEnderChest().getContents().length;i++){
					items[i]= ev.getPlayer().getEnderChest().getContents()[i];
				}
				config.setItemStackArray("Enderchest", items);
			}
			setEnderchestAddon(ev.getPlayer(), UtilItem.convertItemStackArray(config.getItemStackArray("Enderchest")));
		}
		ev.getManager().getPermissionPlayer(ev.getPlayer()).addPermission(PermissionType.ENDERCHEST_USE.getPermissionToString());
	}
	
	@EventHandler
	public void Quit(UserDataConfigRemoveEvent ev){
		if(ev.getPlayer().getEnderChest().getContents().length==54){
			ev.getConfig().setItemStackArray("Enderchest", ev.getPlayer().getEnderChest().getContents());
		}
	}
	
	public void setEnderchestAddon(Player player,net.minecraft.server.v1_8_R3.ItemStack[] itemStack){
		CraftEntity ep = ((CraftEntity)((CraftPlayer)player));
		net.minecraft.server.v1_8_R3.Entity e = get(ep);
		EntityLiving living = (EntityLiving)e;
		EntityHuman human =(EntityHuman)living;
		InventoryEnderChest echest = human.getEnderChest();
		InventorySubcontainer s = (InventorySubcontainer)echest;
		set(s,itemStack);
	}
	
	private void set(InventorySubcontainer s,net.minecraft.server.v1_8_R3.ItemStack[] item){
	    try
	    {
	        Field pField = InventorySubcontainer.class.getDeclaredField("b");
	        pField.setAccessible(true);
	        pField.set(s, item.length);
	        
	        pField = InventorySubcontainer.class.getDeclaredField("items");
	        pField.setAccessible(true);
	        pField.set(s, item);
	        
	    } catch (Exception e) {
	      throw new RuntimeException(e);
	    }
	  }
	
	private net.minecraft.server.v1_8_R3.ItemStack[] get(InventorySubcontainer s){
	    try
	    {
	        Field pField = InventorySubcontainer.class.getDeclaredField("items");
	        pField.setAccessible(true);
	      return (net.minecraft.server.v1_8_R3.ItemStack[])pField.get(s);
	    } catch (Exception e) {
	      throw new RuntimeException(e);
	    }
	  }
	
	private net.minecraft.server.v1_8_R3.Entity get(CraftEntity ep){
	    try
	    {
	        Field pField = CraftEntity.class.getDeclaredField("entity");
	        pField.setAccessible(true);
	      
	      return (net.minecraft.server.v1_8_R3.Entity)pField.get(ep);
	    } catch (Exception e) {
	      throw new RuntimeException(e);
	    }
	  }

}
