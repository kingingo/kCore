package me.kingingo.kcore.Disguise;

import org.bukkit.entity.LivingEntity;

import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Disguise.disguises.livings.DisguiseBlaze;
import me.kingingo.kcore.Disguise.disguises.livings.DisguiseIronGolem;
import me.kingingo.kcore.Disguise.disguises.livings.DisguisePig;
import me.kingingo.kcore.Disguise.disguises.livings.DisguisePlayer;
import me.kingingo.kcore.Disguise.disguises.livings.DisguiseZombie;

public enum DisguiseType {
BLAZE,
IRON_GOLEM,
PIG,
ZOMBIE,
PLAYER;

public static DisguiseBase newDisguise(LivingEntity entity,DisguiseType type){
	switch(type){
	case BLAZE: return new DisguiseBlaze(entity);
	case IRON_GOLEM: return new DisguiseIronGolem(entity);
	case PIG: return new DisguisePig(entity);
	case ZOMBIE: return new DisguiseZombie(entity);
	}
	return null;
}

}
