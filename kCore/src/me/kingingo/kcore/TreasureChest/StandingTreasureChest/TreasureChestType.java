package me.kingingo.kcore.TreasureChest.StandingTreasureChest;

import lombok.Getter;
import me.kingingo.kcore.Util.UtilMath;

public enum TreasureChestType {
UNCOMMON(new Integer[]{1,2,3,4,5},0),
RARE(new Integer[]{6,7,8},2),
MYTHICAL(new Integer[]{9,10},4);

@Getter
private Integer[] ints;
@Getter
private int chance=0;

private TreasureChestType(Integer[] ints,int chance){
	this.ints=ints;
	this.chance=chance;
}

public static TreasureChestType rdm(TreasureChestType type){
	int rdm = UtilMath.RandomInt(10, 1);
	rdm+=type.getChance();
	return getType((rdm>10 ? 10 : rdm));
}

public static TreasureChestType getType(int t){
	for(TreasureChestType c : values()){
		for(int i : c.getInts()){
			if(i==t)return c;
		}
	}
	return null;
}

}
