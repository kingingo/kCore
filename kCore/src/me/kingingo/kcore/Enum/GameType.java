package me.kingingo.kcore.Enum;

import lombok.Getter;
import me.kingingo.kcore.StatsManager.Stats;

public enum GameType {
SurvivalGames1vs1(false,"SurvivalGames1vs1","SG1vs1",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
BedWars1vs1(false,"BedWars1vs1","BW1vs1",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.BEDWARS_ZERSTOERTE_BEDs}),
SkyWars1vs1(false,"SkyWars1vs1","SkyWars1vs1",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.LOSE,Stats.WIN}),
Versus(false,"VERSUS","VS",ServerType.GAME,new Stats[]{Stats.KIT_RANDOM,Stats.KIT_ID,Stats.TEAM_MAX,Stats.TEAM_MIN ,Stats.ELO,Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
SkyWars(true,"SkyWars","SkyWars",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.LOSE,Stats.WIN}),
QuickSurvivalGames(true,"QuickSurvivalGames","QSG",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
SurvivalGames(true,"SurvivalGames","SG",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
OneInTheChamber(true,"OneInTheChamber","OITC",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
SkyPvP(true,"SkyPvP","SK",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
Falldown(true,"Falldown","FD",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.POWER,Stats.WIN,Stats.LOSE}),
TroubleInMinecraft(true,"TroubleInMinecraft","TTT",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.TTT_KARMA,Stats.TTT_PÄSSE,Stats.TTT_TESTS,Stats.TTT_TRAITOR_PUNKTE,Stats.TTT_DETECTIVE_PUNKTE}),
DeathGames(true,"DeathGames","DG",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE}),
BedWars(true,"BedWars","BW",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.BEDWARS_ZERSTOERTE_BEDs}),
SheepWars(true,"SheepWars","SW",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.SHEEPWARS_KILLED_SHEEPS}),
PVP(true,"PvP-Server", "PvP", ServerType.PVP,new Stats[] { Stats.KILLS, Stats.DEATHS, Stats.MONEY, Stats.ELO, Stats.TIME_ELO, Stats.TIME }), 
SKYBLOCK(true,"SkyBlock", "Sky", ServerType.SKYBLOCK,new Stats[] { Stats.KILLS, Stats.DEATHS, Stats.MONEY }), 
WARZ(true,"WarZ-Server", "WarZ", ServerType.WARZ,new Stats[] { Stats.KILLS, Stats.DEATHS,Stats.ANIMAL_KILLS,Stats.ANIMAL_DEATHS,Stats.MONSTER_KILLS,Stats.MONSTER_DEATHS }),
CaveWars(true,"CaveWars","CW",ServerType.GAME,new Stats[]{Stats.KILLS,Stats.DEATHS,Stats.WIN,Stats.LOSE,Stats.SHEEPWARS_KILLED_SHEEPS}),
Masterbuilders(true,"Master Builders","MB",ServerType.GAME,new Stats[]{Stats.LOSE,Stats.WIN}),
NONE(true,"NONE","FAIL",ServerType.GAME,null);

@Getter
private String typ;
@Getter
private String Kürzel;
@Getter
private Stats[] stats;
@Getter
private ServerType serverType;
@Getter
private boolean solo=true;

private GameType(boolean solo,String Typ,String Kürzel,ServerType serverType,Stats[] stats){
	this.typ=Typ;
	this.solo=solo;
	this.stats=stats;
	this.Kürzel=Kürzel;
	this.serverType=serverType;
}

public static GameType get(String g){
	g=g.replaceAll(" ", "");
	for(GameType t : GameType.values()){
		if(t.getTyp().replaceAll(" ", "").equalsIgnoreCase(g))return t;
	}
	return null;
}

}
