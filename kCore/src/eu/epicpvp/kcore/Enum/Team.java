package eu.epicpvp.kcore.Enum;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Util.Color;
import eu.epicpvp.kcore.Util.UtilItem;

public enum Team {
RED("RED", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)14), Color.RED+"Team - Rot"),Color.RED),
YELLOW("YELLOW", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)4), Color.YELLOW+"Team - Gelb"),Color.YELLOW),
BLUE("BLUE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)11), "§9Team - Blau"),"§9"),
GREEN("GREEN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)13), Color.GREEN+"Team - Grün"),Color.GREEN),
PINK("PINK",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)6), Color.PINK+"Team - Pink"),Color.PINK),
GRAY("GRAY",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)7), Color.GRAY+"Team - Grau"),Color.GRAY),
BLACK("BLACK", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)15), Color.BLACK+"Team - Schwarz"),Color.BLACK),
WHITE("WHITE",UtilItem.RenameItem(new ItemStack(Material.WOOL,1), Color.WHITE+"Team - Weiß"),Color.WHITE),
PURPLE("PURPLE",UtilItem.RenameItem(new ItemStack(Material.WOOL, 1,(byte)10),Color.PURPLE+"Team - Lila"),Color.PURPLE ),
ORANGE("ORANGE",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)1), Color.ORANGE+"Team - Orange"),Color.ORANGE),
AQUA("AQUA",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)3), Color.AQUA+"Team - Hell Blau"),Color.AQUA),
CYAN("CYAN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)9), Color.CYAN+"Team - Cyan"),Color.CYAN),
MAGENTA("MAGENTA",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)2), Color.GRAY+"Team - Magenta"),"§5"),
LIGHT_GRAY("LIGHT_GRAY",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)8), Color.GRAY+"Team - Hell Grau"),"§7"),
BROWN("BROWN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)12), Color.GRAY+"Team - Braun"),"§8"),
LIME("LIME",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)5), Color.GRAY+"Team - Hell Grün"),"§a"),

VILLAGE_RED("VILLAGE_RED", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)14), Color.RED+"Villager - Rot"),Color.RED),
VILLAGE_YELLOW("VILLAGE_YELLOW", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)4), Color.YELLOW+"Villager - Gelb"),Color.YELLOW),
VILLAGE_BLUE("VILLAGE_BLUE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)11), Color.BLUE+"Villager - Blau"),Color.BLUE),
VILLAGE_GREEN("VILLAGE_GREEN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)5), Color.GREEN+"Villager - Grün"),Color.GREEN),
VILLAGE_PINK("VILLAGE_PINK", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)6), Color.PINK+"Villager - Pink"),Color.PINK),
VILLAGE_ORANGE("VILLAGE_ORANGE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)1), Color.ORANGE+"Villager - Orange"),Color.ORANGE),
VILLAGE_PURPLE("VILLAGE_PURPLE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)10), Color.PURPLE+"Villager - Lila"),Color.PURPLE),
VILLAGE_GRAY("VILLAGE_GRAY",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)7), Color.GRAY+"Villager - Grau"),Color.GRAY),
VILLAGE_BLACK("VILLAGE_BLACK", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)15), Color.BLACK+"Villager - Schwarz"),Color.BLACK),
VILLAGE_WHITE("VILLAGE_WHITE",UtilItem.RenameItem(new ItemStack(Material.WOOL,1), Color.WHITE+"Villager - White"),Color.WHITE),
VILLAGE_AQUA("VILLAGE_AQUA",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)3), Color.AQUA+"Villager - Hell Blau"),Color.AQUA),
VILLAGE_CYAN("VILLAGE_CYAN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)9), Color.CYAN+"Villager - Cyan"),Color.CYAN),
VILLAGE_MAGENTA("VILLAGE_MAGENTA",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)2), Color.GRAY+"Villager - Magenta"),"§5"),
VILLAGE_LIGHT_GRAY("VILLAGE_LIGHT_GRAY",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)8), Color.GRAY+"Villager - Hell Grau"),"§7"),
VILLAGE_BROWN("VILLAGE_BROWN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)12), Color.GRAY+"Villager - Braun"),"§8"),
VILLAGE_LIME("VILLAGE_LIME",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)5), Color.GRAY+"Villager - Hell Grün"),"§a"),

