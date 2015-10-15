package me.kingingo.kcore.Pet.Shop;

import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryBuy;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.Buttons.SalesPackageBase;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.Gems;
import me.kingingo.kcore.Util.UtilEvent.ActionType;

import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PetShop extends InventoryPageBase{

	@Getter
	private PlayerPetHandler handle;
	@Getter
	private Coins coins;
	@Getter
	private Gems gems;
	
	public PetShop(PlayerPetHandler handle,Gems gems,Coins coins) {
		super(36,"Pet-Shop");
		this.handle=handle;
		this.coins=coins;
		this.gems=gems;
		
		addButton(10, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(getHandle().getManager().getActivePetOwners().containsKey(player.getName().toLowerCase())){
					getHandle().getManager().RemovePet(player, true);
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
				}
				player.closeInventory();
			}
			
		}, Material.BARRIER, "§cPet entfernen", new String[]{"§cEntfernt das momentan gesetzte Pet."}));
		
		addButton(25, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if( player.hasPermission(kPermission.PET_OCELOT.getPermissionToString())|| player.hasPermission(kPermission.PET_ALL.getPermissionToString()) ){
					getHandle().getManager().AddPetOwner(player, "Ocelot", EntityType.OCELOT, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, kPermission.PET_OCELOT);
					}
					
				},"Kaufen",coins,4000,gems,1000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,98, "§aOcelot", new String[]{"§6Kaufbares-Pet","§eCoins: 4000","§aGems: 1000"}));
		
		addButton(16, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if( player.hasPermission(kPermission.PET_ZOMBIE.getPermissionToString()) ||player.hasPermission(kPermission.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Zombie", EntityType.ZOMBIE, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, kPermission.PET_ZOMBIE);
					}
					
				},"Kaufen",coins,7000,gems,2000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,57, "§aZombie", new String[]{"§6Kaufbares-Pet","§eCoins: 7000","§aGems: 2000"}));
		
		addButton(15, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if( player.hasPermission(kPermission.PET_COW.getPermissionToString())||player.hasPermission(kPermission.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Cow", EntityType.COW, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, kPermission.PET_COW);
					}
					
				},"Kaufen",coins,4000,gems,1000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		},  Material.MONSTER_EGG,92, "§aCow", new String[]{"§6Kaufbares-Pet","§eCoins: 4000","§aGems: 1000"}));
		
		addButton(14, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(kPermission.PET_IRON_GOLEM.getPermissionToString())||player.hasPermission(kPermission.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "IronGolem", EntityType.IRON_GOLEM, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, kPermission.PET_IRON_GOLEM);
					}
					
				},"Kaufen",coins,10000,gems,2500);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.IRON_BLOCK, "§aIronGolem", new String[]{"§6Kaufbares-Pet","§eCoins: 10000","§aGems: 2500"}));
		
		addButton(11, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(kPermission.PET_WOLF.getPermissionToString())||player.hasPermission(kPermission.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Wolf", EntityType.WOLF, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

						@Override
						public void onClick(Player player, ActionType type,Object object) {
							getHandle().getPermManager().addPermission(player, kPermission.PET_WOLF);
						}
						
					},"Kaufen",coins,4000,gems,1000);
					player.openInventory(buy);
					getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,95, "§aWolf", new String[]{"§6Kaufbares-Pet","§eCoins: 4000","§aGems: 1000"}));
		
		addButton(12, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(kPermission.PET_PIG.getPermissionToString())||player.hasPermission(kPermission.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Pig", EntityType.PIG, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, kPermission.PET_PIG);
					}
					
					},"Kaufen",coins,4000,gems,1000);
					player.openInventory(buy);
					getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,90, "§aPig", new String[]{"§6Kaufbares-Pet","§eCoins: 4000","§aGems: 1000"}));
		
		addButton(13, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(kPermission.PET_SHEEP.getPermissionToString())||player.hasPermission(kPermission.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Schaf", EntityType.SHEEP, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){

					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, kPermission.PET_SHEEP);
					}
					
					},"Kaufen",coins,4000,gems,1000);
					player.openInventory(buy);
					getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,91, "§aSchaf", new String[]{"§6Kaufbares-Pet","§eCoins: 4000","§aGems: 1000"}));
		
		addButton(19, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(kPermission.PET_CREEPER.getPermissionToString())||player.hasPermission(kPermission.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Creeper", EntityType.CREEPER, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "PREMIUM_PET"));
					player.closeInventory();
				}
			}
			
		}, Material.SKULL_ITEM,4, "§aCreeper", new String[]{"§aPremium-Pet"}));
		
		addButton(20, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(kPermission.PET_SPIDER.getPermissionToString())||player.hasPermission(kPermission.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Spider", EntityType.SPIDER, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, kPermission.PET_SPIDER);
					}
					
				},"Kaufen",coins,4000,gems,1000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,52, "§aSpider", new String[]{"§6Kaufbares-Pet","§eCoins: 4000","§aGems: 1000"}));
		
		addButton(21, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(kPermission.PET_HORSE.getPermissionToString())||player.hasPermission(kPermission.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Horse", EntityType.HORSE, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, kPermission.PET_HORSE);
					}
					
				},"Kaufen",coins,15000,gems,4000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,100, "§aHorse", new String[]{"§6Kaufbares-Pet","§eCoins: 15000","§aGems: 4000"}));
		
		addButton(22, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(kPermission.PET_RABBIT.getPermissionToString())||player.hasPermission(kPermission.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Rabbit", EntityType.RABBIT, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}else{
					InventoryBuy buy = new InventoryBuy(new Click(){
					@Override
					public void onClick(Player player, ActionType type,Object object) {
						getHandle().getPermManager().addPermission(player, kPermission.PET_RABBIT);
					}
					
				},"Kaufen",coins,8000,gems,2000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,101, "§aRabbit", new String[]{"§6Kaufbares-Pet","§eCoins: 8000","§aGems: 2000"}));
		
		addButton(23, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(kPermission.PET_SQUID.getPermissionToString())||player.hasPermission(kPermission.PET_ALL.getPermissionToString())){
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
						getHandle().getPermManager().addPermission(player, kPermission.PET_SQUID);
					}
					
				},"Kaufen",coins,8000,gems,2000);
				player.openInventory(buy);
				getHandle().getBase().addAnother(buy);
				}
			}
			
		}, Material.MONSTER_EGG,94, "§aSquid", new String[]{"§6Kaufbares-Pet","§eCoins: 8000","§aGems: 2000"}));
		
		addButton(24, new SalesPackageBase(new Click(){
			public void onClick(Player player, ActionType type,Object object) {
				if(player.hasPermission(kPermission.PET_BLAZE.getPermissionToString())||player.hasPermission(kPermission.PET_ALL.getPermissionToString())){
					getHandle().getManager().AddPetOwner(player, "Blaze", EntityType.BLAZE, player.getLocation());
					if(!getHandle().getChange_settings().contains(player))getHandle().getChange_settings().add(player);
					player.closeInventory();
				}
			}
			
		}, Material.MONSTER_EGG,61, "§aBlaze", new String[]{"§cNicht Kaufbar"}));
		
		fill(Material.STAINED_GLASS_PANE,(byte)7);
		getHandle().getBase().addPage(this);
	}

	

}
