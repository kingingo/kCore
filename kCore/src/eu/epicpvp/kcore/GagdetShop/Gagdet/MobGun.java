package eu.epicpvp.kcore.GagdetShop.Gagdet;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftAgeable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import eu.epicpvp.kcore.Enum.PlayerState;
import eu.epicpvp.kcore.GagdetShop.GadgetHandler;
import eu.epicpvp.kcore.Scheduler.kScheduler;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilEvent;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilFirework;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilMath;

public class MobGun extends Gadget{
	
	public MobGun(GadgetHandler handler) {
		super(handler, "MobGun", UtilItem.RenameItem(new ItemStack(356), "§eMobGun"));
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent ev){
		if(this.getActive_player().containsKey(ev.getPlayer())
				&& ev.getPlayer().getItemInHand()!=null 
				&& ev.getPlayer().getItemInHand().getType() == getItem().getType()
				&& UtilEvent.isAction(ev, ActionType.R)
				&& use(ev.getPlayer())){
			long time = System.currentTimeMillis();
			for(int i = 0 ; i<4; i++){
				CraftAgeable e = (CraftAgeable) ev.getPlayer().getWorld().spawnCreature(ev.getPlayer().getLocation(), EntityType.SHEEP);
				e.setCustomName("jeb_");
				
				e.setVelocity(ev.getPlayer().getLocation().getDirection().add(new Vector(UtilMath.RandomDouble(-0.2, 0.5),1,UtilMath.RandomDouble(-0.2, 0.5))));
				
				 new kScheduler(getHandler().getInstance(), new kScheduler.kSchedulerHandler() {
						
						@Override
						public void onRun(kScheduler scheduler) {
							if(e.isAdult()){
								e.setBaby();
							}else{
								e.setAdult();
							}
							
							if(e == null || e.isDead() || e.isOnGround() || ( (time+(TimeSpan.SECOND*5)) < System.currentTimeMillis())){
								if(e!=null){
									e.remove();
									UtilFirework.start(e.getLocation(), UtilFirework.RandomColor(), UtilFirework.RandomType());
								}
								scheduler.close();
							}
						}
					}, UpdateType.FASTER);
			}
		}
	}
	
}
