package me.kingingo.kcore.StatsManager;

import lombok.Getter;

public enum Stats {
INVENTORY("inventory varchar(1000)","inventory","Inventory",true),
TIME("time varchar(50)","time","Time",true),
TIME_ELO("time_elo double","time_elo","Elo",true),
ELO("elo double","elo","Elo",true),
KIT_ID("kit_id int","kit_id","Kit-ID",true),
KIT_RANDOM("kit_random varchar(30)","kit_random","Kit-Egal",true),
KIT("kit varchar(1000)","kit","Kit",true),
TEAM_MIN("team_min int","team_min","TeamMin",true),
TEAM_MAX("team_max int","team_max","TeamMax",true),
WORLD("world varchar(30)","world","World",true),
LOC_X("x double","x","X",true),
LOC_Y("y double","y","Y",true),
LOC_Z("z double","z","Z",true),
MONEY("money double","money","Money",true),
FAME_KILLS("fkills int","fkills","Fame-Kills",true),
FAME_DEATHS("fdeaths int","fdeaths","Fame-Tode",true),
MONSTER_KILLS("monsterkills int","monsterkills","MonsterKills",true),
MONSTER_DEATHS("monsterdeaths int","monsterdeaths","MonsterDeaths",true),
ANIMAL_KILLS("animalkills int","animalkills","AnimalKills",true),
ANIMAL_DEATHS("animaldeaths int","animaldeaths","AnimalDeaths",true),
LEVEL("level int","level","Level",true),
KILLS("kills int","kills","Kills",true),
DEATHS("deaths int","deaths","Tode",true),
WIN("win int","win","Wins",true),
LOSE("lose int","lose","Lose",true),
RANKING("rank int","rank","Rang",false),
BEDWARS_ZERSTOERTE_BEDs("beds int","beds","Beds",true),
SHEEPWARS_KILLED_SHEEPS("sheeps int","sheeps","Sheeps",true),
POWER("power int","power","Power",true),
TTT_TESTS("tests int","tests","Tests",true),
TTT_PÄSSE("paesse int","paesse","Paesse",true),
TTT_KARMA("karma int","karma","Karma",true),
TTT_DETECTIVE_PUNKTE("detectivepunkte int","detectivepunkte","Detective-Punkte",true),
TTT_TRAITOR_PUNKTE("traitorpunkte int","traitorpunkte","Traitor-Punkte",true);

@Getter
private String CREATE;
@Getter
private String TYP;
@Getter
private String KÜRZEL;
@Getter
private boolean mysql;

private Stats(String CREATE,String TYP,String KÜRZEL,boolean mysql){
	this.CREATE=CREATE;
	this.TYP=TYP;
	this.KÜRZEL=KÜRZEL;
	this.mysql=mysql;
}

}
