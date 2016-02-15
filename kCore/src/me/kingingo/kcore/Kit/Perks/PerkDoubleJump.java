package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerAddEvent;
import me.kingingo.kcore.Kit.Perks.Event.PerkPlayerRemoveEvent;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class PerkDoubleJump extends Perk{
	
	private PotionEffectType typ;
	private int stärke;
	
	public PerkDoubleJump() {
		super("DoubleJump",UtilItem.RenameItem(new ItemStack(Material.IRON_BOOTS),"§eDoubleJump"));
		this.stärke=2;
		this.typ=PotionEffectType.JUMP;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PerkPlayerAddEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer()) && (ev.getPerkString()==null||ev.getPerkString().equalsIgnoreCase(getName())) ){
			UtilPlayer.addPotionEffect(ev.getPlayer(), typ, 999999,stärke);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PerkPlayerRemoveEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer()) && (ev.getPerkString()==null||ev.getPerkString().equalsIgnoreCase(getName())) ){
			ev.getPlayer().removePotionEffect(typ);
		}
	}
	
	

}
