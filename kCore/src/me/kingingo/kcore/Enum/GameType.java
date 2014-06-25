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
private String K�rzel;
private GameType(String Typ,String K�rzel){
	this.typ=Typ;
	this.K�rzel=K�rzel;
}

public String getK�rzel(){
	return this.K�rzel;
}

public String string(){
	return typ;
}

}
