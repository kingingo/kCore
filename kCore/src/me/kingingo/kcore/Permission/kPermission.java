package me.kingingo.kcore.Permission;

public enum kPermission {
COINS_ONE_ADD("epicpvp.coins.one"),
COINS_TWO_ADD("epicpvp.coins.two"),
COINS_THREE_ADD("epicpvp.coins.three"),
HUB_CHANGE_LANGUAGE("epicpvp.language.change"),
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
MONITOR("epicpvp.lag.monitor"),
VERSUS_SLOTS("epicpvp.vs.slots"),
WHERE_IS("epicpvp.bg.whereis"),
KINFO("epicpvp.bg.kinfo"),
RESTART("epicpvp.bg.restart"),
BROADCAST("epicpvp.bg.broadcast"),
MOTD("epicpvp.bg.motd"),
PREMIUM_TOGGLE("epicpvp.bg.premium"),
UNBAN("epicpvp.bg.unkban"),	
POTION_AMOUNT("epicpvp.potion.amount"),
POTION("epicpvp.potion.use"),
POTION_ALL("epicpvp.potion.*"),
BACK("epicpvp.back.use"),
LAG("epicpvp.bg.lag"),
WORKBENCH("epicpvp.workbench.use"),
ENCHANTMENT_TABLE("epicpvp.enchantmenttable.use"),
AMBOSS("epicpvp.amboss.use"),
REMOVE_ENCHANTMENT("epicpvp.removeenchantment.use"),
EXT("epicpvp.ext.use"),
FILL("epicpvp.cmd.fill"),
SUFFIX("epicpvp.suffix.use"),
NEAR("epicpvp.near.use"),
NEAR_IGNORE("epicpvp.near.ignore"),
HEAD("epicpvp.head.use"),
EXT_ALL("epicpvp.ext.all"),
EXT_OTHER("epicpvp.ext.other"),
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
SHOP_SIGN_CREATE("epicpvp.shop.sign.create"),
SHOP_SIGN_CREATE_MSG("epicpvp.shop.sign.create.msg"),
SHOP_SIGN_CREATE_BYPASS("epicpvp.shop.sign.create.bypass"),
JOIN_FULL_SERVER("epicpvp.join_full_server"),
ALL_PERMISSION("epicpvp.*"),
START_SERVER("epicpvp.start_server"),
START_SERVER_SET_TIME("epicpvp.start_server.settime"),
SERVER_JOIN_SPECTATE("epicpvp.server.spectate"),
PREMIUM_LOBBY("epicpvp.bg.hub.premium"),
FLY_LOBBY("epicpvp.hub.kfly"),
COMMAND_MUTE_ALL("epicpvp.mute.all"),
COMMAND_MUTE_ALL_ALLOW("epicpvp.mute.all.chat"),
ADMIN_SERVICE("epicpvp.service"),
NICK_SEE("epicpvp.bg.nick.see"),
NICK_RANDOM("epicpvp.bg.nick.random"),
NICK_SET("epicpvp.bg.nick.set"),
CHAT_LINK("epicpvp.chatlink"),
COMMAND_GIVE("epicpvp.give.use"),
COMMAND_MEM("epicpvp.command.mem"),
COMMAND_TOGGLE("epicpvp.command.toggle"),
COMMAND_GIVE_ALL("epicpvp.command.giveall"),
PVP_MUTE_ALL("epicpvp.pvpmuteall"),
COMMAND_COMMAND_MUTE_ALL("epicpvp.command.commandmuteall"),
COMMAND_COMMAND_MUTE_ALL_ALLOW("epicpvp.command.commandmuteall.allow"),
kFLY("epicpvp.kfly"),
REPAIR("epicpvp.repair"),
REPAIR_ALL_PLAYERS("epicpvp.repair.all.players"),
REPAIR_ALL("epicpvp.repair.all"),
REPAIR_BODY("epicpvp.repair.body"),
REPAIR_HAND("epicpvp.repair.hand"),
INVSEE("epicpvp.inventory.see"),
INVSEE_BODY("epicpvp.inventory.see.body"),
INVSEE_CLICK("epicpvp.inventory.see.click"),
HEAL("epicpvp.heal"),
HEAL_ALL("epicpvp.heal.all"),
HEAL_OTHER("epicpvp.heal.other"),
HOME_ADMIN("epicpvp.home.admin"),
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
ENDERPEARL_TIME_BYPASS("kpvp.enderpearl.time.bypass"),
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
KIT_RESET("epicpvp.kit.reset"),
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
PLAYER_TELEPORT_A_BYPASS("epicpvp.player.tpa.bypass"),
PLAYER_TELEPORT_AHERE_BYPASS("epicpvp.player.tpahere.bypass"),
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
ADMIN_KIT("epicpvp.admin.kit"),
//DEATHGAMES Kits
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
//DEATHGAMES Kits

//SHEEPWARS Kits
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
SHEEPWARS_KIT_VIP("epicpvp.kit.vip"),
SHEEPWARS_KIT_ULTRA("epicpvp.kit.ultra"),
SHEEPWARS_KIT_LEGEND("epicpvp.kit.legend"),
SHEEPWARS_KIT_MVP("epicpvp.kit.mvp"),
SHEEPWARS_KIT_MVPPLUS("epicpvp.kit.mvpplus"),
ALL_KITS("epicpvp.kit.*"),
//SHEEPWARS Kits

//SHEEPWARS Kits
SKYWARS_KIT_HULK("epicpvp.skywars.kit.hulk"),
SKYWARS_KIT_MARIO("epicpvp.skywars.kit.mario"),
SKYWARS_KIT_STARTERKIT("epicpvp.skywars.kit.starter"),
SKYWARS_KIT_STUNTMAN("epicpvp.skywars.kit.stuntman"),
SKYWARS_KIT_PANZER("epicpvp.skywars.kit.panzer"),
SKYWARS_KIT_SLIME("epicpvp.skywars.kit.slime"),
SKYWARS_KIT_SPRENGMEISTER("epicpvp.skywars.kit.sprengmeister"),
SKYWARS_KIT_KOCH("epicpvp.skywars.kit.koch"),
SKYWARS_KIT_FISCHER("epicpvp.skywars.kit.fischer"),
SKYWARS_KIT_FARMER("epicpvp.skywars.kit.farmer"),
SKYWARS_KIT_J�GER("epicpvp.skywars.kit.j�ger"),
SKYWARS_KIT_ENCHANTER("epicpvp.skywars.kit.enchanter"),
SKYWARS_KIT_HEILER("epicpvp.skywars.kit.heiler"),
SKYWARS_KIT_SP�HER("epicpvp.skywars.kit.sp�her"),
SKYWARS_KIT_KAMPFMEISTER("epicpvp.skywars.kit.kampfmeister"),
SKYWARS_KIT_RITTER("epicpvp.skywars.kit.ritter"),
SKYWARS_KIT_FEUERMEISTER("epicpvp.skywars.kit.feuermeister"),
SKYWARS_KIT_DROIDE("epicpvp.skywars.kit.droide"),
SKYWARS_KIT_STO�ER("epicpvp.skywars.kit.sto�er"),
SKYWARS_KIT_HASE("epicpvp.skywars.kit.hase"),
SKYWARS_KIT_RUSHER("epicpvp.skywars.kit.rusher"),
SKYWARS_KIT_POLIZIST("epicpvp.skywars.kit.polizist"),
SKYWARS_KIT_SUPERMAN("epicpvp.skywars.kit.superman"),
SKYWARS_KIT_VIP("epicpvp.skywars.kit.vip"),
SKYWARS_KIT_ULTRA("epicpvp.skywars.kit.ultra"),
SKYWARS_KIT_LEGEND("epicpvp.skywars.kit.legend"),
SKYWARS_KIT_MVP("epicpvp.skywars.kit.mvp"),
SKYWARS_KIT_MVPPLUS("epicpvp.skywars.kit.mvpplus"),
//SHEEPWARS Kits

//SkyPvP - START -
SkyPvP_Mehr_Leben("epicpvp.skypvp.more_life"),
//SkyPvP - ENDE -

//One In The Chamber - START -
OneInTheChamber_KIT("epicpvp.oitc.kit"),
OneInTheChamber_LIFE("epicpvp.oitc.life"),
//One In The Chamber - ENDE -
SKYBLOCK_GILDEN_ISLAND("epicpvp.skyblock.gilden.island"),
SKYBLOCK_GILDEN_ISLAND_BORDER_BYPASS("epicpvp.skyblock.gilden.border.bypass"),
SKYBLOCK_PREMIUM_ISLAND("epicpvp.skyblock.schematic.premium"),
SKYBLOCK_NORMAL_ISLAND("epicpvp.skyblock.schematic.normal"),
SKYBLOCK_HOME_OTHER("epicpvp.skyblock.home.other"),
SKYBLOCK_ISLAND_BYPASS("epicpvp.skyblock.bypass"),
SKYBLOCK_ISLAND_BORDER_BYPASS("epicpvp.skyblock.borderbypass"),

//PARTICLE
PARTICLE_VIP("epicpvp.particle.vip"),
PARTICLE_ULTRA("epicpvp.particle.ultra"),
PARTICLE_LEGEND("epicpvp.particle.legend"),
PARTICLE_MVP("epicpvp.particle.mvp"),
PARTICLE_MVPPLUS("epicpvp.particle.mvpplus"),
//PARTICLE

//DELIVERY
DELIVERY_PET_VOTE("epicpvp.delivery.vote"),
DELIVERY_PET_TWITTER("epicpvp.delivery.twitter"),
DELIVERY_PET_VIP_WEEK("epicpvp.delivery.rank.vip.week"),
DELIVERY_PET_ULTRA_WEEK("epicpvp.delivery.rank.ultra.week"),
DELIVERY_PET_LEGEND_WEEK("epicpvp.delivery.rank.legend.week"),
DELIVERY_PET_MVP_WEEK("epicpvp.delivery.rank.mvp.week"),
DELIVERY_PET_MVPPLUS_WEEK("epicpvp.delivery.rank.,mvpplus.week"),
//DELIVERY

//PETS
PET_ALL("kpet.*"),
PET_VILLAGER("kpet.villager"),
PET_MAGMACUBE("kpet.magmacube"),
PET_WITCH("kpet.witch"),
PET_SKELETON("kpet.skeleton"),
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

//CAGE
CAGE_GRASS("cage.grass"),
CAGE_GLASS("cage.glass"),
CAGE_NETHER("cage.nether"),
//CAGE

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
DISGUISE_OCELOT("kdisguise.ocelot"),
//DISGUISE

NONE("FAIL");

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
