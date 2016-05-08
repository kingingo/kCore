package eu.epicpvp.kcore.GagdetShop.Gagdet;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.GagdetShop.GadgetHandler;
import eu.epicpvp.kcore.Scheduler.kScheduler;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Util.UtilEffect;
import eu.epicpvp.kcore.Util.UtilEvent;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilParticle;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class Ragebow extends Gadget{

	public Ragebow(GadgetHandler handler) {
		super(handler, "Ragebow", UtilItem.RenameItem(new ItemStack(Material.BOW), "§eRagebow"));
	}

	@EventHandler
	public void interact(PlayerInteractEvent ev){
		if(this.getActive_player().containsKey(ev.getPlayer())
				&& ev.getPlayer().getItemInHand()!=null 
				&& ev.getPlayer().getItemInHand().getType() == getItem().getType()
				&& UtilEvent.isAction(ev, ActionType.R)
				&& use(ev.getPlayer())){
			Arrow arrow = ev.getPlayer().shootArrow();
			
			 new kScheduler(getHandler().getInstance(), new kScheduler.kSchedulerHandler() {
					
					@Override
					public void onRun(kScheduler scheduler) {
						if(arrow.isDead()||arrow.isOnGround()||arrow.isInsideVehicle()){
							arrow.remove();
							scheduler.close();
						}else{
							UtilParticle.FIREWORKS_SPARK.display(0, 2, arrow.getLocation(), 20);
						}
					}
				}, UpdateType.FASTEST);
			 ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void arrow(ProjectileHitEvent ev){
		if (ev.getEntityType() == EntityType.ARROW) {
			Arrow arrow = (Arrow)ev.getEntity();
			
			if(arrow.getShooter() instanceof Player && getActive_player().containsKey( ((Player)arrow.getShooter()) )){
				UtilEffect.playHelix(ev.getEntity().getLocation(),true,3, UtilParticle.FLAME);
				
				for(Player player : UtilPlayer.getInRadius(ev.getEntity().getLocation(), 6).keySet())
					UtilPlayer.Knockback(ev.getEntity().getLocation(), player, 0.4, 2);
			}
		}
	}
}
