package me.kingingo.kcore.Pet.Setting;

import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryChoose;
import me.kingingo.kcore.Inventory.Inventory.InventoryRename;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.Buttons.ButtonBase;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Pet.PetManager;
import me.kingingo.kcore.Util.AnvilGUI.AnvilClickEvent;
import me.kingingo.kcore.Util.AnvilGUI.AnvilClickEventHandler;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftAgeable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public class PetSetting extends InventoryPageBase{
	
	public PetSetting(InventoryBase base,final PetManager manager,EntityType type,ItemStack item) {
		super(InventorySize._9, "");
		int button=3;
		setItem(1,item);
		addButton(3, new ButtonBase(new Click(){
			@Override
			public void onClick(final Player player, ActionType type,Object object) {
				player.closeInventory();
				new InventoryRename(player,new AnvilClickEventHandler(){

					@Override
					public void onAnvilClick(AnvilClickEvent event) {
						Entity c = manager.getActivePetOwners().get(player.getName().toLowerCase());
						c.setCustomName(event.getName().replaceAll("&", "§"));
						c.setCustomNameVisible(true);
					}
					
				}, manager.getInstance(), "Namen Ändern");
				if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
			}
		}, Material.ANVIL, "§aNamen Ändern"));
		
		Entity e = Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"),0,60,0), type);
		if(e instanceof CraftAgeable){
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					CraftAgeable c =(CraftAgeable) manager.GetPet(player);
					c.setAgeLock(true);
					if(c.isAdult()){
						c.setBaby();
					}else{
						c.setAdult();
					}
					player.closeInventory();
					if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
				}
			},Material.GRASS,"§aAlter Ändern"));
		}
		
		if(type==EntityType.SHEEP){
			final InventoryChoose inv_choose=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Sheep sh = (Sheep)manager.GetPet(player);
						sh.setColor(UtilItem.getColorDye( ((ItemStack)object) ));	
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Farbe Ändern",18,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)0), "Weiß"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)1), "Orange"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)2),"Magneta"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)3),"Hell Blau"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)4),"Gelb"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)5),"Hell Grün"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)6),"Pink"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)7),"Grau"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)9),"Cyan"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)10),"Lila"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)11),"Blau"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)12),"Braun"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)13),"Grün"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)14),"Rot"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)15),"Schwarz")});
			base.addPage(inv_choose);
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_choose);
					if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
				}
			},Material.WOOL,"§aFarbe Ändern"));
		}else if(type==EntityType.SLIME||type==EntityType.MAGMA_CUBE){
			
			final InventoryChoose inv_typ=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Slime c = (Slime)manager.GetPet(player);
						c.setSize( Integer.valueOf(((ItemStack)object).getItemMeta().getDisplayName()) );
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Size Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.SLIME_BALL), "0"),UtilItem.RenameItem(new ItemStack(Material.SLIME_BALL), "1"),UtilItem.RenameItem(new ItemStack(Material.SLIME_BALL), "2"),UtilItem.RenameItem(new ItemStack(Material.SLIME_BALL), "3")});
			base.addPage(inv_typ);
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_typ);
				}
			},Material.HAY_BLOCK,"Size Ändern"));
			
		}else if(type==EntityType.GUARDIAN){
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					Guardian w = (Guardian)manager.GetPet(player);
					player.closeInventory();
					if(w.isElder()){
						w.setElder(false);
					}else{
						w.setElder(true);
					}
					if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
				}
			},Material.FISHING_ROD,"§aElder An/Aus"));
		}else if(type==EntityType.CREEPER){
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					Creeper w = (Creeper)manager.GetPet(player);
					player.closeInventory();
					if(w.isPowered()){
						w.setPowered(false);
					}else{
						w.setPowered(true);
					}
					if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
				}
			},Material.NETHER_STAR,"§aPowered An/Aus"));
		}else if(type==EntityType.HORSE){
			final InventoryChoose inv_armor=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Horse c = (Horse)manager.GetPet(player);
						c.getInventory().setArmor( (((ItemStack)object).getItemMeta().getDisplayName().equalsIgnoreCase("Keine Armor") ? null : ((ItemStack)object)) );
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Armor Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(417,1), "Iron Armor"),UtilItem.RenameItem(new ItemStack(418,1), "Gold Armor"),UtilItem.RenameItem(new ItemStack(419,1), "Diamond Armor"),UtilItem.RenameItem(new ItemStack(Material.ARMOR_STAND), "Keine Armor")});
			base.addPage(inv_armor);
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_armor);
				}
			},Material.IRON_BARDING,"Armor Ändern"));
			
			final InventoryChoose inv_color=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Horse c = (Horse)manager.GetPet(player);
						c.setColor( Horse.Color.valueOf( ((ItemStack)object).getItemMeta().getDisplayName() ) );
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Color Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)1), "CHESTNUT"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)12), "BROWN"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)8), "CREAMY"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)12), "DARK_BROWN"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1), "WHITE"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)15), "BLACK"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)7), "GRAY")});
			base.addPage(inv_color);
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_color);
				}
			},Material.WOOL,"Color Ändern"));
			
			final InventoryChoose inv_typ=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Horse c = (Horse)manager.GetPet(player);
						c.setVariant( Variant.valueOf( ((ItemStack)object).getItemMeta().getDisplayName() ) );
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Type Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.BONE,1), "UNDEAD_HORSE"),UtilItem.RenameItem(new ItemStack(Material.BONE,1), "SKELETON_HORSE"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)100), "MULE"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)100), "HORSE"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)100), "DONKEY")});
			base.addPage(inv_typ);
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_typ);
				}
			},Material.HAY_BLOCK,"Type Ändern"));
			
			final InventoryChoose inv_style=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Horse c = (Horse)manager.GetPet(player);
						c.setStyle( Horse.Style.valueOf( ((ItemStack)object).getItemMeta().getDisplayName() ) );
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Style Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)100), "NONE"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)100), "WHITEFIELD"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)100), "WHITE_DOTS"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)100), "BLACK_DOTS"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)100), "WHITE")});
			base.addPage(inv_style);
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_style);
				}
			},Material.CARROT_STICK,"Style Ändern"));
			
		}else if(type==EntityType.RABBIT){
			final InventoryChoose inv_chest=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Rabbit c = (Rabbit)manager.GetPet(player);
						c.setRabbitType( Type.valueOf( ((ItemStack)object).getItemMeta().getDisplayName() ) );
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Rabbit Type Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)101), "WHITE"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)101), "THE_KILLER_BUNNY"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)101), "SALT_AND_PEPPER"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)101), "GOLD"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)101), "BROWN"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)101), "BLACK_AND_WHITE"),UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)101), "BLACK")});
			base.addPage(inv_chest);
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_chest);
				}
			},Material.RABBIT,"§aRabbit Type"));
		}else if(type==EntityType.VILLAGER){
			final InventoryChoose inv_chest=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Villager c = (Villager)manager.GetPet(player);
						c.setProfession( Profession.valueOf( ((ItemStack)object).getItemMeta().getDisplayName() ) );
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Profession Type Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(351,1), Profession.BLACKSMITH.name()),UtilItem.RenameItem(new ItemStack(351,1,(byte)1), Profession.BUTCHER.name()),UtilItem.RenameItem(new ItemStack(351,1,(byte)7), Profession.FARMER.name()),UtilItem.RenameItem(new ItemStack(351,1,(byte)6), Profession.LIBRARIAN.name()),UtilItem.RenameItem(new ItemStack(351,1,(byte)5), Profession.PRIEST.name())});
			base.addPage(inv_chest);
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_chest);
				}
			},Material.BONE,"§aProfession Type"));
		}else if(type==EntityType.SKELETON){
			final InventoryChoose inv_chest=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Skeleton c = (Skeleton)manager.GetPet(player);
						c.setSkeletonType( SkeletonType.valueOf( ((ItemStack)object).getItemMeta().getDisplayName() ) );
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Skeleton Type Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.SKULL_ITEM,1,(byte)SkullType.SKELETON.ordinal()), SkeletonType.NORMAL.name()),UtilItem.RenameItem(new ItemStack(Material.SKULL_ITEM,1,(byte)SkullType.WITHER.ordinal()), SkeletonType.WITHER.name())});
			base.addPage(inv_chest);
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_chest);
				}
			},Material.BONE,"§aSkeleton Type"));
		}else if(type==EntityType.WOLF){
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					Wolf w = (Wolf)manager.GetPet(player);
					player.closeInventory();
					if(w.isAngry()){
						w.setAngry(false);
					}else{
						w.setAngry(true);
					}
					if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
				}
			},Material.CARROT_STICK,"§aAngry An/Aus"));
		}else if(type==EntityType.ZOMBIE||type==EntityType.PIG_ZOMBIE){
			final InventoryChoose inv_helm=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Creature c = (Creature)manager.GetPet(player);
						c.getEquipment().setHelmet( ((ItemStack)object) );
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Helm Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.LEATHER_HELMET,1,(byte)0), "Leder Helm"),UtilItem.RenameItem(new ItemStack(Material.CHAINMAIL_HELMET,1,(byte)0), "Ketten Helm"),UtilItem.RenameItem(new ItemStack(Material.GOLD_HELMET,1,(byte)0), "Gold Helm"),UtilItem.RenameItem(new ItemStack(Material.IRON_HELMET,1,(byte)0), "Iron Helm"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_HELMET,1,(byte)0), "Diamond Helm")});
			base.addPage(inv_helm);
			
			final InventoryChoose inv_chest=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Creature c = (Creature)manager.GetPet(player);
						c.getEquipment().setChestplate( ((ItemStack)object) );
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Brustpanzer Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.LEATHER_CHESTPLATE,1,(byte)0), "Leder Brustpanzer"),UtilItem.RenameItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE,1,(byte)0), "Ketten Brustpanzer"),UtilItem.RenameItem(new ItemStack(Material.GOLD_CHESTPLATE,1,(byte)0), "Gold Brustpanzer"),UtilItem.RenameItem(new ItemStack(Material.IRON_CHESTPLATE,1,(byte)0), "Iron Brustpanzer"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_CHESTPLATE,1,(byte)0), "Diamond Brustpanzer")});
			base.addPage(inv_chest);
			
			final InventoryChoose inv_legg=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Creature c = (Creature)manager.GetPet(player);
						c.getEquipment().setLeggings( ((ItemStack)object) );
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Hose Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.LEATHER_LEGGINGS,1,(byte)0), "Leder Hose"),UtilItem.RenameItem(new ItemStack(Material.CHAINMAIL_LEGGINGS,1,(byte)0), "Ketten Hose"),UtilItem.RenameItem(new ItemStack(Material.GOLD_LEGGINGS,1,(byte)0), "Gold Hose"),UtilItem.RenameItem(new ItemStack(Material.IRON_LEGGINGS,1,(byte)0), "Iron Hose"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_LEGGINGS,1,(byte)0), "Diamond Hose")});
			base.addPage(inv_legg);
			
			final InventoryChoose inv_boots=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Creature c = (Creature)manager.GetPet(player);
						c.getEquipment().setBoots( ((ItemStack)object) );
						player.closeInventory();
						if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
					}
				}
			},"Schuhe Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.LEATHER_BOOTS,1,(byte)0), "Leder Schuhe"),UtilItem.RenameItem(new ItemStack(Material.CHAINMAIL_BOOTS,1,(byte)0), "Ketten Schuhe"),UtilItem.RenameItem(new ItemStack(Material.GOLD_BOOTS,1,(byte)0), "Gold Schuhe"),UtilItem.RenameItem(new ItemStack(Material.IRON_BOOTS,1,(byte)0), "Iron Schuhe"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_BOOTS,1,(byte)0), "Diamond Schuhe")});
			base.addPage(inv_boots);
			
			final InventoryChoose inv_equip=new InventoryChoose(new Click(){
				@Override
				public void onClick(final Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						player.closeInventory();
						if( ((ItemStack)object).getType() == Material.DIAMOND_HELMET ){
							player.openInventory(inv_helm);
						}else if( ((ItemStack)object).getType() == Material.DIAMOND_CHESTPLATE ){
							player.openInventory(inv_chest);
						}else if( ((ItemStack)object).getType() == Material.DIAMOND_LEGGINGS ){
							player.openInventory(inv_legg);
						}else if( ((ItemStack)object).getType() == Material.DIAMOND_BOOTS ){
							player.openInventory(inv_boots);
						}else if( ((ItemStack)object).getType() == Material.CARROT_ITEM ){
							new InventoryRename(player,new AnvilClickEventHandler(){

								@Override
								public void onAnvilClick(AnvilClickEvent event) {
									try{
										Material m = Material.getMaterial(Integer.valueOf(event.getName()));
										if(m==null){
											player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NOT_ENTITY_ID"));
											return;
										}
										((Creature)manager.GetPet(player)).getEquipment().setItemInHand( new ItemStack(m) );;
									}catch(NumberFormatException e){
										player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NOT_ENTITY_ID"));
									}
								}
								
							}, manager.getInstance(), "Item ID");
							if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
						}
					}
				}
			},"Equipment Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.CARROT_ITEM), "Item"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_BOOTS), "Schuhe"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_LEGGINGS), "Hose"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_CHESTPLATE), "Brustpanzer"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_HELMET), "Helm")});
			base.addPage(inv_equip);
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_equip);
				}
			},Material.DIAMOND_CHESTPLATE,"§aEquipment Ändern"));
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					Zombie c =(Zombie) manager.GetPet(player);
					
					if(c.isBaby()){
						c.setBaby(false);
					}else{
						c.setBaby(true);
					}
					player.closeInventory();
					if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
				}
			},Material.GRASS,"§aZombie Alter Ändern"));
			
			button++;
			addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					Zombie c =(Zombie) manager.GetPet(player);
					
					if(c.isVillager()){
						c.setVillager(false);
					}else{
						c.setVillager(true);
					}
					player.closeInventory();
					if(!manager.getHandler().getChange_settings().contains(player))manager.getHandler().getChange_settings().add(player);
				}
			},Material.SKULL_ITEM,2,"§aZombie Type Ändern"));
		}
		
		
		fill(Material.STAINED_GLASS_PANE,4);
		base.addPage(this);
		e.remove();
	}

}
