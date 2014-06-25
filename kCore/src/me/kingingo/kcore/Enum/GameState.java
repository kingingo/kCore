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

//public static GameState getState(String state){
//	switch(state){
//	case "Laden":return GameState.Laden;
//	case "LobbyPhase":return GameState.LobbyPhase;
//	case "inGame":return GameState.InGame;
//	case "Restart":return GameState.Restart;
//	case "Beenden":return GameState.Beenden;
//	case "NONE":return GameState.NONE;
//	}
//	return GameState.NONE;
//}

String state;
private GameState(String state){
	this.state=state;
}

public String string(){
	return state;
}
	
}