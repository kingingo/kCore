package eu.epicpvp.kcore.Enum;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;

public enum GameCage {

GLASS(true,new ItemStack(Material.STAINED_GLASS),UtilItem.Item(new ItemStack(Material.STAINED_GLASS), new String[]{"§aDefault Cage"}, "§7Glass Cage"),PermissionType.CAGE_GLASS,3000),
GRASS(false,new ItemStack(Material.FENCE),UtilItem.Item(new ItemStack(Material.GRASS), new String[]{""}, "§aGrass Cage"),PermissionType.CAGE_GRASS,3000),
SLIME(false,new ItemStack(Material.SLIME_BLOCK),UtilItem.Item(new ItemStack(Material.STAINED_GLASS,1,(byte)1), new String[]{}, ""),PermissionType.CAGE_GRASS,3000),
NETHER(true,new ItemStack(Material.NETHER_FENCE),UtilItem.Item(new ItemStack(Material.NETHER_BRICK), new String[]{""}, "§cNether Cage"),PermissionType.CAGE_NETHER,3000);

@Getter
private ItemStack ground;
@Getter
private ItemStack wall;
@Getter
private PermissionType permission;
@Getter
private boolean teamColor;
@Getter
private int coins;

private GameCage(boolean teamColor,ItemStack wall,ItemStack ground,PermissionType permission,int coins){
	this.wall=wall;
	this.teamColor=teamColor;
	this.ground=ground;
	this.permission=permission;
	this.coins=coins;
}

public ItemStack getGround(byte id){
	if(isTeamColor()){
		return new ItemStack(this.ground.getType(),1,id);
	}else{
		return this.ground;
	}
}

public ItemStack getWall(byte id){
	if(isTeamColor()){
		return new ItemStack(this.wall.getType(),1,id);
	}else{
		return this.wall;
	}
}

public static GameCage getByPermission(PermissionType permission){
	for(GameCage gcase : GameCage.values())if(gcase.getPermission()==permission)return gcase;
	return GameCage.GLASS;
}


public static void saveGameCase(Player player,GameCage gcase,MySQL mysql){
	if(!create){
		mysql.createTable("users_cage", "playerId int,cage varchar(30)");
		create=true;
	}
	
	mysql.Delete("users_cage", "`playerId`='"+UtilPlayer.getPlayerId(player)+"'");
	mysql.Insert("users_cage", "playerId`,`cage`", "'"+UtilPlayer.getPlayerId(player)+"','"+gcase.name()+"'");
}

private static boolean create=false;
public static GameCage getGameCase(Player player,MySQL mysql){
	if(!create){
		mysql.createTable("users_cage", "playerId int,cage varchar(30)");
		create=true;
	}
	
	String gcase = mysql.getString("SELECT `cage` FROM `users_cage` WHERE `playerId`='"+UtilPlayer.getPlayerId(player)+"'");
	
	if(!gcase.equalsIgnoreCase("null")){
		for(GameCage caseg : values()){
			if(caseg.name().equalsIgnoreCase(gcase))return caseg;
		}
	}
	return GameCage.GLASS;
}

}
