package me.kingingo.kcore.Pet.Shop;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryBuy;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.SalesPackageBase;
import me.kingingo.kcore.MySQL.MySQL;
import me.kingingo.kcore.Permission.Permission;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Pet.PetManager;
import me.kingingo.kcore.Pet.Setting.PetSetting;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.Tokens;
import me.kingingo.kcore.Util.UtilPlayer;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftAgeable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PetShop extends InventoryBase{

	@Getter
	private PetManager manager;
	@Getter
	private PermissionManager permManager;
	@Getter
	private ArrayList<Player> change_settings = new ArrayList<>();
	private HashMap<String,String> settings = new HashMap<>();
	
	public PetShop(final PetManager manager,final PermissionManager permManager,final Coins coins,final Tokens tokens){
		super(manager.getInstance(),27,"Pet-Shop");
		this.manager=manager;
		this.permManager=permManager;
		this.manager.setShop(this);
		this.manager.setSetting(true);
		
		this.permManager.getMysql().Update("CREATE TABLE IF NOT EXISTS list_pet(player varchar(30),pet varchar(100))");
		
		this.manager.getSetting_list().put(EntityType.IRON_GOLEM, new PetSetting(manager,EntityType.IRON_GOLEM,UtilItem.RenameItem(new ItemStack(Material.IRON_BLOCK), "§aIronGolem")));
		this.manager.getSetting_list().put(EntityType.PIG, new PetSetting(manager,EntityType.PIG,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 90), "§aPig")));
		this.manager.getSetting_list().put(EntityType.WOLF, new PetSetting(manager,EntityType.WOLF,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 95), "§aWolf")));
		this.manager.getSetting_list().put(EntityType.SHEEP, new PetSetting(manager,EntityType.SHEEP,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 91), "§aSchaf")));
		this.manager.getSetting_list().put(EntityType.COW, new PetSetting(manager,EntityType.COW,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 92), "§aCow")));
		this.manager.getSetting_list().put(EntityType.ZOMBIE, new PetSetting(manager,EntityType.ZOMBIE,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 57), "§aZombie")));
		this.manager.getSetting_list().put(EntityType.OCELOT, new PetSetting(manager,EntityType.OCELOT,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 98), "§aOcelot")));
		
		getMain().setItem(0, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(1, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(2, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(3, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(4, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(5, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(6, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(7, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(8, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));	
		getMain().setItem(9, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(17, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));	
		getMain().setItem(18, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(19, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(20, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(21, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(22, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(23, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(24, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(25, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		getMain().setItem(26, UtilItem.RenameItem(new ItemStack(160,1,(byte)7)," "));
		
		getMain().addButton(14, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, Permission.PET_OCELOT)){
					manager.AddPetOwner(player, "Ocelot", EntityType.OCELOT, player.getLocation());
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, Permission.PET_OCELOT);
					}
					
				},"Kaufen",coins,4000,tokens,100);
				player.openInventory(buy);
				addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,98, "Ocelot", new String[]{""}));
		
		getMain().addButton(16, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, Permission.PET_ZOMBIE)){
					manager.AddPetOwner(player, "Zombie", EntityType.ZOMBIE, player.getLocation());
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, Permission.PET_ZOMBIE);
					}
					
				},"Kaufen",coins,5000,tokens,120);
				player.openInventory(buy);
				addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,57, "Zombie", new String[]{""}));
		
		getMain().addButton(15, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, Permission.PET_COW)){
					manager.AddPetOwner(player, "Cow", EntityType.COW, player.getLocation());
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, Permission.PET_COW);
					}
					
				},"Kaufen",coins,4000,tokens,100);
				player.openInventory(buy);
				addAnother(buy);
				}
			}
			
		},  Material.MONSTER_EGG,92, "Cow", new String[]{""}));
		
		getMain().addButton(10, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, Permission.PET_IRON_GOLEM)){
					manager.AddPetOwner(player, "IronGolem", EntityType.IRON_GOLEM, player.getLocation());
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, Permission.PET_IRON_GOLEM);
					}
					
				},"Kaufen",coins,10000,tokens,250);
				player.openInventory(buy);
				addAnother(buy);
				}
			}
			
		}, Material.IRON_BLOCK, "IronGolem", new String[]{""}));
		
		getMain().addButton(11, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, Permission.PET_WOLF)){
					manager.AddPetOwner(player, "Wolf", EntityType.WOLF, player.getLocation());
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

						@Override
						public void onClick(Player player, ActionType type,Object object) {
							permManager.addPermission(player, Permission.PET_WOLF);
						}
						
					},"Kaufen",coins,4000,tokens,100);
					player.openInventory(buy);
					addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,95, "Wolf", new String[]{""}));
		
		getMain().addButton(12, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, Permission.PET_PIG)){
					manager.AddPetOwner(player, "Pig", EntityType.PIG, player.getLocation());
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, Permission.PET_PIG);
					}
					
					},"Kaufen",coins,4000,tokens,100);
					player.openInventory(buy);
					addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,90, "Pig", new String[]{""}));
		
		getMain().addButton(13, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, Permission.PET_SHEEP)){
					manager.AddPetOwner(player, "Schaf", EntityType.SHEEP, player.getLocation());
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, Permission.PET_SHEEP);
					}
					
					},"Kaufen",coins,4000,tokens,100);
					player.openInventory(buy);
					addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,91, "Schaf", new String[]{""}));
	}
	
	public String toString(Creature c){
		String sql = "ENTITYTYPE:"+c.getType().name()+"-/-CUSTOMNAME:"+c.getCustomName()+"-/-";
		
		if(c instanceof CraftAgeable){
			CraftAgeable ca = (CraftAgeable)c;
			sql=sql+"AGE:"+ca.getAge()+"-/-";
		}else if(c instanceof Zombie){
			Zombie ca = (Zombie)c;
			sql=sql+"AGE:"+ca.isBaby()+"-/-";
		}
		
		if(c instanceof Sheep){
			Sheep s = (Sheep)c;
			sql=sql+"SHEEP:"+s.getColor().name()+"-/-";
		}
		return sql;
	}
	
	public void DeletePetSettings(Player player){
		getPermManager().getMysql().Update("DELETE FROM list_pet WHERE player='"+player.getName().toLowerCase()+"'");
	}
	
	public void InsertPetSettings(Player player){
		if(manager.getActivePetOwners().containsKey(player.getName().toLowerCase())){
			Creature c = manager.getActivePetOwners().get(player.getName().toLowerCase());
			getPermManager().getMysql().Update("INSERT INTO list_pet (player,pet) VALUES ('"+player.getName().toLowerCase()+"','"+toString(c)+"');");
		}
	}
	
	public void loadPetSettings(String player){
		String sql = getPermManager().getMysql().getString("SELECT `pet` FROM `list_pet` WHERE player='"+player.toLowerCase()+"'");
		if(!sql.equalsIgnoreCase("null"))settings.put(player.toLowerCase(), sql);
	}
	
	public void loadPetSettings(Player player,String sql){
		if(!sql.equalsIgnoreCase("null")){
			int a = 1;
			String[] split = sql.split("-/-");
			getManager().AddPetOwner(player, split[1].split(":")[1], EntityType.valueOf( split[0].split(":")[1] ), player.getLocation());
			Creature c = getManager().getActivePetOwners().get(player.getName().toLowerCase());
			
			if(c instanceof CraftAgeable){
				CraftAgeable ca = (CraftAgeable)c;
				a=2;
				ca.setAge( Integer.valueOf( split[2].split(":")[1] ) );
				ca.setAgeLock(true);
			}else if(c instanceof Zombie){
				Zombie ca = (Zombie)c;
				a=2;
				ca.setBaby( Boolean.valueOf( split[2].split(":")[1] ) );
			}
			
			if(c instanceof Sheep){
				Sheep s = (Sheep)c;
				a++;
				s.setColor( DyeColor.valueOf( split[a].split(":")[1] ) );
			}
		}
	}
	
	public void loadPetSettings(Player player){
		String sql = getPermManager().getMysql().getString("SELECT `pet` FROM `list_pet` WHERE player='"+player.getName().toLowerCase()+"'");
		if(!sql.equalsIgnoreCase("null")){
			int a = 1;
			String[] split = sql.split("-/-");
			getManager().AddPetOwner(player, split[1].split(":")[1], EntityType.valueOf( split[0].split(":")[1] ), player.getLocation());
			Creature c = getManager().getActivePetOwners().get(player.getName().toLowerCase());
			
			if(c instanceof CraftAgeable){
				CraftAgeable ca = (CraftAgeable)c;
				a=2;
				ca.setAge( Integer.valueOf( split[2].split(":")[1] ) );
				ca.setAgeLock(true);
			}else if(c instanceof Zombie){
				Zombie ca = (Zombie)c;
				a=2;
				ca.setBaby( Boolean.valueOf( split[2].split(":")[1] ) );
			}
			
			if(c instanceof Sheep){
				Sheep s = (Sheep)c;
				a++;
				s.setColor( DyeColor.valueOf( split[a].split(":")[1] ) );
			}
		}
	}
	
	public void UpdatePetSettings(Player player){
		DeletePetSettings(player);
		InsertPetSettings(player);
	}
	
//	public void UpdatePetSettings(Player player){
//		if(manager.getActivePetOwners().containsKey(player.getName().toLowerCase())){
//			Creature c = manager.getActivePetOwners().get(player.getName().toLowerCase());
//			getPermManager().getMysql().Update("UPDATE list_pet SET pet='"+toString(c)+"' WHERE player='" + player.getName().toLowerCase() + "'");
//		}
//	}
	
	String player;
	@EventHandler
	public void Place(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC_3)return;
		for(int i = 0; i < settings.size(); i++){
			player=((String)settings.keySet().toArray()[i]);
			if(UtilPlayer.isOnline( player )){
				loadPetSettings(Bukkit.getPlayer(player), settings.get(player));
				settings.remove(player);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void Join(AsyncPlayerPreLoginEvent ev){
		loadPetSettings(ev.getName());
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		if(change_settings.contains(ev.getPlayer())){
			change_settings.remove(ev.getPlayer());
			UpdatePetSettings(ev.getPlayer());
		}
		manager.RemovePet(ev.getPlayer(), true);
	}
	
}