SHEEP_RED("SHEEP_RED", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)14), Color.RED+"Sheep - Rot"),Color.RED),
SHEEP_YELLOW("SHEEP_YELLOW", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)4), Color.YELLOW+"Sheep - Gelb"),Color.YELLOW),
SHEEP_BLUE("SHEEP_BLUE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)11), Color.BLUE+"Sheep - Blau"),Color.BLUE),
SHEEP_GREEN("SHEEP_GREEN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)5), Color.GREEN+"Sheep - Grün"),Color.GREEN),
SHEEP_PINK("SHEEP_PINK", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)6), Color.PINK+"Sheep - Pink"),Color.PINK),
SHEEP_ORANGE("SHEEP_ORANGE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)1), Color.ORANGE+"Sheep - Orange"),Color.ORANGE),
SHEEP_PURPLE("SHEEP_PURPLE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)10), Color.PURPLE+"Sheep - Lila"),Color.PURPLE),
SHEEP_GRAY("SHEEP_GRAY",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)7), Color.GRAY+"Sheep - Grau"),Color.GRAY),
SHEEP_AQUA("SHEEP_AQUA", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)6), Color.AQUA+"Sheep - Hell Blau"),Color.AQUA),
SHEEP_CYAN("SHEEP_CYAN", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)9), Color.CYAN+"Sheep - Cyan"),Color.CYAN),
SHEEP_WHITE("SHEEP_WHITE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)10), Color.WHITE+"Sheep - Weiß"),Color.WHITE),
SHEEP_BLACK("SHEEP_BLACK",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)8), Color.BLACK+"Sheep - Schwarz"),Color.BLACK),
SHEEP_MAGENTA("SHEEP_MAGENTA",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)2), "§5Sheep - Magenta"),"§5"),
SHEEP_LIGHT_GRAY("SHEEP_LIGHT_GRAY",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)8), "§7Sheep - Hell Grau"),"§7"),
SHEEP_BROWN("SHEEP_BROWN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)12), "§8Sheep - Braun"),"§8"),
SHEEP_LIME("SHEEP_LIME",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)5), "§aSheep - Hell Grün"),"§a"),

