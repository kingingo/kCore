package me.kingingo.kcore.Enum;

import lombok.Getter;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum GameCase {

GRASS(false,new ItemStack(Material.STAINED_GLASS,(byte)5),new ItemStack(Material.GRASS),kPermission.ALL_PERMISSION,3000),
NETHER(true,new ItemStack(Material.NETHER_FENCE),new ItemStack(Material.NETHER_BRICK),kPermission.ALL_PERMISSION,3000),
GLASS(true,new ItemStack(Material.STAINED_GLASS),new ItemStack(Material.STAINED_GLASS),kPermission.ALL_PERMISSION,3000);

private ItemStack ground;
private ItemStack wall;
@Getter
private kPermission permission;
@Getter
private boolean teamColor;
@Getter
private int coins;

private GameCase(boolean teamColor,ItemStack wall,ItemStack ground,kPermission permission,int coins){
	this.wall=wall;
	this.teamColor=teamColor;
	this.ground=ground;
	this.permission=permission;
	this.coins=coins;
}

public ItemStack getGround(byte id){
	if(isTeamColor()){
		return new ItemStack(this.ground.getType(),id);
	}else{
		return this.ground;
	}
}

public ItemStack getWall(byte id){
	if(isTeamColor()){
		return new ItemStack(this.wall.getType(),id);
	}else{
		return this.wall;
	}
}

public static GameCase getByPermission(kPermission permission){
	for(GameCase gcase : GameCase.values())if(gcase.getPermission()==permission)return gcase;
	return GameCase.GLASS;
}


public static void saveGameCase(Player player,GameCase gcase,MySQL mysql){
	if(!create){
		mysql.createTable("user_cases", "player varchar(30),uuid varchar(50),case varchar(30)");
		create=true;
	}
	
	mysql.Delete("user_cases", "uuid='"+UtilPlayer.getRealUUID(player)+"'");
	mysql.Insert("user_cases", "player,uuid,case", "'"+player.getName()+"','"+UtilPlayer.getRealUUID(player)+"','"+gcase.name()+"'");
}

private static boolean create=false;
public static GameCase getGameCase(Player player,MySQL mysql){
	if(!create){
		mysql.createTable("user_cases", "player varchar(30),uuid varchar(50),case varchar(30)");
		create=true;
	}
	
	String gcase = mysql.getString("SELECT case FROM user_cases WHERE uuid='"+UtilPlayer.getRealUUID(player)+"'");
	
	if(!gcase.equalsIgnoreCase("null")){
		for(GameCase caseg : values()){
			if(caseg.name().equalsIgnoreCase(gcase))return caseg;
		}
	}
	return GameCase.GLASS;
}

}
