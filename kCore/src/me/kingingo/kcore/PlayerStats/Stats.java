package me.kingingo.kcore.PlayerStats;

import lombok.Getter;

public enum Stats {
KILLS("kills int","kills","Kills",true),
DEATHS("deaths int","deaths","Tode",true),
WIN("win int","win","Wins",true),
LOSE("lose int","lose","Lose",true),
RANKING("rank int","rank","Rang",false),

RUSH_ZERSTOERTE_BLOECKE("bloecke int","bloecke","Bloecke",true),

TM_TESTS("tests int","tests","Tests",true),
TM_PÄSSE("paesse int","paesse","Paesse",true),
TM_KARMA("karma int","karma","Karma",true);

@Getter
String CREATE;
@Getter
String TYP;
@Getter
String KÜRZEL;
@Getter
boolean mysql;

private Stats(String CREATE,String TYP,String KÜRZEL,boolean mysql){
	this.CREATE=CREATE;
	this.TYP=TYP;
	this.KÜRZEL=KÜRZEL;
	this.mysql=mysql;
}

}