TEAM_1("TEAM_1",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 1"),Color.AQUA),
TEAM_2("TEAM_2",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 2"),Color.AQUA),
TEAM_3("TEAM_3",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 3"),Color.AQUA),
TEAM_4("TEAM_4",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 4"),Color.AQUA),
TEAM_5("TEAM_5",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 5"),Color.AQUA),
TEAM_6("TEAM_6",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 6"),Color.AQUA),
TEAM_7("TEAM_7",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 7"),Color.AQUA),
TEAM_8("TEAM_8",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 8"),Color.AQUA),
TEAM_9("TEAM_9",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 9"),Color.AQUA),
TEAM_10("TEAM_10",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 10"),Color.AQUA),
TEAM_11("TEAM_11",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 11"),Color.AQUA),
TEAM_12("TEAM_12",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 12"),Color.AQUA),
TEAM_13("TEAM_13",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 13"),Color.AQUA),
TEAM_14("TEAM_14",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 14"),Color.AQUA),
TEAM_15("TEAM_15",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 15"),Color.AQUA),
TEAM_16("TEAM_16",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 16"),Color.AQUA),
TEAM_17("TEAM_17",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 17"),Color.AQUA),
TEAM_18("TEAM_18",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 18"),Color.AQUA),
TEAM_19("TEAM_19",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 19"),Color.AQUA),
TEAM_20("TEAM_20",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 20"),Color.AQUA),
TEAM_21("TEAM_21",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 21"),Color.AQUA),
TEAM_22("TEAM_22",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 22"),Color.AQUA),
TEAM_23("TEAM_23",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 23"),Color.AQUA),
TEAM_24("TEAM_24",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 24"),Color.AQUA),
TEAM_25("TEAM_25",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 25"),Color.AQUA),
TEAM_26("TEAM_26",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 26"),Color.AQUA),
TEAM_27("TEAM_27",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 27"),Color.AQUA),
TEAM_28("TEAM_28",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 28"),Color.AQUA),
TEAM_29("TEAM_29",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 29"),Color.AQUA),
TEAM_30("TEAM_30",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 30"),Color.AQUA),
TEAM_31("TEAM_31",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 31"),Color.AQUA),
TEAM_32("TEAM_32",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 32"),Color.AQUA),


TEAM_POINT_1("TEAM_POINT_1",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 1"),Color.AQUA),
TEAM_POINT_2("TEAM_POINT_2",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 2"),Color.AQUA),
TEAM_POINT_3("TEAM_POINT_3",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 3"),Color.AQUA),
TEAM_POINT_4("TEAM_POINT_4",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 4"),Color.AQUA),
TEAM_POINT_5("TEAM_POINT_5",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 5"),Color.AQUA),
TEAM_POINT_6("TEAM_POINT_6",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 6"),Color.AQUA),
TEAM_POINT_7("TEAM_POINT_7",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 7"),Color.AQUA),
TEAM_POINT_8("TEAM_POINT_8",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 8"),Color.AQUA),
TEAM_POINT_9("TEAM_POINT_9",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 9"),Color.AQUA),
TEAM_POINT_10("TEAM_POINT_10",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 10"),Color.AQUA),
TEAM_POINT_11("TEAM_POINT_11",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 11"),Color.AQUA),
TEAM_POINT_12("TEAM_POINT_12",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 12"),Color.AQUA),
TEAM_POINT_13("TEAM_POINT_13",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 13"),Color.AQUA),
TEAM_POINT_14("TEAM_POINT_14",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 14"),Color.AQUA),
TEAM_POINT_15("TEAM_POINT_15",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 15"),Color.AQUA),
TEAM_POINT_16("TEAM_POINT_16",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 16"),Color.AQUA),
TEAM_POINT_17("TEAM_POINT_17",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 17"),Color.AQUA),
TEAM_POINT_18("TEAM_POINT_18",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 18"),Color.AQUA),
TEAM_POINT_19("TEAM_POINT_19",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 19"),Color.AQUA),
TEAM_POINT_20("TEAM_POINT_20",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 20"),Color.AQUA),
TEAM_POINT_21("TEAM_POINT_21",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 21"),Color.AQUA),
TEAM_POINT_22("TEAM_POINT_22",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 22"),Color.AQUA),
TEAM_POINT_23("TEAM_POINT_23",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 23"),Color.AQUA),
TEAM_POINT_24("TEAM_POINT_24",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 24"),Color.AQUA),
TEAM_POINT_25("TEAM_POINT_25",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 25"),Color.AQUA),
TEAM_POINT_26("TEAM_POINT_26",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 26"),Color.AQUA),
TEAM_POINT_27("TEAM_POINT_27",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 27"),Color.AQUA),
TEAM_POINT_28("TEAM_POINT_28",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 28"),Color.AQUA),
TEAM_POINT_29("TEAM_POINT_29",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 29"),Color.AQUA),
TEAM_POINT_30("TEAM_POINT_30",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 30"),Color.AQUA),
TEAM_POINT_31("TEAM_POINT_31",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 31"),Color.AQUA),
TEAM_POINT_32("TEAM_POINT_32",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team Point 32"),Color.AQUA),

GOLD("GOLD",UtilItem.RenameItem(new ItemStack(Material.GOLD_BLOCK,1), Color.YELLOW+"Gold"),Color.YELLOW),
SILBER("SILBER",UtilItem.RenameItem(new ItemStack(Material.IRON_BLOCK,1), Color.YELLOW+"Silber"),Color.YELLOW),
BRONZE("BRONZE",UtilItem.RenameItem(new ItemStack(Material.COAL_BLOCK,1), Color.YELLOW+"Bronze"),Color.YELLOW),
DIAMOND("DIAMOND",UtilItem.RenameItem(new ItemStack(Material.DIAMOND_BLOCK,1), Color.AQUA+"Diamanten"),Color.AQUA),

DISTRICT_1("District 1",UtilItem.RenameItem(new ItemStack(Material.WORKBENCH,1), Color.GREEN+"District 1"),Color.AQUA),
DISTRICT_2("District 2",UtilItem.RenameItem(new ItemStack(Material.WORKBENCH,1), Color.GREEN+"District 2"),Color.AQUA),
DISTRICT_3("District 3",UtilItem.RenameItem(new ItemStack(Material.WORKBENCH,1), Color.GREEN+"District 3"),Color.AQUA),
DISTRICT_4("District 4",UtilItem.RenameItem(new ItemStack(Material.WORKBENCH,1), Color.GREEN+"District 4"),Color.AQUA),
DISTRICT_5("District 5",UtilItem.RenameItem(new ItemStack(Material.WORKBENCH,1), Color.GREEN+"District 5"),Color.AQUA),
DISTRICT_6("District 6",UtilItem.RenameItem(new ItemStack(Material.WORKBENCH,1), Color.GREEN+"District 6"),Color.AQUA),
DISTRICT_7("District 7",UtilItem.RenameItem(new ItemStack(Material.WORKBENCH,1), Color.GREEN+"District 7"),Color.AQUA),
DISTRICT_8("District 8",UtilItem.RenameItem(new ItemStack(Material.WORKBENCH,1), Color.GREEN+"District 8"),Color.AQUA),
DISTRICT_9("District 9",UtilItem.RenameItem(new ItemStack(Material.WORKBENCH,1), Color.GREEN+"District 9"),Color.AQUA),
DISTRICT_10("District 10",UtilItem.RenameItem(new ItemStack(Material.WORKBENCH,1), Color.GREEN+"District 10"),Color.AQUA),
DISTRICT_11("District 11",UtilItem.RenameItem(new ItemStack(Material.WORKBENCH,1), Color.GREEN+"District 11"),Color.AQUA),
DISTRICT_12("District 12",UtilItem.RenameItem(new ItemStack(Material.WORKBENCH,1), Color.GREEN+"District 12"),Color.AQUA),

TRAITOR("Traitor",UtilItem.RenameItem(new ItemStack(Material.IRON_SWORD), Color.RED+"Traitor"),Color.RED),
INOCCENT("Inoccent",UtilItem.RenameItem(new ItemStack(Material.BOW),Color.GRAY+"Inoccent"), Color.GRAY),
DETECTIVE("Detective", UtilItem.RenameItem(new ItemStack(Material.STICK),Color.BLUE+"Detective"), Color.BLUE),

SOLO("SOLO",null,Color.GRAY);

private String n;
private ItemStack i;
private String c;
private ItemStack[] armor;

private Team(String n,ItemStack i,String c){
	this.n=n;
	this.i=i;
	this.c=c;
	this.armor=armor;
}

public String getColor(){
	return this.c;
}

public ItemStack getItem(){
	return this.i;
}

public String Name(){
	return n;
}

public static Team getPoint(Team team){
	switch(team){
	case TEAM_1: return Team.TEAM_POINT_1;
	case TEAM_2: return Team.TEAM_POINT_2;
	case TEAM_3: return Team.TEAM_POINT_3;
	case TEAM_4: return Team.TEAM_POINT_4;
	case TEAM_5: return Team.TEAM_POINT_5;
	case TEAM_6: return Team.TEAM_POINT_6;
	case TEAM_7: return Team.TEAM_POINT_7;
	case TEAM_8: return Team.TEAM_POINT_8;
	case TEAM_9: return Team.TEAM_POINT_9;
	case TEAM_10: return Team.TEAM_POINT_10;
	case TEAM_11: return Team.TEAM_POINT_11;
	case TEAM_12: return Team.TEAM_POINT_12;
	case TEAM_13: return Team.TEAM_POINT_13;
	case TEAM_14: return Team.TEAM_POINT_14;
	case TEAM_15: return Team.TEAM_POINT_15;
	case TEAM_16: return Team.TEAM_POINT_16;
	case TEAM_17: return Team.TEAM_POINT_17;
	case TEAM_18: return Team.TEAM_POINT_18;
	case TEAM_19: return Team.TEAM_POINT_19;
	case TEAM_20: return Team.TEAM_POINT_20;
	case TEAM_21: return Team.TEAM_POINT_21;
	case TEAM_22: return Team.TEAM_POINT_22;
	case TEAM_23: return Team.TEAM_POINT_23;
	case TEAM_24: return Team.TEAM_POINT_24;
	case TEAM_25: return Team.TEAM_POINT_25;
	case TEAM_26: return Team.TEAM_POINT_26;
	case TEAM_27: return Team.TEAM_POINT_27;
	case TEAM_28: return Team.TEAM_POINT_28;
	case TEAM_29: return Team.TEAM_POINT_29;
	case TEAM_30: return Team.TEAM_POINT_30;
	case TEAM_31: return Team.TEAM_POINT_31;
	case TEAM_32: return Team.TEAM_POINT_32;
	}
	return null;
}

}