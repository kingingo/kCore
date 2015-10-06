package me.kingingo.kcore.Pet.Shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryBuy;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.Buttons.SalesPackageBase;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Pet.PetManager;
import me.kingingo.kcore.Pet.Events.PetCreateEvent;
import me.kingingo.kcore.Pet.Setting.PetSetting;
import me.kingingo.kcore.Update.UpdateType;
import me.kingingo.kcore.Update.Event.UpdateEvent;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilPlayer;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftAgeable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PetShop extends InventoryPageBase implements IPetShop,Listener{

	@Getter
	private PetManager manager;
	@Getter
	private PermissionManager permManager;
	@Getter
	private ArrayList<Player> change_settings = new ArrayList<>();
	private HashMap<UUID,String> settings = new HashMap<>();

	public PetShop(final InventoryBase base,final PetManager manager,final PermissionManager permManager,final Coins coins){
		super(36,"Pet-Shop");
		Bukkit.getPluginManager().registerEvents(this, permManager.getInstance());
		this.manager=manager;
		this.permManager=permManager;
		this.manager.setShop(this);
		this.manager.setSetting(true);
		this.permManager.getMysql().Update("CREATE TABLE IF NOT EXISTS list_pet(uuid varchar(100),pet varchar(100))");
		
		this.manager.getSetting_list().put(EntityType.IRON_GOLEM, new PetSetting(manager,EntityType.IRON_GOLEM,UtilItem.RenameItem(new ItemStack(Material.IRON_BLOCK), "§aIronGolem")));
		this.manager.getSetting_list().put(EntityType.PIG, new PetSetting(manager,EntityType.PIG,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 90), "§aPig")));
		this.manager.getSetting_list().put(EntityType.WOLF, new PetSetting(manager,EntityType.WOLF,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 95), "§aWolf")));
		this.manager.getSetting_list().put(EntityType.SHEEP, new PetSetting(manager,EntityType.SHEEP,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 91), "§aSchaf")));
		this.manager.getSetting_list().put(EntityType.COW, new PetSetting(manager,EntityType.COW,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 92), "§aCow")));
		this.manager.getSetting_list().put(EntityType.ZOMBIE, new PetSetting(manager,EntityType.ZOMBIE,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 57), "§aZombie")));
		this.manager.getSetting_list().put(EntityType.OCELOT, new PetSetting(manager,EntityType.OCELOT,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 98), "§aOcelot")));
		this.manager.getSetting_list().put(EntityType.CREEPER, new PetSetting(manager,EntityType.CREEPER,UtilItem.RenameItem(new ItemStack(Material.SKULL_ITEM,1,(byte)4), "§aCreeper")));
		this.manager.getSetting_list().put(EntityType.SPIDER, new PetSetting(manager,EntityType.SPIDER,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 59), "§aSpider")));
		this.manager.getSetting_list().put(EntityType.HORSE, new PetSetting(manager,EntityType.HORSE,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 100), "§aHorse")));
		this.manager.getSetting_list().put(EntityType.RABBIT, new PetSetting(manager,EntityType.RABBIT,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 101), "§aRabbit")));
		this.manager.getSetting_list().put(EntityType.SQUID, new PetSetting(manager,EntityType.SQUID,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 94), "§aSquid")));
		this.manager.getSetting_list().put(EntityType.BLAZE, new PetSetting(manager,EntityType.BLAZE,UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte) 61), "§aBlaze")));
		
		addButton(10, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(manager.getActivePetOwners().containsKey(player.getName().toLowerCase())){
					manager.RemovePet(player, true);
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
				}
				player.closeInventory();
			}
			
		}, Material.BARRIER, "§cPet entfernen", new String[]{"§cEntfernt das momentan gesetzte Pet."}));
		
		addButton(25, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_OCELOT)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "Ocelot", EntityType.OCELOT, player.getLocation());
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, kPermission.PET_OCELOT);
					}
					
				},"Kaufen",coins,4000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,98, "§aOcelot", new String[]{"§6Kaufbares-Pet","§eCoins: 4000"}));
		
		addButton(16, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_ZOMBIE)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "Zombie", EntityType.ZOMBIE, player.getLocation());
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, kPermission.PET_ZOMBIE);
					}
					
				},"Kaufen",coins,7000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,57, "§aZombie", new String[]{"§6Kaufbares-Pet","§eCoins: 7000"}));
		
		addButton(15, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_COW)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "Cow", EntityType.COW, player.getLocation());
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, kPermission.PET_COW);
					}
					
				},"Kaufen",coins,4000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		},  Material.MONSTER_EGG,92, "§aCow", new String[]{"§6Kaufbares-Pet","§eCoins: 4000"}));
		
		addButton(14, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_IRON_GOLEM)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "IronGolem", EntityType.IRON_GOLEM, player.getLocation());
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, kPermission.PET_IRON_GOLEM);
					}
					
				},"Kaufen",coins,10000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.IRON_BLOCK, "§aIronGolem", new String[]{"§6Kaufbares-Pet","§eCoins: 10000"}));
		
		addButton(11, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_WOLF)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "Wolf", EntityType.WOLF, player.getLocation());
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

						@Override
						public void onClick(Player player, ActionType type,Object object) {
							permManager.addPermission(player, kPermission.PET_WOLF);
						}
						
					},"Kaufen",coins,4000);
					player.openInventory(buy);
					base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,95, "§aWolf", new String[]{"§6Kaufbares-Pet","§eCoins: 4000"}));
		
		addButton(12, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_PIG)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "Pig", EntityType.PIG, player.getLocation());
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, kPermission.PET_PIG);
					}
					
					},"Kaufen",coins,4000);
					player.openInventory(buy);
					base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,90, "§aPig", new String[]{"§6Kaufbares-Pet","§eCoins: 4000"}));
		
		addButton(13, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_SHEEP)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "Schaf", EntityType.SHEEP, player.getLocation());
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, kPermission.PET_SHEEP);
					}
					
					},"Kaufen",coins,4000);
					player.openInventory(buy);
					base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,91, "§aSchaf", new String[]{"§6Kaufbares-Pet","§eCoins: 4000"}));
		
		addButton(19, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_CREEPER)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "Creeper", EntityType.CREEPER, player.getLocation());
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PREMIUM_PET"));
					player.closeInventory();
				}
			}
			
		}, Material.SKULL_ITEM,4, "§aCreeper", new String[]{"§aPremium-Pet"}));
		
		addButton(20, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_SPIDER)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "Spider", EntityType.SPIDER, player.getLocation());
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, kPermission.PET_SPIDER);
					}
					
				},"Kaufen",coins,4000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,52, "§aSpider", new String[]{"§6Kaufbares-Pet","§eCoins: 4000"}));
		
		addButton(21, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_HORSE)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "Horse", EntityType.HORSE, player.getLocation());
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, kPermission.PET_HORSE);
					}
					
				},"Kaufen",coins,15000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,100, "§aHorse", new String[]{"§6Kaufbares-Pet","§eCoins: 15000"}));
		
		addButton(22, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_RABBIT)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "Rabbit", EntityType.RABBIT, player.getLocation());
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, kPermission.PET_RABBIT);
					}
					
				},"Kaufen",coins,8000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,101, "§aRabbit", new String[]{"§6Kaufbares-Pet","§eCoins: 8000"}));
		
		addButton(23, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_SQUID)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "Squid", EntityType.OCELOT, player.getLocation());
					manager.GetPet(player).setCustomName("Squid");
					((Creature)manager.GetPet(player)).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100000*20, 2));
					manager.GetPet(player).setPassenger( player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.SQUID) );
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						permManager.addPermission(player, kPermission.PET_SQUID);
					}
					
				},"Kaufen",coins,8000);
				player.openInventory(buy);
				base.addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,94, "§aSquid", new String[]{"§6Kaufbares-Pet","§eCoins: 8000"}));
		
		addButton(24, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(permManager.hasPermission(player, kPermission.PET_BLAZE)||permManager.hasPermission(player, kPermission.PET_ALL)){
					manager.AddPetOwner(player, "Blaze", EntityType.BLAZE, player.getLocation());
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					player.closeInventory();
				}
			}
			
		}, Material.MONSTER_EGG,61, "§aBlaze", new String[]{"§cNicht Kaufbar"}));
		
		fill(Material.STAINED_GLASS_PANE,(byte)7);
	}
	
	public String toString(Entity c){
		
		String sql = "ENTITYTYPE:"+(c.getPassenger()!=null&&c.getPassenger().getType()!=EntityType.PLAYER ? c.getPassenger().getType().name() : c.getType().name())+"-/-CUSTOMNAME:"+c.getCustomName()+"-/-";
		
		if(c instanceof CraftAgeable){
			CraftAgeable ca = (CraftAgeable)c;
			sql=sql+"AGE:"+ca.getAge()+"-/-";
		}else if(c instanceof Zombie){
			Zombie ca = (Zombie)c;
			sql=sql+"AGE:"+ca.isBaby()+"-/-";
			sql=sql+"VILLAGER:"+ca.isVillager()+"-/-";
		}
		
		if(c instanceof Sheep){
			Sheep s = (Sheep)c;
			sql=sql+"SHEEP:"+s.getColor().name()+"-/-";
		}else if(c instanceof Zombie||c instanceof PigZombie){
			sql=sql+"EQUIP:"+UtilInv.itemStackArrayToBase64(((Creature)c).getEquipment().getArmorContents())+"-/-";
			sql=sql+"ITEM:"+((Creature)c).getEquipment().getItemInHand().getTypeId()+"-/-";
		}else if(c instanceof Wolf){
			sql=sql+"ANGRY:"+((Wolf)c).isAngry()+"-/-";
		}else if(c instanceof Creeper){
			sql=sql+"POWERED:"+((Creeper)c).isPowered()+"-/-";
		}else if(c instanceof Slime){
			sql=sql+"SLIME_SIZE:"+((Slime)c).getSize()+"-/-";
		}else if(c instanceof Rabbit){
			sql=sql+"RABBITTYPE:"+((Rabbit)c).getRabbitType().name()+"-/-";
		}else if(c instanceof Horse){
			sql=sql+"COLOR:"+((Horse)c).getColor().name()+"-/-";
			sql=sql+"VARIANT:"+((Horse)c).getVariant().name()+"-/-";
			sql=sql+"ARMOR:"+( ((Horse)c).getInventory().getArmor()==null ? 0 : ((Horse)c).getInventory().getArmor().getTypeId() )+"-/-";
			sql=sql+"STYLE:"+((Horse)c).getStyle().name()+"-/-";
		}
		return sql;
	}
	
	public void DeletePetSettings(Player player){
		getPermManager().getMysql().Update("DELETE FROM list_pet WHERE uuid='"+UtilPlayer.getRealUUID(player)+"'");
	}
	
	public void InsertPetSettings(Player player){
		if(manager.getActivePetOwners().containsKey(player.getName().toLowerCase())){
			Entity c = manager.getActivePetOwners().get(player.getName().toLowerCase());
			getPermManager().getMysql().Update("INSERT INTO list_pet (uuid,pet) VALUES ('"+UtilPlayer.getRealUUID(player)+"','"+toString(c)+"');");
		}
	}
	
	public kPermission getPerm(EntityType type){
		switch(type){
		case CHICKEN: return kPermission.PET_CHICKEN;
		case CREEPER: return kPermission.PET_CREEPER;
		case COW: return kPermission.PET_COW;
		case IRON_GOLEM: return kPermission.PET_IRON_GOLEM;
		case OCELOT: return kPermission.PET_OCELOT;
		case ZOMBIE: return kPermission.PET_ZOMBIE;
		case WOLF: return kPermission.PET_WOLF;
		case SHEEP: return kPermission.PET_SHEEP;
		case PIG: return kPermission.PET_PIG;
		case PIG_ZOMBIE: return kPermission.PET_PIGZOMBIE;
		case SPIDER: return kPermission.PET_SPIDER;
		case SQUID: return kPermission.PET_SQUID;
		default:
			return kPermission.NONE;
		}
	}
	
	public void loadPetSettings(UUID uuid){
		String sql = getPermManager().getMysql().getString("SELECT `pet` FROM `list_pet` WHERE uuid='"+uuid+"'");
		if(!sql.equalsIgnoreCase("null"))settings.put(uuid, sql);
	}
	
	public void loadPetSettings(Player player,String sql){
		if(!sql.equalsIgnoreCase("null")){
			int a = 1;
			String[] split = sql.split("-/-");
			if(permManager.hasPermission(player, getPerm(EntityType.valueOf( split[0].split(":")[1] ))) || permManager.hasPermission(player, kPermission.PET_ALL)){
				getManager().AddPetOwner(player, split[a].split(":")[1], ( EntityType.valueOf( split[0].split(":")[1] )==EntityType.SQUID ? EntityType.OCELOT : EntityType.valueOf( split[0].split(":")[1] ) ) , player.getLocation());
				Entity c = getManager().getActivePetOwners().get(player.getName().toLowerCase());
			
			if(EntityType.valueOf( split[0].split(":")[1] ) == EntityType.SQUID){
				((Creature)manager.GetPet(player)).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100000*20, 2));
				c=player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.SQUID);
				manager.GetPet(player).setPassenger( c );
			}
			
			if(c instanceof CraftAgeable){
				CraftAgeable ca = (CraftAgeable)c;
				a++;
				if(split[a]!=null)ca.setAge( Integer.valueOf( split[a].split(":")[1] ) );
				if(split[a]!=null)ca.setAgeLock(true);
			}else if(c instanceof Zombie){
				Zombie ca = (Zombie)c;
				a++;
				if(split[a]!=null)ca.setBaby( Boolean.valueOf( split[a].split(":")[1] ) );
				a++;
				if(split[a]!=null)ca.setVillager( Boolean.valueOf( split[a].split(":")[1]) );
			}
			
			if(c instanceof Sheep){
				Sheep s = (Sheep)c;
				a++;
				if(split[a]!=null)s.setColor( DyeColor.valueOf( split[a].split(":")[1] ) );
				
			}else if(c instanceof Zombie||c instanceof PigZombie){
				a++;
				try {
					if(split[a]!=null)((Creature)c).getEquipment().setArmorContents( UtilInv.itemStackArrayFromBase64(split[a].split(":")[1]) );
				} catch (IOException e) {
					e.printStackTrace();
				}
				a++;
				try {
					if(split[a]!=null)((Creature)c).getEquipment().setItemInHand( new ItemStack( Integer.valueOf(split[a].split(":")[1]) ) );
				} catch (NumberFormatException e) {
				a--;
				}
			}else if(c instanceof Wolf){
				a++;
				if(split[a]!=null)((Wolf)c).setAngry( Boolean.valueOf( split[a].split(":")[1] ) );
			}else if(c instanceof Creeper){
				a++;
				if(split[a]!=null)((Creeper)c).setPowered( Boolean.valueOf( split[a].split(":")[1] ) );
			}else if(c instanceof Slime){
				a++;
				if(split[a]!=null)((Slime)c).setSize( Integer.valueOf(String.valueOf( split[a].split(":")[1]) ) );
			}else if(c instanceof Rabbit){
				a++;
				if(split[a]!=null)((Rabbit)c).setRabbitType( Type.valueOf( String.valueOf( split[a].split(":")[1] ) ) );
			}else if(c instanceof Horse){
				a++;
				if(split[a]!=null)((Horse)c).setColor( Horse.Color.valueOf( String.valueOf( split[a].split(":")[1] ) ) );
				a++;
				if(split[a]!=null)((Horse)c).setVariant( Variant.valueOf( String.valueOf( split[a].split(":")[1] ) ) );
				a++;
				if(split[a]!=null)((Horse)c).getInventory().setArmor( new ItemStack( Integer.valueOf(split[a].split(":")[1]) ) );
				a++;
				if(split[a]!=null)((Horse)c).setStyle( Horse.Style.valueOf( String.valueOf(split[a].split(":")[1] )) );
			}
			
			}else{
				DeletePetSettings(player);
			}
		}
	}
	
	Entity c;
	@EventHandler
	public void Ve(VehicleEnterEvent ev){
		if(ev.getVehicle() instanceof Horse&&ev.getEntered() instanceof Player){
			if(getManager().getActivePetOwners().containsKey(((Player)ev.getEntered()).getName().toLowerCase())){
				c=getManager().getActivePetOwners().get(((Player)ev.getEntered()).getName().toLowerCase());
				if(c.getEntityId()==ev.getVehicle().getEntityId()){
					return;
				}
			 }
			ev.setCancelled(true);
		}
	}
	
	public void loadPetSettings(Player player){
		String sql = getPermManager().getMysql().getString("SELECT `pet` FROM `list_pet` WHERE uuid='"+UtilPlayer.getRealUUID(player)+"'");
		loadPetSettings(player, sql);
	}
	
	public void UpdatePetSettings(Player player){
		DeletePetSettings(player);
		InsertPetSettings(player);
	}
	
	@EventHandler
	public void Create(PetCreateEvent ev){
		if(ev.getPet() instanceof Horse){
			((Horse)ev.getPet()).getInventory().setSaddle(new ItemStack(Material.SADDLE));
		}
	}
	
	UUID player;
	@EventHandler
	public void Place(UpdateEvent ev){
		if(ev.getType()!=UpdateType.SEC_3)return;
		for(int i = 0; i < settings.size(); i++){
			player=((UUID)settings.keySet().toArray()[i]);
			if(UtilPlayer.isOnline( player )){
				loadPetSettings(Bukkit.getPlayer(player), settings.get(player));
				settings.remove(player);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOW)
	public void Join(AsyncPlayerPreLoginEvent ev){
		loadPetSettings(UtilPlayer.getRealUUID(ev.getName(), ev.getUniqueId()));
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
