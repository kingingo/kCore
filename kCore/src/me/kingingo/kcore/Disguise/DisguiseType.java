package me.kingingo.kcore.Disguise;

import org.bukkit.entity.LivingEntity;

import me.kingingo.kcore.Disguise.disguises.DisguiseBase;
import me.kingingo.kcore.Disguise.disguises.livings.*;

public enum DisguiseType {
BLAZE,
BAT,
BLOCK,
CAT,
CHICKEN,
COW,
CREEPER,
ENDERMAN,
HORSE,
MAGMA_CUBE,
PIG_ZOMBIE,
SHEEP,
SLIME,
SNOWMAN,
WITCH,
WOLF,
IRON_GOLEM,
PIG,
ZOMBIE,
PLAYER;

public static DisguiseBase newDisguise(LivingEntity entity,DisguiseType type,Object[] o){
	switch(type){
	case BLAZE: 
		DisguiseBlaze d = new DisguiseBlaze(entity);
		d.SetName((String)o[0]);
		d.SetCustomNameVisible(true);
		return d;
	case BAT: 
		DisguiseBat d1 = new DisguiseBat(entity);
		d1.SetName( (String)o[0] );
		d1.SetCustomNameVisible(true);
		return d1;
//	case BLOCK:
//		DisguiseBlock d2 = new DisguiseBlock(entity,((int)o[0]),((int)o[1]));
//		return d2;
	case CAT: 
		DisguiseCat d3=new DisguiseCat(entity);
		d3.SetName((String)o[0]);
		d3.SetCustomNameVisible(true);
		return d3;
	case CHICKEN: 
		DisguiseChicken d4 = new DisguiseChicken(entity);
		d4.SetName( (String)o[0] );
		d4.SetCustomNameVisible(true);
		return d4;
	case COW: 
		DisguiseCow d5 = new DisguiseCow(entity);
		d5.SetName( (String)o[0] );
		d5.SetCustomNameVisible(true);
		return d5;
	case CREEPER: 
		DisguiseCreeper d6 = new DisguiseCreeper(entity);
		d6.SetName((String)o[0]);
		d6.SetCustomNameVisible(true);
		return d6;
	case ENDERMAN:
		DisguiseEnderman d7 = new DisguiseEnderman(entity);
		d7.SetName( (String)o[0] );
		d7.SetCustomNameVisible(true);
		return d7;
	case HORSE:
		DisguiseHorse d8 = new DisguiseHorse(entity);
		d8.SetName((String)o[0]);
		d8.SetCustomNameVisible(true);
		return d8;
//	case MAGMA_CUBE:
//		DisguiseMagmaCube d9 = new DisguiseMagmaCube(entity);
//		d9.SetName( (String)o[0] );
//		d9.SetCustomNameVisible(true);
//		return d9;
	case PIG_ZOMBIE: 
		DisguisePigZombie d10 = new DisguisePigZombie(entity);
		d10.SetName( (String)o[0] );
		d10.SetCustomNameVisible(true);
		return d10;
	case SHEEP: 
		DisguiseSheep d11 = new DisguiseSheep(entity);
		d11.SetName( (String)o[0] );
		d11.SetCustomNameVisible(true);
		return d11;
	case SNOWMAN: 
		DisguiseSnowman d12 = new DisguiseSnowman(entity);
		d12.SetName( (String)o[0] );
		d12.SetCustomNameVisible(true);
		return d12;
	case WITCH: 
		DisguiseWitch d13 = new DisguiseWitch(entity);
		d13.SetName( (String)o[0] );
		d13.SetCustomNameVisible(true);
		return d13;
//	case SLIME:
//		DisguiseSlime d14 = new DisguiseSlime(entity);
//		d14.SetName( (String)o[0] );
//		d14.SetCustomNameVisible(true);
//		return d14;
	case WOLF: 
		DisguiseWolf d15 = new DisguiseWolf(entity);
		d15.SetName( (String)o[0] );
		d15.SetCustomNameVisible(true);
		return d15;
	//case PLAYER: return new DisguisePlayer(entity,(String)o[0]);
	case IRON_GOLEM:
		DisguiseIronGolem d17 = new DisguiseIronGolem(entity);
		d17.SetName( (String)o[0] );
		d17.SetCustomNameVisible(true);
		return d17;
	case PIG: 
		DisguisePig d18 = new DisguisePig(entity);
		d18.SetName( (String)o[0] );
		d18.SetCustomNameVisible(true);
		return d18;
	case ZOMBIE: 
		DisguiseZombie d19 = new DisguiseZombie(entity);
		d19.SetName( (String)o[0] );
		d19.SetCustomNameVisible(true);
		return d19;
	default:
		return new DisguisePig(entity);
	}
}

}
