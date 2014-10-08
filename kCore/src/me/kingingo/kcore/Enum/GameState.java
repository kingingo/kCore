package me.kingingo.kcore.Enum;

public enum GameState {
Laden("Laden"),
LobbyPhase("LobbyPhase"),
SchutzModus("SchutzModus"),
DeathMatch("DeathMatch"),
StartGame("StartGame"),
InGame("InGame"),
Restart("Restart"),
Beenden("Beenden"),
NONE("NONE");

String state;
private GameState(String state){
	this.state=state;
}

public String string(){
	return state;
}
	
}