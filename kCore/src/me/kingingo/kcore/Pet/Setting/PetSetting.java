package me.kingingo.kcore.Pet.Setting;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryChoose;
import me.kingingo.kcore.Inventory.Inventory.InventoryRename;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Pet.PetManager;
import me.kingingo.kcore.Util.AnvilGUI.AnvilClickEvent;
import me.kingingo.kcore.Util.AnvilGUI.AnvilClickEventHandler;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftAgeable;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

public class PetSetting extends InventoryBase{
	
	public PetSetting(final PetManager manager,EntityType type,ItemStack item) {
		super(manager.getInstance(),9, "PetSetting");
		int button=3;
		getMain().setItem(1,item);
		getMain().addButton(3, new ButtonBase(new Click(){
			@Override
			public void onClick(final Player player, ActionType type,Object object) {
				player.closeInventory();
				new InventoryRename(player,new AnvilClickEventHandler(){

					@Override
					public void onAnvilClick(AnvilClickEvent event) {
						Creature c = manager.getActivePetOwners().get(player.getName().toLowerCase());
						c.setCustomName(event.getName().replaceAll("&", "§"));
						c.setCustomNameVisible(true);
					}
					
				}, manager.getInstance(), "Namen Ändern");
				if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
			}
		}, Material.ANVIL, "§aNamen Ändern"));
		
		Entity e = Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"),0,60,0), type);
		if(e instanceof CraftAgeable){
			button++;
			getMain().addButton(button, new ButtonBase(new Click(){
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
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
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
						if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					}
				}
			},"Farbe Ändern",18,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)0), "Weiß"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)1), "Orange"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)2),"Magneta"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)3),"Hell Blau"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)4),"Gelb"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)5),"Hell Grün"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)6),"Pink"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)7),"Grau"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)9),"Cyan"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)10),"Lila"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)11),"Blau"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)12),"Braun"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)13),"Grün"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)14),"Rot"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)15),"Schwarz")});
			addPage(inv_choose);
			button++;
			getMain().addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_choose);
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
				}
			},Material.WOOL,"§aFarbe Ändern"));
		}
		
		if(type==EntityType.ZOMBIE||type==EntityType.PIG_ZOMBIE){
			final InventoryChoose inv_helm=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Creature c = manager.GetPet(player);
						c.getEquipment().setHelmet( ((ItemStack)object) );
						player.closeInventory();
						if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					}
				}
			},"Helm Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.LEATHER_HELMET,1,(byte)0), "Leder Helm"),UtilItem.RenameItem(new ItemStack(Material.CHAINMAIL_HELMET,1,(byte)0), "Ketten Helm"),UtilItem.RenameItem(new ItemStack(Material.GOLD_HELMET,1,(byte)0), "Gold Helm"),UtilItem.RenameItem(new ItemStack(Material.IRON_HELMET,1,(byte)0), "Iron Helm"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_HELMET,1,(byte)0), "Diamond Helm")});
			addPage(inv_helm);
			
			final InventoryChoose inv_chest=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Creature c = manager.GetPet(player);
						c.getEquipment().setChestplate( ((ItemStack)object) );
						player.closeInventory();
						if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					}
				}
			},"Brustpanzer Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.LEATHER_CHESTPLATE,1,(byte)0), "Leder Brustpanzer"),UtilItem.RenameItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE,1,(byte)0), "Ketten Brustpanzer"),UtilItem.RenameItem(new ItemStack(Material.GOLD_CHESTPLATE,1,(byte)0), "Gold Brustpanzer"),UtilItem.RenameItem(new ItemStack(Material.IRON_CHESTPLATE,1,(byte)0), "Iron Brustpanzer"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_CHESTPLATE,1,(byte)0), "Diamond Brustpanzer")});
			addPage(inv_chest);
			
			final InventoryChoose inv_legg=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Creature c = manager.GetPet(player);
						c.getEquipment().setLeggings( ((ItemStack)object) );
						player.closeInventory();
						if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					}
				}
			},"Hose Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.LEATHER_LEGGINGS,1,(byte)0), "Leder Hose"),UtilItem.RenameItem(new ItemStack(Material.CHAINMAIL_LEGGINGS,1,(byte)0), "Ketten Hose"),UtilItem.RenameItem(new ItemStack(Material.GOLD_LEGGINGS,1,(byte)0), "Gold Hose"),UtilItem.RenameItem(new ItemStack(Material.IRON_LEGGINGS,1,(byte)0), "Iron Hose"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_LEGGINGS,1,(byte)0), "Diamond Hose")});
			addPage(inv_legg);
			
			final InventoryChoose inv_boots=new InventoryChoose(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					if(object instanceof ItemStack){
						Creature c = manager.GetPet(player);
						c.getEquipment().setBoots( ((ItemStack)object) );
						player.closeInventory();
						if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
					}
				}
			},"Schuhe Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.LEATHER_BOOTS,1,(byte)0), "Leder Schuhe"),UtilItem.RenameItem(new ItemStack(Material.CHAINMAIL_BOOTS,1,(byte)0), "Ketten Schuhe"),UtilItem.RenameItem(new ItemStack(Material.GOLD_BOOTS,1,(byte)0), "Gold Schuhe"),UtilItem.RenameItem(new ItemStack(Material.IRON_BOOTS,1,(byte)0), "Iron Schuhe"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_BOOTS,1,(byte)0), "Diamond Schuhe")});
			addPage(inv_boots);
			
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
											player.sendMessage(Text.PREFIX.getText()+"§cDas ist keine ID!");
											return;
										}
										manager.GetPet(player).getEquipment().setItemInHand( new ItemStack(m) );;
									}catch(NumberFormatException e){
										player.sendMessage(Text.PREFIX.getText()+"§cDas ist keine ID!");
									}
								}
								
							}, manager.getInstance(), "Item ID");
							if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
						}
					}
				}
			},"Equipment Ändern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.CARROT_ITEM), "Item"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_BOOTS), "Schuhe"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_LEGGINGS), "Hose"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_CHESTPLATE), "Brustpanzer"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_HELMET), "Helm")});
			addPage(inv_equip);
			
			button++;
			getMain().addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_equip);
				}
			},Material.DIAMOND_CHESTPLATE,"§aEquipment Ändern"));
			
			button++;
			getMain().addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					Zombie c =(Zombie) manager.GetPet(player);
					
					if(c.isBaby()){
						c.setBaby(false);
					}else{
						c.setBaby(true);
					}
					player.closeInventory();
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
				}
			},Material.GRASS,"§aZombie Alter Ändern"));
			
			button++;
			getMain().addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					Zombie c =(Zombie) manager.GetPet(player);
					
					if(c.isVillager()){
						c.setVillager(false);
					}else{
						c.setVillager(true);
					}
					player.closeInventory();
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
				}
			},Material.SKULL_ITEM,2,"§aZombie Type Ändern"));
		}
		
		
		getMain().fill(Material.STAINED_GLASS_PANE,4);
		e.remove();
	}

}
