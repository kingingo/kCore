package me.kingingo.kcore.Gilden;

import lombok.Getter;
import me.kingingo.kcore.StatsManager.Stats;

public enum GildenType {
PVP("PvP-Server","PvP",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.LOC_X,Stats.LOC_Y,Stats.LOC_Z,Stats.WORLD}),
SKY("Sky-Server","Sky",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.MONEY}),
WARZ("WarZ-Server","WarZ",new Stats[]{Stats.KILLS,Stats.DEATHS});

@Getter
private String typ;
@Getter
private String Kürzel;
@Getter
private Stats[] stats;

private GildenType(String Typ,String Kürzel,Stats[] stats){
	this.typ=Typ;
	this.stats=stats;
	this.Kürzel=Kürzel;
}

}
