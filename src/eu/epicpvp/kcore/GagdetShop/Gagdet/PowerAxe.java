package eu.epicpvp.kcore.GagdetShop.Gagdet;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.GagdetShop.GadgetHandler;
import eu.epicpvp.kcore.Scheduler.kScheduler;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilEvent;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilFirework;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilParticle;

public class PowerAxe extends Gadget{

	public PowerAxe(GadgetHandler handler) {
		super(handler, "PowerAxe", UtilItem.RenameItem(new ItemStack(Material.IRON_AXE), "§ePowerAxe"));
	}

	@EventHandler
	public void interact(PlayerInteractEvent ev){
		if(this.getActive_player().containsKey(ev.getPlayer())
				&& ev.getPlayer().getItemInHand()!=null 
				&& ev.getPlayer().getItemInHand().getType() == getItem().getType()
				&& UtilEvent.isAction(ev, ActionType.R)
				&& use(ev.getPlayer())){
			long time = System.currentTimeMillis();
			Item item = ev.getPlayer().getWorld().dropItemNaturally(ev.getPlayer().getEyeLocation(), new ItemStack(Material.IRON_AXE));
			item.setPickupDelay(100000);
			item.setVelocity(ev.getPlayer().getLocation().getDirection());
			
			new kScheduler(getHandler().getInstance(), new kScheduler.kSchedulerHandler() {
				
				@Override
				public void onRun(kScheduler scheduler) {
					if(item.isOnGround() || item.isDead() || (time+(TimeSpan.SECOND*10)) < System.currentTimeMillis() ){
						item.remove();
						scheduler.close();
						
						UtilFirework.start(item.getLocation(), Color.WHITE, Type.BALL);
					}else{
						UtilParticle.CRIT.display(0, 1, item.getLocation(), 20);
					}
				}
			}, UpdateType.TICK);
		}
	}
	
}
