package eu.epicpvp.kcore.MysteryBox;

import lombok.Getter;

public enum MysteryBoxState {
BUILDING(0),
CHOOSE(1),
DELETE(2);

@Getter
private int id;
	
private MysteryBoxState(int id){
	this.id=id;
}
	
}
