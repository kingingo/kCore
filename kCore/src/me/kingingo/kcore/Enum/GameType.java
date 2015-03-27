package me.kingingo.kcore.Enum;

import lombok.Getter;
import me.kingingo.kcore.PlayerStats.Stats;

public enum GameType {
SurvivalGames("SurvivalGames","SG",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
OneInTheChamber("OneInTheChamber","OITC",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
SkyPvP("SkyPvP","SK",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
Falldown("Falldown","FD",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.POWER,Stats.WIN,Stats.LOSE}),
Rush("Rush","RUSH",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
TroubleMine("TroubleMine","TIMV",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.TTT_KARMA,Stats.TTT_PÄSSE,Stats.TTT_TESTS,Stats.TTT_TRAITOR_PUNKTE,Stats.TTT_DETECTIVE_PUNKTE}),
TroubleInMinecraft("TroubleInMinecraft","TTT",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.TTT_KARMA,Stats.TTT_PÄSSE,Stats.TTT_TESTS,Stats.TTT_TRAITOR_PUNKTE,Stats.TTT_DETECTIVE_PUNKTE}),
DeathGames("DeathGames","DG",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
MegaRush("MegaRush","MR",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
BeastMode("BeastMode","BM",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
SheepWars8("SheepWars8","SW",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.SHEEPWARS_KILLED_SHEEPS}),
SheepWars16("SheepWars16","SW",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.SHEEPWARS_KILLED_SHEEPS}),
MarioParty("MarioParty","MP",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
PVP("PvP-Server", "PvP", ServerType.PVP,new Stats[] { Stats.KILLS, Stats.DEATHS, Stats.MONEY }), 
ArcadeGames("ArcadeGames","AG",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
SKYBLOCK("SkyBlock", "Sky", ServerType.SKYBLOCK,new Stats[] { Stats.KILLS, Stats.DEATHS, Stats.MONEY }), 
WARZ("WarZ-Server", "WarZ", ServerType.WARZ,new Stats[] { Stats.KILLS, Stats.DEATHS,Stats.ANIMAL_KILLS,Stats.ANIMAL_DEATHS,Stats.MONSTER_KILLS,Stats.MONSTER_DEATHS }),
NONE("NONE","FAIL",ServerType.GAME,null);

@Getter
private String typ;
@Getter
private String Kürzel;
@Getter
private Stats[] stats;
@Getter
private ServerType serverType;

private GameType(String Typ,String Kürzel,ServerType serverType,Stats[] stats){
	this.typ=Typ;
	this.stats=stats;
	this.Kürzel=Kürzel;
	this.serverType=serverType;
}

}
