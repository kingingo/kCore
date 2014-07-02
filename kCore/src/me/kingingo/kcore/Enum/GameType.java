package me.kingingo.kcore.Enum;

import lombok.Getter;
import me.kingingo.kcore.PlayerStats.Stats;

public enum GameType {
SurvivalGames("SurvivalGames","SG",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
OneInTheChamber("OneInTheChamber","OITC",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
SkyPvP("SkyPvP","SK",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
Falldown("Falldown","FD",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
Rush("Rush","RUSH",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
TroubleMine("TroubleMine","TIMV",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.TM_KARMA,Stats.TM_PÄSSE,Stats.TM_TESTS}),
TroubleInMinecraft("TroubleInMinecraft","TTT",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.TM_KARMA,Stats.TM_PÄSSE,Stats.TM_TESTS}),
EnderMode("EnderMode","EM",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
MegaRush("MegaRush","MR",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
BeastMode("BeastMode","BM",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
MarioParty("MarioParty","MP",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
NONE("NONE","FAIL",null);

private String typ;
private String Kürzel;
@Getter
private Stats[] stats;
private GameType(String Typ,String Kürzel,Stats[] stats){
	this.typ=Typ;
	this.stats=stats;
	this.Kürzel=Kürzel;
}

public String getKürzel(){
	return this.Kürzel;
}

public String string(){
	return typ;
}

}
