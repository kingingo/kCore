package me.kingingo.kcore.Listener.EnderChest;

import java.lang.reflect.Field;

import me.kingingo.kcore.Listener.kListener;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Permission.Event.PlayerLoadPermissionEvent;
import me.kingingo.kcore.UserDataConfig.UserDataConfig;
import me.kingingo.kcore.UserDataConfig.Events.UserDataConfigRemoveEvent;
import me.kingingo.kcore.Util.UtilEvent;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.kConfig.kConfig;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.InventoryEnderChest;
import net.minecraft.server.v1_8_R3.InventorySubcontainer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

public class EnderChestListener extends kListener{

	private UserDataConfig userData;
	private kConfig config;
	
	public EnderChestListener(UserDataConfig userData) {
		super(userData.getInstance(),"EnderChestListener");
		this.userData=userData;
		if(Bukkit.getPluginManager().getPermission(kPermission.ENDERCHEST_USE.getPermissionToString())==null){
			  Bukkit.getPluginManager().addPermission(new Permission(kPermission.ENDERCHEST_USE.getPermissionToString()));
		}
	}
	
	@EventHandler
	public void Interact(PlayerInteractEvent ev){
		if(UtilEvent.isAction(ev, ActionType.R_BLOCK)){
			if(!ev.getPlayer().hasPermission(kPermission.ENDERCHEST_USE.getPermissionToString())){
				ev.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void LoadPerm(PlayerLoadPermissionEvent ev){
		if(ev.getPlayer().hasPermission(kPermission.ENDERCHEST_ADDON.getPermissionToString())&&userData.getConfigs().containsKey(UtilPlayer.getRealUUID(ev.getPlayer()))){
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
		ev.getManager().getPlist().get(UtilPlayer.getRealUUID(ev.getPlayer())).setPermission(kPermission.ENDERCHEST_USE.getPermissionToString(), true);
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
