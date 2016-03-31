package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Kit.Perks.Event.PerkPlayerAddEvent;
import eu.epicpvp.kcore.Kit.Perks.Event.PerkPlayerRemoveEvent;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class PerkDoubleJump extends Perk{
	
	private PotionEffectType typ;
	private int staerke;
	
	public PerkDoubleJump() {
		super("DoubleJump",UtilItem.RenameItem(new ItemStack(Material.IRON_BOOTS),"§eDoubleJump"));
		this.staerke=2;
		this.typ=PotionEffectType.JUMP;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void respawn(PlayerRespawnEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer())){
			UtilPlayer.addPotionEffect(ev.getPlayer(), PotionEffectType.INCREASE_DAMAGE, 999999,1);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PerkPlayerAddEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer()) && (ev.getPerkString()==null||ev.getPerkString().equalsIgnoreCase(getName())) ){
			UtilPlayer.addPotionEffect(ev.getPlayer(), typ, 999999,staerke);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PerkPlayerRemoveEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer()) && (ev.getPerkString()==null||ev.getPerkString().equalsIgnoreCase(getName())) ){
			ev.getPlayer().removePotionEffect(typ);
		}
	}
}
