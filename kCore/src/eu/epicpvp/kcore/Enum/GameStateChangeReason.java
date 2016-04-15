package eu.epicpvp.kcore.Enum;

import lombok.Getter;

public enum GameStateChangeReason {
GAME_END("Game Endet"),
LAST_PLAYER("Es ist nur noch ein Spieler Online!"),
CUSTOM("Plugin"),
CHANGE_TYPE("Server Game Type §ndert sich!"),
LAST_TEAM("Es ist nun noch ein Team da!");

@Getter
String name="";
private GameStateChangeReason(String name){
	this.name=name;
}

}
