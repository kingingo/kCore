package eu.epicpvp.kcore.Kit.Perks;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Translation.TranslationManager;
import eu.epicpvp.kcore.Util.TimeSpan;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilParticle;
import eu.epicpvp.kcore.Util.UtilTime;

public class PerkPotionClear extends Perk{

	private HashMap<String,Long> timer = new HashMap<>();
	
	public PerkPotionClear() {
		super("PotionClear",UtilItem.RenameItem(new ItemStack(374),"§ePotionClear"));
	}
	
	public boolean is(Player player){
		if(timer.containsKey(player.getName())){
			if(timer.get(player.getName()) > System.currentTimeMillis()){
				return true;
			}
			timer.remove(player.getName());
			return false;
		}
		return false;
	}
	
	@EventHandler
	public void Click(PlayerInteractEntityEvent ev){
			if(getPerkData().hasPlayer(this, ev.getPlayer())){
				if(ev.getPlayer().isSneaking()){
						if(ev.getRightClicked() instanceof Player){
							if(!is(ev.getPlayer())){
							timer.put(ev.getPlayer().getName(), System.currentTimeMillis()+(TimeSpan.MINUTE*15));
							UtilParticle.FLAME.display(1, 20, ((Player)ev.getRightClicked()).getLocation(), 15);
							for(PotionEffect e : ((Player)ev.getRightClicked()).getActivePotionEffects()){
								if(e.getType()!=PotionEffectType.JUMP)((Player)ev.getRightClicked()).removePotionEffect(e.getType());
							}
							}else{
								ev.getPlayer().sendMessage(TranslationManager.getText(ev.getPlayer(), "PREFIX")+"§cDu musst noch §4"+UtilTime.formatMili( (timer.get(ev.getPlayer().getName())-System.currentTimeMillis()) )+"§c warten");
							}
						}
				}
			}
	}


}
