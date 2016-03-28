package eu.epicpvp.kcore.Pet.Shop;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryBuy;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.SalesPackageBase;
import eu.epicpvp.kcore.Language.Language;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import lombok.Getter;

public class PetShop extends InventoryPageBase{

	@Getter
	private PlayerPetHandler handle;
	@Getter
	private StatsManager money;
	
	public PetShop(PlayerPetHandler handle,StatsManager money) {
		super(InventorySize._45.getSize(),"Pet-Shop");
		this.handle=handle;
		this.money=money;
		
		addButton(10, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getHandle().getManager().getActivePetOwners().containsKey(player.getName().toLowerCase())){
					getHandle().getManager().RemovePet(player, true);
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
				}
				player.closeInventory();
			}
			
		}, Material.BARRIER, "§cPet entfernen", new String[]{"§cEntfernt das momentan gesetzte Pet."}));
		
		addButton(11, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_WOLF.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Wolf", EntityType.WOLF, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

						@Override
						public void onClick(Player player, ActionType type,Object object) {
							getHandle().getPermManager().addPermission(player, PermissionType.PET_WOLF);
						}
						
					},"Kaufen",money,1000,4000);
					player.openInventory(buy);
					getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,95, "§aWolf", new String[]{"§6Kaufbares-Pet","§eCoins: 4000","§aGems: 1000"}));
		
		addButton(12, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_PIG.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Pig", EntityType.PIG, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, PermissionType.PET_PIG);
					}
					
					},"Kaufen",money,1000,4000);
					player.openInventory(buy);
					getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,90, "§aPig", new String[]{"§6Kaufbares-Pet","§eCoins: 4000","§aGems: 1000"}));
		
		addButton(13, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_SHEEP.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Schaf", EntityType.SHEEP, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, PermissionType.PET_SHEEP);
					}
					
					},"Kaufen",money,1000,4000);
					player.openInventory(buy);
					getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,91, "§aSchaf", new String[]{"§6Kaufbares-Pet","§eCoins: 4000","§aGems: 1000"}));
		
		addButton(14, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_IRON_GOLEM.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "IronGolem", EntityType.IRON_GOLEM, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, PermissionType.PET_IRON_GOLEM);
					}
					
				},"Kaufen",money,2500,10000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.IRON_BLOCK, "§aIronGolem", new String[]{"§6Kaufbares-Pet","§eCoins: 10000","§aGems: 2500"}));
		
		addButton(15, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if( player.hasPermission(PermissionType.PET_COW.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Cow", EntityType.COW, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, PermissionType.PET_COW);
					}
					
				},"Kaufen",money,1000,4000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		},  Material.MONSTER_EGG,92, "§aCow", new String[]{"§6Kaufbares-Pet","§eCoins: 4000","§aGems: 1000"}));
		
		addButton(16, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if( player.hasPermission(PermissionType.PET_ZOMBIE.getPermissionToString()) ||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Zombie", EntityType.ZOMBIE, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, PermissionType.PET_ZOMBIE);
					}
					
				},"Kaufen",money,2000,7000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,57, "§aZombie", new String[]{"§6Kaufbares-Pet","§eCoins: 7000","§aGems: 2000"}));
		
		addButton(19, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_SPIDER.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Spider", EntityType.SPIDER, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, PermissionType.PET_SPIDER);
					}
					
				},"Kaufen",money,1000,4000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,52, "§aSpider", new String[]{"§6Kaufbares-Pet","§eCoins: 4000","§aGems: 1000"}));
		
		addButton(20, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_HORSE.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Horse", EntityType.HORSE, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, PermissionType.PET_HORSE);
					}
					
				},"Kaufen",money,4000,15000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,100, "§aHorse", new String[]{"§6Kaufbares-Pet","§eCoins: 15000","§aGems: 4000"}));
		
		addButton(21, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_RABBIT.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Rabbit", EntityType.RABBIT, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, PermissionType.PET_RABBIT);
					}
					
				},"Kaufen",money,2000,8000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,101, "§aRabbit", new String[]{"§6Kaufbares-Pet","§eCoins: 8000","§aGems: 2000"}));
		
		addButton(22, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_SQUID.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Squid", EntityType.OCELOT, player.getLocation());
					getHandle().getManager().GetPet(player).setCustomName("Squid");
					((Creature)getHandle().getManager().GetPet(player)).addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100000*20, 2));
					getHandle().getManager().GetPet(player).setPassenger( player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.SQUID) );
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, PermissionType.PET_SQUID);
					}
					
				},"Kaufen",money,2000,8000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,94, "§aSquid", new String[]{"§6Kaufbares-Pet","§eCoins: 8000","§aGems: 2000"}));
		
		addButton(23, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if( player.hasPermission(PermissionType.PET_OCELOT.getPermissionToString())|| player.hasPermission(PermissionType.PET_ALL.getPermissionToString()) ){
					getHandle().getManager().AddPetOwner(player, "Ocelot", EntityType.OCELOT, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, PermissionType.PET_OCELOT);
					}
					
				},"Kaufen",money,1000,4000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,98, "§aOcelot", new String[]{"§6Kaufbares-Pet","§eCoins: 4000","§aGems: 1000"}));
		
		addButton(24, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_CREEPER.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Creeper", EntityType.CREEPER, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "BUY_RANK"));
					player.closeInventory();
				}
			}
			
		}, Material.SKULL_ITEM,4, "§aCreeper", new String[]{"§eVip Pet"}));
		
		addButton(25, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_VILLAGER.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Villager", EntityType.VILLAGER, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "BUY_RANK"));
					player.closeInventory();
				}
			}
			
		}, Material.MONSTER_EGG,120, "§aVillager", new String[]{"§6Ultra Pet"}));
		
		addButton(28, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_BLAZE.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Blaze", EntityType.BLAZE, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "BUY_RANK"));
					player.closeInventory();
				}
			}
			
		}, Material.MONSTER_EGG,61, "§aBlaze", new String[]{"§5Legend Pet"}));
		
		addButton(29, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_SKELETON.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Skeleton", EntityType.SKELETON, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "BUY_RANK"));
					player.closeInventory();
				}
			}
			
		}, Material.MONSTER_EGG,51, "§aSkeleton", new String[]{"§3MVP Pet"}));
		
		addButton(30, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_WITCH.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Witch", EntityType.WITCH, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "BUY_RANK"));
					player.closeInventory();
				}
			}
			
		}, Material.MONSTER_EGG,66, "§aWitch", new String[]{"§9MVP+ Pet"}));
		
		addButton(31, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(PermissionType.PET_SNOWMAN.getPermissionToString())||player.hasPermission(PermissionType.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Snowman", EntityType.SNOWMAN, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PET_SEPZIAL_SHOP"));
					player.closeInventory();
				}
			}
			
		}, Material.CARROT_ITEM, "§aSnowman", new String[]{"§cXMAS Pet"}));
		
		fill(Material.STAINED_GLASS_PANE,(byte)7);
		getHandle().getBase().addPage(this);
	}

	

}
