package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Perk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class PerkNoKnockback extends Perk{

	public PerkNoKnockback() {
		super("noKnockback",Text.PERK_NOKNOCKBACK.getTexts());
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	  public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
	   if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
		   Player dm = (Player)e.getDamager();
		   if(!this.getKit().hasPlayer(this,dm))return;
		   Player op = (Player)e.getEntity();
		   dm.setVelocity(new Vector(0,0,0));
		   op.setVelocity(new Vector(0,0,0));
	   }
	  }
	
}
