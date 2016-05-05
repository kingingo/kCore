package eu.epicpvp.kcore.MysteryChest;

import lombok.Getter;

public enum MysteryChestState {
BUILDING(0),
CHOOSE(1),
DELETE(2);

@Getter
private int id;
	
private MysteryChestState(int id){
	this.id=id;
}
	
}
