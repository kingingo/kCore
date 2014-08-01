package me.kingingo.kcore.Permission;

public enum Permission {
JOIN_FULL_SERVER("epicpvp.join_full_server"),
ALL_PERMISSION("epicpvp.*"),
NONE("FAIL"),
SERVER_JOIN_SPECTATE("epicpvp.server.spectate"),
PREMIUM_LOBBY("lobby.premiumbereich"), 

//Kits
SHEEPRUSH_KIT_STARTER("epicpvp.kit.starter"),
SHEEPRUSH_KIT_ARROWMAN("epicpvp.kit.arrowman"),
SHEEPRUSH_KIT_ITEMSTEALER("epicpvp.kit.itemstealer"),
SHEEPRUSH_KIT_HEALER("epicpvp.kit.healer"),
SHEEPRUSH_KIT_DROPPER("epicpvp.kit.dropper"),
SHEEPRUSH_KIT_ANKER("epicpvp.kit.anker"),
SHEEPRUSH_KIT_PERKER("epicpvp.kit.perker"),
SHEEPRUSH_KIT_TNTER("epicpvp.kit.tnter"),
SHEEPRUSH_KIT_BUFFER("epicpvp.kit.buffer"),
SHEEPRUSH_KIT_KNIGHT("epicpvp.kit.knight"),
SHEEPRUSH_KIT_THEDEATH("epicpvp.kit.thedeath"),
SHEEPRUSH_KIT_SPRINGER("epicpvp.kit.springer"),

ALL_KITS("epicpvp.kit.*"),
//Kits

//One In The Chamber - START -
OneInTheChamber_KIT("epicpvp.oitc.kit"),
OneInTheChamber_LIFE("epicpvp.oitc.life"),
//One In The Chamber - ENDE -

//PETS
PET_BLAZE("kpet.blaze"),
PET_CHICKEN("kpet.chicken"),
PET_COW("kpet.cow"),
PET_ENDERMAN("kpet.enderman"),
PET_IRON_GOLEM("kpet.irongolem"),
PET_PIG("kpet.pig"),
PET_SPIDER("kpet.spider"),
PET_WOLF("kpet.wolf"),
PET_ZOMBIE("kpet.zombie"),
PET_SHEEP("kpet.sheep");

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
