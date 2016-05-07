package eu.epicpvp.kcore.GagdetShop.Gagdet;


import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.GagdetShop.GadgetHandler;
import eu.epicpvp.kcore.Scheduler.kScheduler;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilEvent;
import eu.epicpvp.kcore.Util.UtilFirework;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilParticle;

public class SlimeHead extends Gadget{

	public SlimeHead(GadgetHandler handler) {
		super(handler, "SlimeHead", UtilItem.RenameItem(new ItemStack(Material.SLIME_BALL), "§eSlimeHead"));
	}

	@EventHandler
	public void interact(PlayerInteractEvent ev){
		if(this.getActive_player().containsKey(ev.getPlayer())
				&& ev.getPlayer().getItemInHand()!=null 
				&& ev.getPlayer().getItemInHand().getType() == getItem().getType()
				&& UtilEvent.isAction(ev, ActionType.R)
				&& use(ev.getPlayer())){
			 ev.setCancelled(true);
			 long time = System.currentTimeMillis();
			 Entity[] slimes = new Entity[10];
			 
			 for(int i = 0; i<=10; i++){
				 if(i==0){
					 ArmorStand e = (ArmorStand) ev.getPlayer().getWorld().spawnEntity(ev.getPlayer().getLocation(), EntityType.ARMOR_STAND);
					 e.setVisible(true);
					 e.setSmall(true);
					 ev.getPlayer().setPassenger(e);
					 slimes[i]=e;
				 }else{
					 Slime slime = (Slime)ev.getPlayer().getWorld().spawnEntity(ev.getPlayer().getLocation(), EntityType.SLIME);
					 slime.setSize(i);
					 slimes[i]=slime;
					 
					 slimes[i-1].setPassenger(slime);
				 }
			 }
			 
			
			 new kScheduler(getHandler().getInstance(), new kScheduler.kSchedulerHandler() {
					
					@Override
					public void onRun(kScheduler scheduler) {
						if((time+(TimeSpan.SECOND*15)) < System.currentTimeMillis() ){
							for(Entity slime : slimes){
								if(slime!=null){
									UtilFirework.start(slime.getLocation(), Color.GREEN, Type.BURST);
									slime.remove();
								}
							}
							scheduler.close();
						}else if(ev.getPlayer().isSneaking() || (time+(TimeSpan.SECOND*10)) < System.currentTimeMillis() ){
							for(Entity slime : slimes){
								if(slime!=null){
									slime.leaveVehicle();
									if(slime.getType()==EntityType.ARMOR_STAND)slime.remove();
								}
							}
						}
					}
				}, UpdateType.FASTEST);
		}
	}
}
