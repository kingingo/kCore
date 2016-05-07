package eu.epicpvp.kcore.GagdetShop.Gagdet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.GagdetShop.GadgetHandler;
import eu.epicpvp.kcore.Scheduler.kScheduler;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Util.UtilEvent;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilLocation;
import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.Util.UtilParticle;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;

public class Pearl extends Gadget{

	public Pearl(GadgetHandler handler) {
		super(handler, "Pearl", UtilItem.RenameItem(new ItemStack(Material.ENDER_PEARL), "§ePearl"));
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent ev){
		if(this.getActive_player().containsKey(ev.getPlayer())
				&& ev.getPlayer().getItemInHand()!=null 
				&& ev.getPlayer().getItemInHand().getType() == getItem().getType()
				&& UtilEvent.isAction(ev, ActionType.R)
				&& use(ev.getPlayer())){
			 ev.setCancelled(true);
			EnderPearl e = (EnderPearl) ev.getPlayer().launchProjectile(EnderPearl.class, ev.getPlayer().getLocation().getDirection().add(new Vector(0,1,0)));
			e.setPassenger(ev.getPlayer());
			
			 new kScheduler(getHandler().getInstance(), new kScheduler.kSchedulerHandler() {
					
					@Override
					public void onRun(kScheduler scheduler) {
						if(e.isDead()||e.isOnGround()||e.isInsideVehicle()||e.getPassenger()==null){
							e.remove();
							scheduler.close();
						}else{
							UtilParticle.ENCHANTMENT_TABLE.display(0, 2, e.getLocation(), 20);
						}
					}
				}, UpdateType.FASTEST);
		}
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent ev) {
		if(getActive_player().containsKey(ev.getPlayer()) && ev.getCause() == TeleportCause.ENDER_PEARL) {
			if(ev.getTo().getBlock().getType()!=Material.AIR){
				ev.setTo(new Location(ev.getTo().getWorld(),ev.getTo().getX(),UtilLocation.getHighestY(ev.getTo()),ev.getTo().getZ()));
			}
	    }
	}
}
