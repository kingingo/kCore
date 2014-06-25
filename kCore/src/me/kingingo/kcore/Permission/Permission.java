package me.kingingo.kcore.Permission;

public enum Permission {
JOIN_FULL_SERVER("epicpvp.join_full_server"),
ALL_PERMISSION("epicpvp.*"),
NONE("FAIL"),
PREMIUM_LOBBY("lobby.premiumbereich"), 

//One In The Chamber - START -
OneInTheChamber_KIT("epicpvp.oitc.kit"),
OneInTheChamber_LIFE("epicpvp.oitc.life"),
//One In The Chamber - ENDE -

//PETS
PET_BLAZE("pet.blaze"),
PET_CHICKEN("pet.chicken"),
PET_COW("pet.cow"),
PET_ENDERMAN("pet.enderman"),
PET_IRON_GOLEM("pet.iron_golem"),
PET_PIG("pet.pig"),
PET_SPIDER("pet.spider"),
PET_WOLF("pet.wolf"),
PET_ZOMBIE("pet.zombie"),
PET_SHEEP("pet.sheep");

//PETS

public static Permission isPerm(String perm){
	Permission per=Permission.NONE;
	switch(perm){
	case "epicpvp.oitc.kit": per=Permission.OneInTheChamber_KIT; break;
	case "epicpvp.oitc.life": per=Permission.OneInTheChamber_LIFE; break;
	case "epicpvp.join_full_server": per=Permission.JOIN_FULL_SERVER; break;
	case "epicpvp.*": per=Permission.ALL_PERMISSION; break;
	}
	return per;
}

private String perm;
private Permission(String perm){
	this.perm=perm;
}

public String getPermissionToString(){
	return this.perm;
}

}
