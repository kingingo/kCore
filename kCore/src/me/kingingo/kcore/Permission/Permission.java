package me.kingingo.kcore.Permission;

public enum Permission {
JOIN_FULL_SERVER("epicpvp.join_full_server"),
ALL_PERMISSION("epicpvp.*"),
START_SERVER("epicpvp.start_server"),
NONE("FAIL"),
SERVER_JOIN_SPECTATE("epicpvp.server.spectate"),
PREMIUM_LOBBY("lobby.premiumbereich"),
MUTE_ALL("epicpvp.mute.all"),
MUTE_ALL_CHAT("epicpvp.mute.all.chat"),
ADMIN_SERVICE("epicpvp.service"),
NICK_RANDOM("epicpvp.nick.random"),
NICK_SET("epicpvp.nick.set"),
CHAT_LINK("epicpvp.chatlink"),
COMMAND_MEM("epicpvp.command.mem"),

PERK_NO_HUNGER("epicpvp.perk.nohunger"),
PERK_JUMP("epicpvp.perk.jump"),
PERK_NO_FIRE("epicpvp.perk.nofire"),
PERK_RUNNER("epicpvp.perk.runner"),
PERK_DOUBLE_XP("epicpvp.perk.doublexp"),
PERK_NO_REPAIR("epicpvp.perk.norepair"),
PERK_GET_XP("epicpvp.perk.getxp"),
PERK_ITEM_NAME("epicpvp.perk.itename"),
PERK_MINER("epicpvp.perk.miner"),
PERK_DROPPER("epicpvp.perk.dropper"),
PERK_HEALER("epicpvp.perk.healer"),
PERK_GOLENAPPLE("epicpvp.perk.goldenapple"),

DEATHGAMES_KIT_ANKER("epicpvp.kit.dg.anker"),
DEATHGAMES_KIT_BOMBER("epicpvp.kit.dg.bomber"),
DEATHGAMES_KIT_FIREMAN("epicpvp.kit.dg.fireman"),
DEATHGAMES_KIT_HOLZ("epicpvp.kit.dg.holz"),
DEATHGAMES_KIT_PANZER("epicpvp.kit.dg.panzer"),
DEATHGAMES_KIT_SKORPION("epicpvp.kit.dg.skorpion"),
DEATHGAMES_KIT_VERSORGER("epicpvp.kit.dg.versorger"),
DEATHGAMES_KIT_VAMPIRE("epicpvp.kit.dg.vampire"),
DEATHGAMES_KIT_ANGLE("epicpvp.kit.dg.angle"),
DEATHGAMES_KIT_ENTERHARKEN("epicpvp.kit.dg.enterharken"),
DEATHGAMES_KIT_JUMPER("epicpvp.kit.dg.jumper"),
DEATHGAMES_KIT_RUNNER("epicpvp.kit.dg.runner"),
DEATHGAMES_KIT_HAI("epicpvp.kit.dg.hai"),
DEATHGAMES_KIT_SCHILDKROETE("epicpvp.kit.dg.schildkroete"),
DEATHGAMES_KIT_RITTER("epicpvp.kit.dg.ritter"),
DEATHGAMES_KIT_SUPERMAN("epicpvp.kit.dg.superman"),
DEATHGAMES_KIT_TELEPORTER("epicpvp.kit.dg.teleporter"),
DEATHGAMES_KIT_SWITCHER("epicpvp.kit.dg.switcher"),

//Kits
ADMIN_KIT("epicpvp.admin.kit"),
SHEEPWARS_KIT_OLD_RUSH("epicpvp.kit.old_rush"),
SHEEPWARS_KIT_STARTER("epicpvp.kit.starter"),
SHEEPWARS_KIT_ARROWMAN("epicpvp.kit.arrowman"),
SHEEPWARS_KIT_ITEMSTEALER("epicpvp.kit.itemstealer"),
SHEEPWARS_KIT_HEALER("epicpvp.kit.healer"),
SHEEPWARS_KIT_DROPPER("epicpvp.kit.dropper"),
SHEEPWARS_KIT_ANKER("epicpvp.kit.anker"),
SHEEPWARS_KIT_PERKER("epicpvp.kit.perker"),
SHEEPWARS_KIT_TNTER("epicpvp.kit.tnter"),
SHEEPWARS_KIT_BUFFER("epicpvp.kit.buffer"),
SHEEPWARS_KIT_KNIGHT("epicpvp.kit.knight"),
SHEEPWARS_KIT_THEDEATH("epicpvp.kit.thedeath"),
SHEEPWARS_KIT_SPRINGER("epicpvp.kit.springer"),

SHEEPWARS_KIT_IRONGOLEM("epicpvp.kit.irongolem"),
SHEEPWARS_KIT_PIGZOMBIE("epicpvp.kit.pigzombie"),
SHEEPWARS_KIT_CREEPER("epicpvp.kit.creeper"),
SHEEPWARS_KIT_ZOMBIE("epicpvp.kit.zombie"),

ALL_KITS("epicpvp.kit.*"),
//Kits

//SkyPvP - START -
SkyPvP_Mehr_Leben("epicpvp.skypvp.more_life"),
//SkyPvP - ENDE -

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
	for(Permission permission : Permission.values()){
		if(permission.getPermissionToString().equalsIgnoreCase(perm)){
			per=permission;
			break;
		}
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
