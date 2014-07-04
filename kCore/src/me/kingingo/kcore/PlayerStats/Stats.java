package me.kingingo.kcore.PlayerStats;

import lombok.Getter;

public enum Stats {
KILLS("kills int","kills",true),
DEATHS("deaths int","deaths",true),
WIN("win int","win",true),
LOSE("lose int","lose",true),
RANKING("rank int","rank",false),

RUSH_ZERSTOERTE_BLOECKE("bloecke int","bloecke",true),

TM_TESTS("tests int","tests",true),
TM_PÄSSE("paesse int","paesse",true),
TM_KARMA("karma int","karma",true);

@Getter
String CREATE;
@Getter
String TYP;
@Getter
boolean mysql;

private Stats(String CREATE,String TYP,boolean mysql){
	this.CREATE=CREATE;
	this.TYP=TYP;
	this.mysql=mysql;
}

}
