package me.kingingo.kcore.Permission;

public enum kPermission {
KBAN("epicpvp.bg.kban"),
ZEITBAN("epicpvp.bg.zeitban"),
ZEITBAN_MAX_14TAGE("epicpvp.bg.zeitban.14tage"),
ZEITBAN_MAX_7TAGE("epicpvp.bg.zeitban.7tage"),
KICKEN("epicpvp.bg.kicken"),
CLEAR_CHAT("epicpvp.bg.cc"),
SERVER("epicpvp.bg.server"),
DEL_SERVER("epicpvp.bg.delserver"),
ADD_SERVER("epicpvp.bg.addserver"),
TEAM_MESSAGE("epicpvp.bg.team_message"),
WHERE_IS("epicpvp.bg.whereis"),
KINFO("epicpvp.bg.kinfo"),
RESTART("epicpvp.bg.restart"),
BROADCAST("epicpvp.bg.broadcast"),
MOTD("epicpvp.bg.motd"),
PREMIUM_TOGGLE("epicpvp.bg.premium"),
UNBAN("epicpvp.bg.unkban"),	
LAG("epicpvp.bg.lag"),
CLIENT("epicpvp.bg.client"),
TICKET_ADMIN("epicpvp.bg.ticket"),
SETGROUP("epicpvp.bg.setgroup"),
PERMISSION("epicpvp.bg.permission"),
BUILD_SERVER("epicpvp.bg.build.server"),
BAN_LVL_1("epicpvp.bg.kban.lvl.one"),
BAN_LVL_2("epicpvp.bg.kban.lvl.two"),
BAN_LVL_3("epicpvp.bg.kban.lvl.three"),
BAN_LVL_4("epicpvp.bg.kban.lvl.four"),
BAN_LVL_5("epicpvp.bg.kban.lvl.five"),
JOIN_FULL_SERVER("epicpvp.join_full_server"),
ALL_PERMISSION("epicpvp.*"),
START_SERVER("epicpvp.start_server"),
START_SERVER_SET_TIME("epicpvp.start_server.settime"),
NONE("FAIL"),
SERVER_JOIN_SPECTATE("epicpvp.server.spectate"),
PREMIUM_LOBBY("lobby.premiumbereich"),
COMMAND_MUTE_ALL("epicpvp.mute.all"),
COMMAND_MUTE_ALL_ALLOW("epicpvp.mute.all.chat"),
ADMIN_SERVICE("epicpvp.service"),
NICK_SEE("epicpvp.nick.see"),
NICK_RANDOM("epicpvp.nick.random"),
NICK_SET("epicpvp.nick.set"),
CHAT_LINK("epicpvp.chatlink"),
COMMAND_MEM("epicpvp.command.mem"),
COMMAND_TOGGLE("epicpvp.command.toggle"),
COMMAND_GIVE_ALL("epicpvp.command.giveall"),
PVP_MUTE_ALL("epicpvp.pvpmuteall"),
COMMAND_COMMAND_MUTE_ALL("epicpvp.command.commandmuteall"),
COMMAND_COMMAND_MUTE_ALL_ALLOW("epicpvp.command.commandmuteall.allow"),
kFLY("epicpvp.kfly"),
REPAIR("epicpvp.repair"),
INVSEE("epicpvp.inventory.see"),
INVSEE_CLICK("epicpvp.inventory.see.click"),
REPAIR_ALL("epicpvp.repair.all"),
REPAIR_BODY("epicpvp.repair.body"),
REPAIR_HAND("epicpvp.repair.hand"),
HEAL("epicpvp.heal"),
HEAL_ALL("epicpvp.heal.all"),
HEAL_OTHER("epicpvp.heal.other"),
VANISH("epicpvp.vanish"),
GILDE_NEWISLAND("epicpvp.gilde.newisland"),
GILDE_HOME_OTHER("epicpvp.gilde.home.other"),
MORE("epicpvp.more"),
MORE_ALL("epicpvp.more.all"),
FEED("epicpvp.feed"),
FEED_ALL("epicpvp.feed.all"),
FLYSPEED("epicpvp.fly.speed"),
ENDERCHEST("epicpvp.enderchest.open"),
ENDERCHEST_CLICK("epicpvp.enderchest.open.click"),
ENDERCHEST_OTHER("epicpvp.enderchest.open.other"),
ENDERPEARL("kpvp.enderpearl"),
ENDERCHEST_USE("epicpvp.enderchest.use"),
ENDERCHEST_ADDON("epicpvp.enderchest.addon"),
CLEARINVENTORY("epicpvp.clearinventory"),
CLEARINVENTORY_OTHER("epicpvp.clearinventory.other"),
CLEARINVENTORY_ALL("epicpvp.clearinventory.all"),
FEED_OTHER("epicpvp.feed.other"),
CHAT_NERV("epicpvp.chat.nerv_format"),
SOCIAL_SPY("epicpvp.chat.socialspy"),
CHAT_FARBIG("epicpvp.chat.farbig"),
RENAMEITEM("epicpvp.renameitem"),
TAG("epicpvp.time.day"),
NACHT("epicpvp.time.nacht"),
SUN("epicpvp.time.sun"),
HOME("epicpvp.home.use"),
HOME_BYEPASS_DELAY("epicpvp.home.byepass.delay"),
HOME_SET("epicpvp.home.set"),
HOME_DEL("epicpvp.home.del"),
KIT("epicpvp.kit.use"),
KIT_SET("epicpvp.kit.set"),
KIT_BYEPASS_DELAY("epicpvp.kit.byepass.delay"),
KSPAWN("kpvp.kspawn"),
SPAWNMOB("epicpvp.spawnmob"),
SPAWNMOB_ALL("epicpvp.spawnmob.*"),
SPAWNER("epicpvp.spawner"),
SPAWNER_ALL("epicpvp.spawner.*"),
SPAWN_IGNORE_DELAY("epicpvp.spawn.ignore_delay"),
SPAWN_SET("epicpvp.spawn.set"),

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
PERK_APPLE("epicpvp.perk.apple"),
PERK_WATER_DAMAGE("epicpvp.perk.waterdamage"),
PERK_INSTANT_TNT("epicpvp.perk.instanttnt"),
PERK_HAT("epicpvp.perk.hat"),
PERK_GOLENAPPLE("epicpvp.perk.goldenapple"),
PERK_ARROW_POTIONEFFECT("epicpvp.perk.arrowpotioneffect"),
PERK_ALL("epicpvp.perk.*"),

PLAYER_TELEPORT_A("epicpvp.player.tpa"),
PLAYER_TELEPORT_AHERE("epicpvp.player.tpahere"),
PLAYER_TELEPORT("epicpvp.player.tp"),
PLAYER_TELEPORT_HERE("epicpvp.player.tphere"),
PLAYER_TELEPORT_ACCEPT("epicpvp.player.tpaccept"),
WARP_SET("epicpvp.home.set"),
WARP("epicpvp.warp"),
WARP_BYEPASS_DELAY("epicpvp.warp.byepass.delay"),
WARP_LIST("epicpvp.warp.list"),
//TS
TEAMSPEAK_QUERY("epicpvp.ts.query"),
TEAMSPEAK_OWNER("epicpvp.ts.owner"),
TEAMSPEAK_DEV_OWNER("epicpvp.ts.dev_owner"),
TEAMSPEAK_ADMIN("epicpvp.ts.admin"),
TEAMSPEAK_PVP_ADMIN("epicpvp.ts.pvpadmin"),
TEAMSPEAK_TEAM_LEITER("epicpvp.ts.team_leiter"),
TEAMSPEAK_SMOD("epicpvp.ts.smod"),
TEAMSPEAK_MOD("epicpvp.ts.mod"),
TEAMSPEAK_FOURM_MOD("epicpvp.ts.forummod"),
TEAMSPEAK_ARCHITEKT_LEITER("epicpvp.ts.architekt-leiter"),
TEAMSPEAK_ARCHITEKT("epicpvp.ts.architekt"),
TEAMSPEAK_HELFER("epicpvp.ts.helfer"),
TEAMSPEAK_PROBE_ARCHITEKT("epicpvp.ts.probe_architekt"),
TEAMSPEAK_MITGLIED("epicpvp.ts.mitglied"),
TEAMSPEAK_PREMIUM("epicpvp.ts.premium"),
TEAMSPEAK_YOUTUBER("epicpvp.ts.youtuber"),
TEAMSPEAK_TEAM_FREUND("epicpvp.ts.team_freund"),
TEAMSPEAK_GEBURTSTAG("epicpvp.ts.geburtstag"),
TEAMSPEAK_BOT("epicpvp.ts.bot"),
TEAMSPEAK_NICHT_ANSTUPSBAR("epicpvp.ts.nicht_anstupsbar"),
TEAMSPEAK_GARNICHT_ANSTUPSBAR("epicpvp.ts.garnicht_anstupsbar"),
TEAMSPEAK_NICHT_REDEN("epicpvp.ts.nicht_reden"),
TEAMSPEAK_NICHT_BEWEGEN("epicpvp.ts.nicht_bewegen"),
TEAMSPEAK_NICHT_ANSTUPSBART("epicpvp.ts.nicht_anstupsbart"),
TEAMSPEAK_NICHT_ANSCHREIBBAR("epicpvp.ts.nicht_anschreibbar"),
TEAMSPEAK_VERIFIZIERT("epicpvp.ts.verifiziert"),
TEAMSPEAK_AUFNAHME("epicpvp.ts.aufnahme"),
//TS
//Kits
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
SHEEPWARS_KIT_SUPERMAN("epicpvp.kit.superman"),
SHEEPWARS_KIT_BOGENMEISTER("epicpvp.kit.bogenmeister"),
SHEEPWARS_KIT_IRONGOLEM("epicpvp.kit.irongolem"),
SHEEPWARS_KIT_PIGZOMBIE("epicpvp.kit.pigzombie"),
SHEEPWARS_KIT_CREEPER("epicpvp.kit.creeper"),
SHEEPWARS_KIT_ZOMBIE("epicpvp.kit.zombie"),
ALL_KITS("epicpvp.kit.*"),
//Kits

SKYWARS_KIT_J�GER("epicpvp.skywars.kit.j�ger"),
SKYWARS_KIT_ENCHANTER("epicpvp.skywars.kit.enchanter"),
SKYWARS_KIT_HEILER("epicpvp.skywars.kit.heiler"),
SKYWARS_KIT_SP�HER("epicpvp.skywars.kit.sp�her"),
SKYWARS_KIT_KAMPFMEISTER("epicpvp.skywars.kit.kampfmeister"),
SKYWARS_KIT_RITTER("epicpvp.skywars.kit.ritter"),
SKYWARS_KIT_FEUERMEISTER("epicpvp.skywars.kit.feuermeister"),
SKYWARS_KIT_DROIDE("epicpvp.skywars.kit.droide"),
SKYWARS_KIT_STO�ER("epicpvp.skywars.kit.sto�er"),

//SkyPvP - START -
SkyPvP_Mehr_Leben("epicpvp.skypvp.more_life"),
//SkyPvP - ENDE -
//One In The Chamber - START -
OneInTheChamber_KIT("epicpvp.oitc.kit"),
OneInTheChamber_LIFE("epicpvp.oitc.life"),
//One In The Chamber - ENDE -
SKYBLOCK_GILDEN_ISLAND("epicpvp.skyblock.gilden.island"),
SKYBLOCK_PREMIUM_ISLAND("epicpvp.skyblock.schematic.premium"),
SKYBLOCK_NORMAL_ISLAND("epicpvp.skyblock.schematic.normal"),
SKYBLOCK_HOME_OTHER("epicpvp.skyblock.home.other"),
SKYBLOCK_ISLAND_BYPASS("epicpvp.skyblock.bypass"),

//PETS
PET_ALL("kpet.*"),
PET_BLAZE("kpet.blaze"),
PET_CHICKEN("kpet.chicken"),
PET_COW("kpet.cow"),
PET_ENDERMAN("kpet.enderman"),
PET_SNOWMAN("kpet.snowman"),
PET_CREEPER("kpet.creeper"),
PET_IRON_GOLEM("kpet.irongolem"),
PET_PIG("kpet.pig"),
PET_PLAYER("kpet.player"),
PET_SPIDER("kpet.spider"),
PET_PIGZOMBIE("kpet.pigzombie"),
PET_WITHERSKULL("kpet.witherskull"),
PET_SLIME("kpet.slime"),
PET_WOLF("kpet.wolf"),
PET_ZOMBIE("kpet.zombie"),
PET_SHEEP("kpet.sheep"),
PET_HORSE("kpet.horse"),
PET_RABBIT("kpet.rabbit"),
PET_SQUID("kpet.squid"),
PET_GUARDIAN("kpet.guardian"),
PET_OCELOT("kpet.ocelot"),
//PETS

//DISGUISE
DISGUISE_ALL("kdisguise.*"),
DISGUISE_BLAZE("kdisguise.blaze"),
DISGUISE_CHICKEN("kdisguise.chicken"),
DISGUISE_COW("kdisguise.cow"),
DISGUISE_ENDERMAN("kdisguise.enderman"),
DISGUISE_SNOWMAN("kdisguise.snowman"),
DISGUISE_CREEPER("kdisguise.creeper"),
DISGUISE_IRON_GOLEM("kdisguise.irongolem"),
DISGUISE_PIG("kdisguise.pig"),
DISGUISE_PLAYER("kdisguise.player"),
DISGUISE_SPIDER("kdisguise.spider"),
DISGUISE_PIGZOMBIE("kdisguise.pigzombie"),
DISGUISE_WITHERSKULL("kdisguise.witherskull"),
DISGUISE_SLIME("kdisguise.slime"),
DISGUISE_WOLF("kdisguise.wolf"),
DISGUISE_ZOMBIE("kdisguise.zombie"),
DISGUISE_SHEEP("kdisguise.sheep"),
DISGUISE_HORSE("kdisguise.horse"),
DISGUISE_RABBIT("kdisguise.rabbit"),
DISGUISE_SQUID("kdisguise.squid"),
DISGUISE_GUARDIAN("kdisguise.guardian"),
DISGUISE_OCELOT("kdisguise.ocelot");
//DISGUISE

public static kPermission isPerm(String perm){
	kPermission per=kPermission.NONE;
	for(kPermission permission : kPermission.values()){
		if(permission.getPermissionToString().equalsIgnoreCase(perm)){
			per=permission;
			break;
		}
	}
	return per;
}

private String perm;
private kPermission(String perm){
	this.perm=perm;
}

public String getPermissionToString(){
	return this.perm;
}

}
