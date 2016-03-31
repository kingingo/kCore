package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Util.UtilItem;
import lombok.Getter;
import lombok.Setter;

public class PerkArrowPotionEffect extends Perk{

	@Getter
	@Setter
	private PotionEffectType type=PotionEffectType.SLOW;
	@Getter
	@Setter
	private int sec=5;
	
	public PerkArrowPotionEffect() {
		super("ArrowPotionEffect",UtilItem.RenameItem(new ItemStack(Material.ARROW),"§EArrowPotionEffect"));
	}

	Player shooter;
	@EventHandler
	public void Shoot(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player&&ev.getDamager() instanceof Arrow){
			if(((Arrow)ev.getDamager()).getShooter() instanceof Player){
				shooter=(Player)((Arrow)ev.getDamager()).getShooter();
				if(!this.getPerkData().hasPlayer(this, shooter))return;
				((Player)ev.getEntity()).addPotionEffect(new PotionEffect(getType(), 20*sec,1));
			}
		}
	}
	
}
