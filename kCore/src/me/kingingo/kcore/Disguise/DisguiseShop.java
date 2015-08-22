package me.kingingo.kcore.Disguise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Disguise.disguises.livings.DisguiseWither;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryBuy;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.SalesPackageBase;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.avaje.ebeaninternal.server.deploy.BeanDescriptor.EntityType;

public class DisguiseShop extends InventoryPageBase implements Listener{

	@Getter
	private PermissionManager permissionManager;
	@Getter
	private Coins coins;
	@Getter
	private DisguiseManager disguiseManager;
	@Getter
	private ArrayList<Player> change_settings = new ArrayList<>();
	private HashMap<UUID,String> settings = new HashMap<>();
	
	public DisguiseShop(final InventoryBase base,final PermissionManager permissionManager,final Coins coins,DisguiseManager disguiseManager) {
		super(36, "Disguise Shop");
		Bukkit.getPluginManager().registerEvents(this, permissionManager.getInstance());
		this.permissionManager=permissionManager;
		this.coins=coins;
		this.disguiseManager=disguiseManager;
		this.permissionManager.getMysql().Update("CREATE TABLE IF NOT EXISTS list_disguise(uuid varchar(100),disguise varchar(100))");
		
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
				if(getPermissionManager().hasPermission(player, kPermission.DISGUISE_ZOMBIE)||getPermissionManager().hasPermission(player, kPermission.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.ZOMBIE);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, kPermission.DISGUISE_ZOMBIE);
					}
					
				},"Kaufen",coins,7000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,54, "§aZombie", new String[]{"§6Kaufbares-Pet","§eCoins: 7000"}));
		
		addButton(12, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, kPermission.DISGUISE_ENDERMAN)||getPermissionManager().hasPermission(player, kPermission.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.ENDERMAN);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, kPermission.DISGUISE_ENDERMAN);
					}
					
				},"Kaufen",coins,7000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,58, "§aEnderman", new String[]{"§6Kaufbares-Pet","§eCoins: 7000"}));
		
		addButton(13, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, kPermission.DISGUISE_BLAZE)||getPermissionManager().hasPermission(player, kPermission.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.BLAZE);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, kPermission.DISGUISE_BLAZE);
					}
					
				},"Kaufen",coins,7000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,61, "§aBlaze", new String[]{"§6Kaufbares-Pet","§eCoins: 7000"}));
		
		addButton(14, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, kPermission.DISGUISE_WOLF)||getPermissionManager().hasPermission(player, kPermission.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.WOLF);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, kPermission.DISGUISE_WOLF);
					}
					
				},"Kaufen",coins,6000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,95, "§aWolf", new String[]{"§6Kaufbares-Pet","§eCoins: 6000"}));
		
		addButton(15, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, kPermission.DISGUISE_PIG)||getPermissionManager().hasPermission(player, kPermission.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.PIG);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, kPermission.DISGUISE_PIG);
					}
					
				},"Kaufen",coins,5000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,90, "§aPig", new String[]{"§6Kaufbares-Pet","§eCoins: 5000"}));
		
		addButton(16, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, kPermission.DISGUISE_SHEEP)||getPermissionManager().hasPermission(player, kPermission.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.SHEEP);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, kPermission.DISGUISE_SHEEP);
					}
					
				},"Kaufen",coins,5000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,91, "§aSheep", new String[]{"§6Kaufbares-Pet","§eCoins: 5000"}));
		
		addButton(19, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, kPermission.DISGUISE_CREEPER)||getPermissionManager().hasPermission(player, kPermission.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.CREEPER);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getPermissionManager().addPermission(player, kPermission.DISGUISE_CREEPER);
					}
					
				},"Kaufen",coins,7000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,50, "§aCreeper", new String[]{"§6Kaufbares-Pet","§eCoins: 7000"}));
		
		addButton(20, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, kPermission.DISGUISE_GUARDIAN)||getPermissionManager().hasPermission(player, kPermission.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.GUARDIAN);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
				}
				player.closeInventory();
			}
			
		}, Material.MONSTER_EGG,68, "§6Guardian", new String[]{"§aPremium-Pet"}));
		
		addButton(21, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getPermissionManager().hasPermission(player, kPermission.DISGUISE_WITHERSKULL)||getPermissionManager().hasPermission(player, kPermission.DISGUISE_ALL)){
					getDisguiseManager().disguise(player, DisguiseType.WITHER);
					if(!getChange_settings().contains(player))getChange_settings().add(player);
				}
				player.closeInventory();
			}
			
		}, Material.SKULL_ITEM,SkullType.WITHER.ordinal(), "§cWither", new String[]{"§cNicht kaufbar!"}));
		
		fill(Material.STAINED_GLASS_PANE,(byte)7);
	}
	
	public void Delete(Player player){
		getPermissionManager().getMysql().Update("DELETE FROM list_disguise WHERE uuid='"+UtilPlayer.getRealUUID(player)+"'");
	}
	
	public void Insert(Player player){
		if(getDisguiseManager().isDisguise(player)){
			getPermissionManager().getMysql().Update("INSERT INTO list_disguise (uuid,disguise) VALUES ('"+UtilPlayer.getRealUUID(player)+"','"+getDisguiseManager().getDisguise(player).GetEntityTypeId()+"');");
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
	
	UUID player;
	DisguiseType type;
	@EventHandler
	public void Place(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC_3)return;
		for(int i = 0; i < settings.size(); i++){
			player=((UUID)settings.keySet().toArray()[i]);
			if(UtilPlayer.isOnline( player )){
				try{
					type=DisguiseType.valueOf(settings.get(player));
					getDisguiseManager().disguise(Bukkit.getPlayer(player), type);
				}catch(IllegalArgumentException e){
					
				}
				settings.remove(player);
			}
		}
	}
	
	public void load(UUID uuid){
		String sql = getPermissionManager().getMysql().getString("SELECT `disguise` FROM `list_disguise` WHERE uuid='"+uuid+"'");
		if(!sql.equalsIgnoreCase("null"))settings.put(uuid, sql);
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void Join(AsyncPlayerPreLoginEvent ev){
		load(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()));
	}

}
