package me.kingingo.kcore.PlayerStats;

import lombok.Getter;

public enum Stats {
KILLS("kills int","kills"),
DEATHS("deaths int","deaths"),
WIN("win int","win"),
LOSE("lose int","lose"),
RANKING("rank int","rank"),

RUSH_ZERSTOERTE_BLOECKE("bloecke int","bloecke"),

TM_TESTS("tests int","tests"),
TM_PÄSSE("paesse int","paesse"),
TM_KARMA("karma int","karma");

@Getter
String CREATE;
@Getter
String TYP;
private Stats(String CREATE,String TYP){
	this.CREATE=CREATE;
	this.TYP=TYP;
}

}
