package me.kingingo.kcore.Enum;

import lombok.Getter;
import me.kingingo.kcore.StatsManager.Stats;

public enum GameType {
SkyWars("SkyWars","SkyWars",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.LOSE,Stats.WIN}),
Versus("VERSUS","VS",ServerType.GAME,new Stats[]{Stats.KIT,Stats.KIT_RANDOM,Stats.TEAM_MAX,Stats.TEAM_MIN ,Stats.ELO,Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
QuickSurvivalGames("QuickSurvivalGames","QSG",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
SurvivalGames("SurvivalGames","SG",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
OneInTheChamber("OneInTheChamber","OITC",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
SkyPvP("SkyPvP","SK",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
Falldown("Falldown","FD",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.POWER,Stats.WIN,Stats.LOSE}),
TroubleInMinecraft("TroubleInMinecraft","TTT",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.TTT_KARMA,Stats.TTT_PÄSSE,Stats.TTT_TESTS,Stats.TTT_TRAITOR_PUNKTE,Stats.TTT_DETECTIVE_PUNKTE}),
DeathGames("DeathGames","DG",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
BedWars("BedWars","BW",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.BEDWARS_ZERSTOERTE_BEDs}),
SheepWars("SheepWars","SW",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.SHEEPWARS_KILLED_SHEEPS}),
PVP("PvP-Server", "PvP", ServerType.PVP,new Stats[] { Stats.KILLS, Stats.DEATHS, Stats.MONEY, Stats.ELO, Stats.TIME_ELO, Stats.TIME }), 
SKYBLOCK("SkyBlock", "Sky", ServerType.SKYBLOCK,new Stats[] { Stats.KILLS, Stats.DEATHS, Stats.MONEY }), 
WARZ("WarZ-Server", "WarZ", ServerType.WARZ,new Stats[] { Stats.KILLS, Stats.DEATHS,Stats.ANIMAL_KILLS,Stats.ANIMAL_DEATHS,Stats.MONSTER_KILLS,Stats.MONSTER_DEATHS }),
CaveWars("CaveWars","CW",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.SHEEPWARS_KILLED_SHEEPS}),
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
