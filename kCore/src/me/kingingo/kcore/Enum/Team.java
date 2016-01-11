package me.kingingo.kcore.Enum;

import me.kingingo.kcore.Util.Color;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Team {
RED("RED", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)14), Color.RED+"Team - Rot"),Color.RED),
YELLOW("YELLOW", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)4), Color.YELLOW+"Team - Gelb"),Color.YELLOW),
BLUE("BLUE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)11), "§9Team - Blau"),"§9"),
GREEN("GREEN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)5), Color.GREEN+"Team - Grün"),Color.GREEN),
PINK("PINK",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)6), Color.PINK+"Team - Pink"),Color.PINK),
GRAY("GRAY",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)8), Color.GRAY+"Team - Gray"),Color.GRAY),
BLACK("BLACK", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)15), Color.BLACK+"Team - Schwarz"),Color.BLACK),
WHITE("WHITE",UtilItem.RenameItem(new ItemStack(Material.WOOL,1), Color.WHITE+"Team - White"),Color.WHITE),
PURPLE("PURPLE",UtilItem.RenameItem(new ItemStack(Material.WOOL, 1,(byte)10),Color.PURPLE+"Team - Lila"),Color.PURPLE ),
ORANGE("ORANGE",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)1), Color.ORANGE+"Team - Orange"),Color.ORANGE),
AQUA("AQUA",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)3), Color.AQUA+"Team - Hell Blau"),Color.AQUA),
CYAN("CYAN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)9), Color.CYAN+"Team - Cyan"),Color.CYAN),

VILLAGE_RED("VILLAGE_RED", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)14), Color.RED+"Villager - Rot"),Color.RED),
VILLAGE_YELLOW("VILLAGE_YELLOW", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)4), Color.YELLOW+"Villager - Gelb"),Color.YELLOW),
VILLAGE_BLUE("VILLAGE_BLUE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)11), Color.BLUE+"Villager - Blau"),Color.BLUE),
VILLAGE_GREEN("VILLAGE_GREEN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)5), Color.GREEN+"Villager - Grün"),Color.GREEN),
VILLAGE_PINK("VILLAGE_PINK", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)6), Color.PINK+"Villager - Pink"),Color.PINK),
VILLAGE_ORANGE("VILLAGE_ORANGE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)1), Color.ORANGE+"Villager - Orange"),Color.ORANGE),
VILLAGE_PURPLE("VILLAGE_PURPLE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)10), Color.PURPLE+"Villager - Lila"),Color.PURPLE),
VILLAGE_GRAY("VILLAGE_GRAY",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)8), Color.GRAY+"Villager - Grau"),Color.GRAY),
VILLAGE_BLACK("VILLAGE_BLACK", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)15), Color.BLACK+"Villager - Schwarz"),Color.BLACK),
VILLAGE_WHITE("VILLAGE_WHITE",UtilItem.RenameItem(new ItemStack(Material.WOOL,1), Color.WHITE+"Villager - White"),Color.WHITE),
VILLAGE_AQUA("VILLAGE_AQUA",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)3), Color.AQUA+"Villager - Hell Blau"),Color.AQUA),
VILLAGE_CYAN("VILLAGE_CYAN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)9), Color.CYAN+"Villager - Cyan"),Color.CYAN),

SHEEP_RED("SHEEP_RED", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)14), Color.RED+"Sheep - Rot"),Color.RED),
SHEEP_YELLOW("SHEEP_YELLOW", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)4), Color.YELLOW+"Sheep - Gelb"),Color.YELLOW),
SHEEP_BLUE("SHEEP_BLUE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)11), Color.BLUE+"Sheep - Blau"),Color.BLUE),
SHEEP_GREEN("SHEEP_GREEN",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)5), Color.GREEN+"Sheep - Grün"),Color.GREEN),
SHEEP_PINK("SHEEP_PINK", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)6), Color.PINK+"Sheep - Pink"),Color.PINK),
SHEEP_ORANGE("SHEEP_ORANGE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)1), Color.ORANGE+"Sheep - Orange"),Color.ORANGE),
SHEEP_PURPLE("SHEEP_PURPLE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)10), Color.PURPLE+"Sheep - Lila"),Color.PURPLE),
SHEEP_GRAY("SHEEP_GRAY",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)8), Color.GRAY+"Sheep - Grau"),Color.GRAY),
SHEEP_AQUA("SHEEP_AQUA", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)6), Color.AQUA+"Sheep - Hell Blau"),Color.AQUA),
SHEEP_CYAN("SHEEP_CYAN", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)9), Color.CYAN+"Sheep - Cyan"),Color.CYAN),
SHEEP_WHITE("SHEEP_WHITE", UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)10), Color.WHITE+"Sheep - Weiß"),Color.WHITE),
SHEEP_BLACK("SHEEP_BLACK",UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)8), Color.BLACK+"Sheep - Schwarz"),Color.BLACK),

TEAM_1("Team 1",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 1"),Color.AQUA),
TEAM_2("Team 2",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 2"),Color.AQUA),
TEAM_3("Team 3",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 3"),Color.AQUA),
TEAM_4("Team 4",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 4"),Color.AQUA),
TEAM_5("Team 5",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 5"),Color.AQUA),
TEAM_6("Team 6",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 6"),Color.AQUA),
TEAM_7("Team 7",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 7"),Color.AQUA),
TEAM_8("Team 8",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 8"),Color.AQUA),
TEAM_9("Team 9",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 9"),Color.AQUA),
TEAM_10("Team 10",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 10"),Color.AQUA),
TEAM_11("Team 11",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 11"),Color.AQUA),
TEAM_12("Team 12",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 12"),Color.AQUA),
TEAM_13("Team 13",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 13"),Color.AQUA),
TEAM_14("Team 14",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 14"),Color.AQUA),
TEAM_15("Team 15",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 15"),Color.AQUA),
TEAM_16("Team 16",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 16"),Color.AQUA),
TEAM_17("Team 17",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 17"),Color.AQUA),
TEAM_18("Team 18",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 18"),Color.AQUA),
TEAM_19("Team 19",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 19"),Color.AQUA),
TEAM_20("Team 20",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 20"),Color.AQUA),
TEAM_21("Team 21",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 21"),Color.AQUA),
TEAM_22("Team 22",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 22"),Color.AQUA),
TEAM_23("Team 23",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 23"),Color.AQUA),
TEAM_24("Team 24",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 24"),Color.AQUA),
TEAM_25("Team 25",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 25"),Color.AQUA),
TEAM_26("Team 26",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 26"),Color.AQUA),
TEAM_27("Team 27",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 27"),Color.AQUA),
TEAM_28("Team 28",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 28"),Color.AQUA),
TEAM_29("Team 29",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 29"),Color.AQUA),
TEAM_30("Team 30",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 30"),Color.AQUA),
TEAM_31("Team 31",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 31"),Color.AQUA),
TEAM_32("Team 32",UtilItem.RenameItem(new ItemStack(Material.EYE_OF_ENDER), Color.GREEN+"Team 32"),Color.AQUA),

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

}