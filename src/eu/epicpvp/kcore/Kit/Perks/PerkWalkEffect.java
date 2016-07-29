package eu.epicpvp.kcore.Kit.Perks;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilMath;

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
				p.getLocation().getWorld().playEffect(p.getLocation().add(UtilMath.randomInteger(2), UtilMath.randomInteger(3), UtilMath.randomInteger(2)), this.effect,i);
				p.getLocation().getWorld().playEffect(p.getLocation().add(UtilMath.randomInteger(2), UtilMath.randomInteger(3), UtilMath.randomInteger(2)), this.effect,i);
				p.getLocation().getWorld().playEffect(p.getLocation().add(UtilMath.randomInteger(2), UtilMath.randomInteger(3), UtilMath.randomInteger(2)), this.effect,i);
				p.getLocation().getWorld().playEffect(p.getLocation().add(UtilMath.randomInteger(2), UtilMath.randomInteger(3), UtilMath.randomInteger(2)), this.effect,i);
				p.getLocation().getWorld().playEffect(p.getLocation().add(UtilMath.randomInteger(2), UtilMath.randomInteger(3), UtilMath.randomInteger(2)), this.effect,i);
				p.getLocation().getWorld().playEffect(p.getLocation().add(UtilMath.randomInteger(2), UtilMath.randomInteger(3), UtilMath.randomInteger(2)), this.effect,i);
			}
		
	}

}
