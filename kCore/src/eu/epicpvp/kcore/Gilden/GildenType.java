package eu.epicpvp.kcore.Gilden;

import dev.wolveringer.dataclient.gamestats.StatsKey;
import lombok.Getter;

public enum GildenType {
PVP("PvP-Server","PvP",new StatsKey[]{StatsKey.KILLS,StatsKey.DEATHS,StatsKey.ELO,StatsKey.LOC_X,StatsKey.LOC_Y,StatsKey.LOC_Z,StatsKey.WORLD}),
SKY("Sky-Server","Sky",new StatsKey[]{StatsKey.KILLS,StatsKey.DEATHS,StatsKey.MONEY}),
WARZ("WarZ-Server","WarZ",new StatsKey[]{StatsKey.KILLS,StatsKey.DEATHS});

@Getter
private String typ;
@Getter
private String Kuerzel;
@Getter
private StatsKey[] stats;

private GildenType(String Typ,String Kuerzel,StatsKey[] stats){
	this.typ=Typ;
	this.stats=stats;
	this.Kuerzel=Kuerzel;
}

}
