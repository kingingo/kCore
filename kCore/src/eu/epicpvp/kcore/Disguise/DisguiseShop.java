package eu.epicpvp.kcore.Disguise;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import eu.epicpvp.kcore.Disguise.Events.DisguisePlayerLoadEvent;
import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryBuy;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesPackageBase;
import eu.epicpvp.kcore.MySQL.MySQL;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Update.UpdateType;
import eu.epicpvp.kcore.Update.Event.UpdateEvent;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilNumber;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;
import lombok.Setter;

public class DisguiseShop extends InventoryPageBase implements Listener{

	@Getter
	private PermissionManager permissionManager;
	@Getter
	private StatsManager money;
	@Getter
	private DisguiseManager disguiseManager;
	@Getter
	private ArrayList<Player> change_settings = new ArrayList<>();
	private HashMap<Integer,DisguiseType> settings = new HashMap<>();
	@Getter
	@Setter
	private boolean async=false;
	private MySQL mysql;
	
	public DisguiseShop(MySQL mysql,final PermissionManager permissionManager,StatsManager money,DisguiseManager disguiseManager) {
		super(36, "Disguise Shop");
		Bukkit.getPluginManager().registerEvents(this, permissionManager.getInstance());
		this.permissionManager=permissionManager;
		this.money=money;
		this.disguiseManager=disguiseManager;
		this.mysql=mysql;
		this.mysql.Update("CREATE TABLE IF NOT EXISTS `list_disguise`(playerId int,disguise varchar(100))");
		
		addButton(10, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getDisguiseManager().isDisguise(player)){
					getDisguiseManager().undisguise(player);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
				}
				player.closeInventory();
			}
			
		}, Material.SKULL_ITEM,SkullType.PLAYER.ordinal(),"§cPlayer", new String[]{"§7Du wirst wieder ein normaler Spieler."}));
		
		addButton(11, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, PermissionType.DISGUISE_ZOMBIE)||getPermissionManager().hasPermission(player, PermissionType.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.ZOMBIE);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, PermissionType.DISGUISE_ZOMBIE);
					}
					
				},"Kaufen",money,3000,7000);
				player.openInventory(buy);
				UtilInv.getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,54, "§aZombie", new String[]{"§6Kaufbares-Pet","§eCoins: 7000","§aGems: 3000"}));
		
		addButton(12, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, PermissionType.DISGUISE_ENDERMAN)||getPermissionManager().hasPermission(player, PermissionType.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.ENDERMAN);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, PermissionType.DISGUISE_ENDERMAN);
					}
					
				},"Kaufen",money,3000,7000);
				player.openInventory(buy);
				UtilInv.getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,58, "§aEnderman", new String[]{"§6Kaufbares-Pet","§eCoins: 7000","§aGems: 3000"}));
		
		addButton(13, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, PermissionType.DISGUISE_BLAZE)||getPermissionManager().hasPermission(player, PermissionType.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.BLAZE);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, PermissionType.DISGUISE_BLAZE);
					}
					
				},"Kaufen",money,3000,7000);
				player.openInventory(buy);
				UtilInv.getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,61, "§aBlaze", new String[]{"§6Kaufbares-Pet","§eCoins: 7000","§aGems: 3000"}));
		
		addButton(14, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, PermissionType.DISGUISE_WOLF)||getPermissionManager().hasPermission(player, PermissionType.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.WOLF);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, PermissionType.DISGUISE_WOLF);
					}
					
				},"Kaufen",money,2500,6000);
				player.openInventory(buy);
				UtilInv.getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,95, "§aWolf", new String[]{"§6Kaufbares-Pet","§eCoins: 6000","§aGems: 2500"}));
		
		addButton(15, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, PermissionType.DISGUISE_PIG)||getPermissionManager().hasPermission(player, PermissionType.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.PIG);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, PermissionType.DISGUISE_PIG);
					}
					
				},"Kaufen",money,2000,5000);
				player.openInventory(buy);
				UtilInv.getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,90, "§aPig", new String[]{"§6Kaufbares-Pet","§eCoins: 5000","§aGems: 2000"}));
		
		addButton(16, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, PermissionType.DISGUISE_SHEEP)||getPermissionManager().hasPermission(player, PermissionType.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.SHEEP);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, PermissionType.DISGUISE_SHEEP);
					}
					
				},"Kaufen",money,2000,5000);
				player.openInventory(buy);
				UtilInv.getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,91, "§aSheep", new String[]{"§6Kaufbares-Pet","§eCoins: 5000","§aGems: 2000"}));
		
		addButton(19, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, PermissionType.DISGUISE_CREEPER)||getPermissionManager().hasPermission(player, PermissionType.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.CREEPER);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, PermissionType.DISGUISE_CREEPER);
					}
					
				},"Kaufen",money,3000,7000);
				player.openInventory(buy);
				UtilInv.getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,50, "§aCreeper", new String[]{"§6Kaufbares-Pet","§eCoins: 7000","§aGems: 3000"}));
		
		addButton(20, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, PermissionType.DISGUISE_GUARDIAN)||getPermissionManager().hasPermission(player, PermissionType.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.GUARDIAN);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
				}
				player.closeInventory();
			}
			
		}, Material.MONSTER_EGG,68, "§6Guardian", new String[]{"§aPremium-Pet"}));
		
		addButton(21, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, PermissionType.DISGUISE_WITHERSKULL)||getPermissionManager().hasPermission(player, PermissionType.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.WITHER);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
				}
				player.closeInventory();
			}
			
		}, Material.SKULL_ITEM,SkullType.WITHER.ordinal(), "§cWither", new String[]{"§cNicht kaufbar!"}));
		
		fill(Material.STAINED_GLASS_PANE,(byte)7);
	}
	
	public void Delete(Player player){
		if(isAsync()){
			mysql.asyncUpdate("DELETE FROM `list_disguise` WHERE playerId='"+UtilPlayer.getPlayerId(player)+"'");
		}else{
			mysql.Update("DELETE FROM `list_disguise` WHERE playerId='"+UtilPlayer.getPlayerId(player)+"'");
		}
	}
	
	public void Insert(Player player){
		if(getDisguiseManager().isDisguise(player)){
			if(isAsync()){
				mysql.asyncUpdate("INSERT INTO `list_disguise` (playerId,disguise) VALUES ('"+UtilPlayer.getPlayerId(player)+"','"+getDisguiseManager().getDisguise(player).GetEntityTypeId()+"');");
			}else{
				mysql.Update("INSERT INTO `list_disguise` (playerId,disguise) VALUES ('"+UtilPlayer.getPlayerId(player)+"','"+getDisguiseManager().getDisguise(player).GetEntityTypeId()+"');");
			}
		}
	}
	
	public void Update(Player player){
		Delete(player);
		Insert(player);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void Quit(PlayerQuitEvent ev){
		if(getChange_settings().contains(ev.getPlayer())){
			getChange_settings().remove(ev.getPlayer());
			Update(ev.getPlayer());
		}
	}
	
	DisguisePlayerLoadEvent loadevent;
	int playerId;
	DisguiseType type;
	@EventHandler
	public void Place(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC_3)return;
		for(int i = 0; i < settings.size(); i++){
			playerId=UtilNumber.toInt(settings.keySet().toArray()[i]);
			if(UtilPlayer.isOnline( playerId )){
				
				loadevent = new DisguisePlayerLoadEvent(getDisguiseManager(),settings.get(playerId), UtilPlayer.searchExact(playerId));
				Bukkit.getPluginManager().callEvent(loadevent);
				
				if(loadevent.getType()!=null){
					try{
						if(loadevent.getObject()!=null){
							getDisguiseManager().disguise(loadevent.getPlayer(), loadevent.getType(),loadevent.getObject());
						}else{
							getDisguiseManager().disguise(loadevent.getPlayer(), loadevent.getType());
						}
					}catch(IllegalArgumentException e){
						e.printStackTrace();
					}
				}
				
				settings.remove(playerId);
			}
		}
	}
	
	public void load(int playerId){
		String sql = mysql.getString("SELECT `disguise` FROM `list_disguise` WHERE `playerId`='"+playerId+"'");
		if(!sql.equalsIgnoreCase("null")){
			settings.put(playerId, DisguiseType.valueOf(sql));
		}else{
			settings.put(playerId, null);
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void Join(AsyncPlayerPreLoginEvent ev){
		load(UtilPlayer.getPlayerId(ev.getName()));
	}

}
