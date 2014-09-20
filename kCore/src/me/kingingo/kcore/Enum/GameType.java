package me.kingingo.kcore.Enum;

import lombok.Getter;
import me.kingingo.kcore.PlayerStats.Stats;

public enum GameType {
SurvivalGames("SurvivalGames","SG",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
OneInTheChamber("OneInTheChamber","OITC",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
SkyPvP("SkyPvP","SK",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
Falldown("Falldown","FD",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
Rush("Rush","RUSH",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
TroubleMine("TroubleMine","TIMV",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.TTT_KARMA,Stats.TTT_PÄSSE,Stats.TTT_TESTS,Stats.TTT_TRAITOR_PUNKTE,Stats.TTT_DETECTIVE_PUNKTE}),
TroubleInMinecraft("TroubleInMinecraft","TTT",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.TTT_KARMA,Stats.TTT_PÄSSE,Stats.TTT_TESTS,Stats.TTT_TRAITOR_PUNKTE,Stats.TTT_DETECTIVE_PUNKTE}),
EnderMode("EnderMode","EM",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
MegaRush("MegaRush","MR",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
BeastMode("BeastMode","BM",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
SheepWars8("SheepWars8","SW",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.SHEEPWARS_KILLED_SHEEPS}),
SheepWars16("SheepWars16","SW",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.SHEEPWARS_KILLED_SHEEPS}),
MarioParty("MarioParty","MP",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
PVP("PvP-Server","PvP",new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.MONEY}),
SKY("Sky-Server","Sky",new Stats[]{Stats.KILLS,Stats.DEATHS}),
WARZ("WarZ-Server","WarZ",new Stats[]{Stats.KILLS,Stats.DEATHS}),
NONE("NONE","FAIL",null);

@Getter
private String typ;
@Getter
private String Kürzel;
@Getter
private Stats[] stats;

private GameType(String Typ,String Kürzel,Stats[] stats){
	this.typ=Typ;
	this.stats=stats;
	this.Kürzel=Kürzel;
}

}
