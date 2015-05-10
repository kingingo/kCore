package me.kingingo.kcore.Enum;

public enum GameState {
Laden("Laden"),
LobbyPhase("LobbyPhase"),
SchutzModus("SchutzModus"),
DeathMatch("DeathMatch"),
StartDeathMatch("StartDeathMatch"),
StartGame("StartGame"),
InGame("InGame"),
Restart("Restart"),
MultiGame("MultiGame"),
NONE("NONE");

String state;
private GameState(String state){
	this.state=state;
}

public String string(){
	return state;
}
	
}