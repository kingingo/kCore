package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Util.UtilMath;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PerkHealByHit extends Perk{

	int herz;
	int chance;
	
	public PerkHealByHit(int chance,int herz){
		super("HealByHit", Text.PERK_HEALBYHIT.getTexts(new String[]{String.valueOf(chance),String.valueOf(herz)}));
		this.chance=chance;
		this.herz=herz;
	}
	
	Player defend;
	Player attack;
	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev){
		if(ev.getEntity() instanceof Player&&ev.getDamager() instanceof Player){
			defend=(Player)ev.getEntity();
			attack=(Player)ev.getDamager();
			if(!this.getKit().hasPlayer(this,attack))return;
			if(UtilMath.RandomInt(100, 1) < chance){
				UtilPlayer.health(attack, herz);
			}
		}
	}
	

}
