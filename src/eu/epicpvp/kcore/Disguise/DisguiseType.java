package eu.epicpvp.kcore.Disguise;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton.SkeletonType;

import eu.epicpvp.kcore.Disguise.disguises.DisguiseBase;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseBat;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseBlaze;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseBlock;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseCat;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseCaveSpider;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseChicken;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseCow;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseCreeper;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseEnderdragon;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseEnderman;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseGuardian;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseHorse;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseIronGolem;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseMagmaCube;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseMushroomCow;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguisePig;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguisePigZombie;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguisePlayer;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseRabbit;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseSheep;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseSkeleton;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseSlime;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseSnowman;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseSpider;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseVillager;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseWitch;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseWither;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseWolf;
import eu.epicpvp.kcore.Disguise.disguises.livings.DisguiseZombie;

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
CAVE_SPIDER,
SPIDER,
GUARDIAN,
MUSHROOM_COW,
RABBIT,
VILLAGER,
WITHER,
ENDERDRAGON,
WITHER_SKELETON,
SKELETON,
PLAYER;

public static DisguiseBase newDisguise(LivingEntity entity,DisguiseType type,Object[] o){
	switch(type){
	case ENDERDRAGON:
		return new DisguiseEnderdragon(entity);
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
	case BLOCK:
		DisguiseBlock d2 = new DisguiseBlock(entity,((int)o[0])); // ,((int)o[1])
		return d2;
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
	case WITHER_SKELETON: 
		DisguiseSkeleton s1 = new DisguiseSkeleton(entity);
		s1.SetName( (String)o[0] );
		s1.SetCustomNameVisible(true);
		s1.SetSkeletonType(SkeletonType.WITHER);
		return s1;
	case SKELETON: 
		DisguiseSkeleton s = new DisguiseSkeleton(entity);
		s.SetName( (String)o[0] );
		s.SetCustomNameVisible(true);
		s.SetSkeletonType(SkeletonType.NORMAL);
		return s;
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
	case MAGMA_CUBE:
		DisguiseMagmaCube d9 = new DisguiseMagmaCube(entity);
		d9.SetName( (String)o[0] );
		d9.SetCustomNameVisible(true);
		return d9;
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
	case SLIME:
		DisguiseSlime d14 = new DisguiseSlime(entity);
		d14.SetName( (String)o[0] );
		d14.SetCustomNameVisible(true);
		return d14;
	case WOLF: 
		DisguiseWolf d15 = new DisguiseWolf(entity);
		d15.SetName( (String)o[0] );
		d15.SetCustomNameVisible(true);
		return d15;
	case PLAYER:
		return new DisguisePlayer(entity,(String)o[0]);
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
	case CAVE_SPIDER: 
		DisguiseCaveSpider d20 = new DisguiseCaveSpider(entity);
		d20.SetName( (String)o[0] );
		d20.SetCustomNameVisible(true);
		return d20;
	case SPIDER: 
		DisguiseSpider d21 = new DisguiseSpider(entity);
		d21.SetName( (String)o[0] );
		d21.SetCustomNameVisible(true);
		return d21;
	case GUARDIAN:
		DisguiseGuardian d22 = new DisguiseGuardian(entity);
		d22.SetName( (String)o[0] );
		d22.SetCustomNameVisible(true);
		return d22;
	case MUSHROOM_COW:
		DisguiseMushroomCow d23 = new DisguiseMushroomCow(entity);
		d23.SetName( (String)o[0] );
		d23.SetCustomNameVisible(true);
		return d23;
	case RABBIT:
		DisguiseRabbit d24 = new DisguiseRabbit(entity);
		d24.SetName( (String)o[0] );
		d24.SetCustomNameVisible(true);
		return d24;
	case VILLAGER:
		DisguiseVillager d25 = new DisguiseVillager(entity);
		d25.SetName( (String)o[0] );
		d25.SetCustomNameVisible(true);
		return d25;
	case WITHER:
		DisguiseWither d26 = new DisguiseWither(entity);
		d26.SetName( (String)o[0] );
		d26.SetCustomNameVisible(true);
		return d26;
	default:
		return new DisguisePig(entity);
	}
}

}
