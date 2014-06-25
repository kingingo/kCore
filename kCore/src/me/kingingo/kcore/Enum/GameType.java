package me.kingingo.kcore.Enum;

public enum GameType {
SurvivalGames("SurvivalGames","SG"),
OneInTheChamber("OneInTheChamber","OITC"),
SkyPvP("SkyPvP","SK"),
Falldown("Falldown","FD"),
Rush("Rush","RUSH"),
TroubleMine("TroubleMine","TIMV"),
EnderMode("EnderMode","EM"),
MegaRush("MegaRush","MR"),
BeastMode("BeastMode","BM"),
MarioParty("MarioParty","MP"),
NONE("NONE","FAIL");

private String typ;
private String Kürzel;
private GameType(String Typ,String Kürzel){
	this.typ=Typ;
	this.Kürzel=Kürzel;
}

public String getKürzel(){
	return this.Kürzel;
}

public String string(){
	return typ;
}

}
