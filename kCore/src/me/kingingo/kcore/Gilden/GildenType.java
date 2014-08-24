package me.kingingo.kcore.Gilden;

import lombok.Getter;
import me.kingingo.kcore.PlayerStats.Stats;

public enum GildenType {
PvP("PvP-Server","pvp",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WORLD,Stats.LOC_X,Stats.LOC_Y,Stats.LOC_Z}),
Sky("Sky-Server","sky",new Stats[]{Stats.KILLS,Stats.DEATHS}),
WarZ("WarZ-Server","warz",new Stats[]{Stats.KILLS,Stats.DEATHS});

private String typ;
private String K�rzel;
@Getter
private Stats[] stats;
private GildenType(String Typ,String K�rzel,Stats[] stats){
	this.typ=Typ;
	this.stats=stats;
	this.K�rzel=K�rzel;
}

public String getK�rzel(){
	return this.K�rzel;
}

public String string(){
	return typ;
}

}
