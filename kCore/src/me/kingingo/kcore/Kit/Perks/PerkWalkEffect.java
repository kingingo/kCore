package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.UtilMath;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class PerkWalkEffect extends Perk{

	Effect effect;
	int i;
	
	public PerkWalkEffect(Effect effect, int i) {
		super("WalkEffect");
		this.effect=effect;
		this.i=i;
	}
	
	@EventHandler
	public void Update(UpdateEvent ev){
		if(ev.getType()!=UpdateType.FAST)return;
		if(!getPerkData().getPlayers().containsKey(this))return;
			for(Player p : getPerkData().getPlayers().get(this)){
				p.getLocation().getWorld().playEffect(p.getLocation().add(UtilMath.r(2), UtilMath.r(3), UtilMath.r(2)), this.effect,i);
				p.getLocation().getWorld().playEffect(p.getLocation().add(UtilMath.r(2), UtilMath.r(3), UtilMath.r(2)), this.effect,i);
				p.getLocation().getWorld().playEffect(p.getLocation().add(UtilMath.r(2), UtilMath.r(3), UtilMath.r(2)), this.effect,i);
				p.getLocation().getWorld().playEffect(p.getLocation().add(UtilMath.r(2), UtilMath.r(3), UtilMath.r(2)), this.effect,i);
				p.getLocation().getWorld().playEffect(p.getLocation().add(UtilMath.r(2), UtilMath.r(3), UtilMath.r(2)), this.effect,i);
				p.getLocation().getWorld().playEffect(p.getLocation().add(UtilMath.r(2), UtilMath.r(3), UtilMath.r(2)), this.effect,i);
			}
		
	}

}
