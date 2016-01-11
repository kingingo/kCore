package me.kingingo.kcore.Enum;

import lombok.Getter;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum GameCage {

GLASS(true,new ItemStack(Material.STAINED_GLASS),UtilItem.Item(new ItemStack(Material.STAINED_GLASS), new String[]{"§aDefault Cage"}, "§7Glass Cage"),kPermission.CAGE_GLASS,3000),
GRASS(false,new ItemStack(Material.FENCE),UtilItem.Item(new ItemStack(Material.GRASS), new String[]{""}, "§aGrass Cage"),kPermission.CAGE_GRASS,3000),
SLIME(false,new ItemStack(Material.SLIME_BLOCK),UtilItem.Item(new ItemStack(Material.STAINED_GLASS,1,(byte)1), new String[]{}, ""),kPermission.CAGE_GRASS,3000),
NETHER(true,new ItemStack(Material.NETHER_FENCE),UtilItem.Item(new ItemStack(Material.NETHER_BRICK), new String[]{""}, "§cNether Cage"),kPermission.CAGE_NETHER,3000);

@Getter
private ItemStack ground;
@Getter
private ItemStack wall;
@Getter
private kPermission permission;
@Getter
private boolean teamColor;
@Getter
private int coins;

private GameCage(boolean teamColor,ItemStack wall,ItemStack ground,kPermission permission,int coins){
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

public static GameCage getByPermission(kPermission permission){
	for(GameCage gcase : GameCage.values())if(gcase.getPermission()==permission)return gcase;
	return GameCage.GLASS;
}


public static void saveGameCase(Player player,GameCage gcase,MySQL mysql){
	if(!create){
		mysql.createTable("users_cage", "player varchar(30),uuid varchar(50),cage varchar(30)");
		create=true;
	}
	
	mysql.Delete("users_cage", "uuid='"+UtilPlayer.getRealUUID(player)+"'");
	mysql.Insert("users_cage", "player,uuid,cage", "'"+player.getName()+"','"+UtilPlayer.getRealUUID(player)+"','"+gcase.name()+"'");
}

private static boolean create=false;
public static GameCage getGameCase(Player player,MySQL mysql){
	if(!create){
		mysql.createTable("users_cage", "player varchar(30),uuid varchar(50),cage varchar(30)");
		create=true;
	}
	
	String gcase = mysql.getString("SELECT cage FROM users_cage WHERE uuid='"+UtilPlayer.getRealUUID(player)+"'");
	
	if(!gcase.equalsIgnoreCase("null")){
		for(GameCage caseg : values()){
			if(caseg.name().equalsIgnoreCase(gcase))return caseg;
		}
	}
	return GameCage.GLASS;
}

}
