package eu.epicpvp.kcore.TreasureChest.NEW;

import lombok.Getter;

public enum TreasureChestState {
BUILDING(0),
CHOOSE(1),
DELETE(2);

@Getter
private int id;
	
private TreasureChestState(int id){
	this.id=id;
}
	
}
