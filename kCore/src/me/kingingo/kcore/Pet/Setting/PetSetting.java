package me.kingingo.kcore.Pet.Setting;

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
						c.setCustomName(event.getName().replaceAll("&", "�"));
						c.setCustomNameVisible(true);
					}
					
				}, manager.getInstance(), "Namen �ndern");
				if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
			}
		}, Material.ANVIL, "�aNamen �ndern"));
		
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
			},Material.GRASS,"�aAlter �ndern"));
		}else if(e instanceof Zombie){
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
			},Material.SKULL_ITEM,2,"�aZombie Alter �ndern"));
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
			},"Farbe �ndern",18,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)0), "Wei�"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)1), "Orange"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)2),"Magneta"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)3),"Hell Blau"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)4),"Gelb"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)5),"Hell Gr�n"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)6),"Pink"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)7),"Grau"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)9),"Cyan"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)10),"Lila"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)11),"Blau"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)12),"Braun"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)13),"Gr�n"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)14),"Rot"),UtilItem.RenameItem(new ItemStack(Material.WOOL,1,(byte)15),"Schwarz")});
			addPage(inv_choose);
			button++;
			getMain().addButton(button, new ButtonBase(new Click(){
				@Override
				public void onClick(Player player, ActionType type,Object object) {
					player.closeInventory();
					player.openInventory(inv_choose);
					if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
				}
			},Material.WOOL,"�aFarbe �ndern"));
		}
		
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
		},"Helm �ndern",9,new ItemStack[]{UtilItem.RenameItem(new ItemStack(Material.LEATHER_HELMET,1,(byte)0), "Leder Helm"),UtilItem.RenameItem(new ItemStack(Material.CHAINMAIL_HELMET,1,(byte)0), "Ketten Helm"),UtilItem.RenameItem(new ItemStack(Material.GOLD_HELMET,1,(byte)0), "Gold Helm"),UtilItem.RenameItem(new ItemStack(Material.IRON_HELMET,1,(byte)0), "Iron Helm"),UtilItem.RenameItem(new ItemStack(Material.DIAMOND_HELMET,1,(byte)0), "Diamond Helm")});
		addPage(inv_helm);
		button++;
		getMain().addButton(button, new ButtonBase(new Click(){
			@Override
			public void onClick(Player player, ActionType type,Object object) {
				player.closeInventory();
				player.openInventory(inv_helm);
				if(!manager.getShop().getChange_settings().contains(player))manager.getShop().getChange_settings().add(player);
			}
		},Material.LEATHER_HELMET,"�aHelm �ndern"));
		
		
		getMain().fill(Material.STAINED_GLASS_PANE,4);
		e.remove();
	}

}
