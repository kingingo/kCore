package me.kingingo.kcore.PlayerStats;

import lombok.Getter;

public enum Stats {
KILLS("kills int","kills","Kills",true),
DEATHS("deaths int","deaths","Tode",true),
WIN("win int","win","Wins",true),
LOSE("lose int","lose","Lose",true),
RANKING("rank int","rank","Rang",false),

RUSH_ZERSTOERTE_BLOECKE("bloecke int","bloecke","Bloecke",true),

SHEEPWARS_KILLED_SHEEPS("sheeps int","sheeps","Sheeps",true),

TTT_TESTS("tests int","tests","Tests",true),
TTT_PÄSSE("paesse int","paesse","Paesse",true),
TTT_KARMA("karma int","karma","Karma",true),
TTT_DETECTIVE_PUNKTE("detectivepunkte int","detectivepunkte","Detective-Punkte",true),
TTT_TRAITOR_PUNKTE("traitorpunkte int","traitorpunkte","Traitor-Punkte",true);

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
