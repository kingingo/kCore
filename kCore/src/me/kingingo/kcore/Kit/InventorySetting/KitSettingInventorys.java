package me.kingingo.kcore.Kit.InventorySetting;

import java.util.HashMap;

import lombok.Getter;
import me.kingingo.kcore.Inventory.InventoryBase;
import me.kingingo.kcore.Inventory.InventoryPageBase;
import me.kingingo.kcore.Inventory.Inventory.InventoryChoose;
import me.kingingo.kcore.Inventory.Inventory.InventorySortChoose;
import me.kingingo.kcore.Inventory.Item.ButtonBase;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.IButton;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Versus.VersusKit;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class KitSettingInventorys extends InventoryBase{

	@Getter
	private JavaPlugin instance;
	@Getter
	private HashMap<Player,InventoryPageBase> inventorys;
	private HashMap<Player,Integer> enchant_list;
	private InventorySortChoose helm_choose;
	private int helm_place=4;
	private InventorySortChoose chestplate_choose;
	private int chestplate_place=13;
	private InventorySortChoose leggings_choose;
	private int leggings_place=22;
	private InventorySortChoose boots_choose;
	private int boots_place=31;
	private InventoryChoose main_item_inventory1;
	private InventoryChoose main_item_inventory2;
	private InventoryChoose enchant;
	private int main_place_1 = 12;
	private int main_place_2=11;
	private InventoryChoose second_item1;
	private InventoryChoose second_item2;
	private InventoryChoose potion;
	private int second_place_1=14;
	private int second_place_2=15;
	private int potion_place=16;
	private StatsManager statsManager;
	private InventoryBase base;
	
	public KitSettingInventorys(JavaPlugin instance,StatsManager statsManager){
		super(instance,"KitSettingInventorys");
		this.instance=instance;
		this.statsManager=statsManager;
		this.enchant_list=new HashMap<>();
		this.base=new InventoryBase(instance, "Kits");
		this.inventorys=new HashMap<>();
		this.helm_choose=new InventorySortChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object obj) {
				if(inventorys.containsKey(player)){
					inventorys.get(player).getButton(helm_place).setItemStack( ((ItemStack)obj).clone() );
					player.openInventory( inventorys.get(player) );
				}
			}
			
		}, "Wähle einen Helm", InventorySize._9.getSize(), new ItemStack[]{new ItemStack(Material.LEATHER_HELMET),new ItemStack(Material.IRON_HELMET),new ItemStack(Material.GOLD_HELMET),new ItemStack(Material.DIAMOND_HELMET)});
		this.helm_choose.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inventorys.get(player));
			}
			
		},UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14), "§cZurück")));
		this.base.addPage(helm_choose);
		
		this.chestplate_choose=new InventorySortChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object obj) {
				if(inventorys.containsKey(player)){
					inventorys.get(player).getButton(chestplate_place).setItemStack( ((ItemStack)obj).clone() );
					player.openInventory( inventorys.get(player) );
				}
			}
			
		}, "Wähle ein Brustpanzer", InventorySize._9.getSize(), new ItemStack[]{new ItemStack(Material.LEATHER_CHESTPLATE),new ItemStack(Material.IRON_CHESTPLATE),new ItemStack(Material.GOLD_CHESTPLATE),new ItemStack(Material.DIAMOND_CHESTPLATE)});
		this.chestplate_choose.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inventorys.get(player));
			}
			
		},UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14), "§cZurück")));
		this.base.addPage(chestplate_choose);
		this.leggings_choose=new InventorySortChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object obj) {
				if(inventorys.containsKey(player)){
					inventorys.get(player).getButton(leggings_place).setItemStack( ((ItemStack)obj).clone() );
					player.openInventory( inventorys.get(player) );
				}
			}
			
		}, "Wähle eine Hose", InventorySize._9.getSize(), new ItemStack[]{new ItemStack(Material.LEATHER_LEGGINGS),new ItemStack(Material.IRON_LEGGINGS),new ItemStack(Material.GOLD_LEGGINGS),new ItemStack(Material.DIAMOND_LEGGINGS)});
		this.leggings_choose.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inventorys.get(player));
			}
			
		},UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14), "§cZurück")));
		this.base.addPage(leggings_choose);
		this.boots_choose=new InventorySortChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object obj) {
				if(inventorys.containsKey(player)){
					inventorys.get(player).getButton(boots_place).setItemStack( ((ItemStack)obj).clone() );
					player.openInventory( inventorys.get(player) );
				}
			}
			
		}, "Wähle deine Schuhe", InventorySize._9.getSize(), new ItemStack[]{new ItemStack(Material.LEATHER_BOOTS),new ItemStack(Material.IRON_BOOTS),new ItemStack(Material.GOLD_BOOTS),new ItemStack(Material.DIAMOND_BOOTS)});
		this.boots_choose.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inventorys.get(player));
			}
			
		},UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14), "§cZurück")));
		this.base.addPage(boots_choose);
		this.main_item_inventory1=new InventoryChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object obj) {
				if(inventorys.containsKey(player)){
					inventorys.get(player).getButton(main_place_1).setItemStack( ((ItemStack)obj).clone() );
					player.openInventory( inventorys.get(player) );
				}
			}
			
		},9, "Wähle ein Main Item", InventorySize._27.getSize(), new ItemStack[]{new ItemStack(Material.BOW),new ItemStack(Material.DIAMOND_SWORD),new ItemStack(Material.STONE_SWORD),new ItemStack(Material.IRON_SWORD),new ItemStack(Material.GOLD_SWORD),new ItemStack(Material.WOOD_SWORD),new ItemStack(Material.DIAMOND_AXE),new ItemStack(Material.STONE_AXE),new ItemStack(Material.IRON_AXE),new ItemStack(Material.GOLD_AXE),new ItemStack(Material.WOOD_AXE)});
		this.main_item_inventory1.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inventorys.get(player));
			}
			
		},UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14), "§cZurück")));
		this.base.addPage(main_item_inventory1);
		this.main_item_inventory2=new InventoryChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object obj) {
				if(inventorys.containsKey(player)){
					inventorys.get(player).getButton(main_place_2).setItemStack( ((ItemStack)obj).clone() );
					player.openInventory( inventorys.get(player) );
				}
			}
			
		},9, "Wähle ein Main Item", InventorySize._27.getSize(), new ItemStack[]{new ItemStack(Material.BOW),new ItemStack(Material.DIAMOND_SWORD),new ItemStack(Material.STONE_SWORD),new ItemStack(Material.IRON_SWORD),new ItemStack(Material.GOLD_SWORD),new ItemStack(Material.WOOD_SWORD),new ItemStack(Material.DIAMOND_AXE),new ItemStack(Material.STONE_AXE),new ItemStack(Material.IRON_AXE),new ItemStack(Material.GOLD_AXE),new ItemStack(Material.WOOD_AXE)});
		main_item_inventory2.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inventorys.get(player));
			}
			
		},UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14), "§cZurück")));
		this.base.addPage(main_item_inventory2);
		second_item1=new InventoryChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object obj) {
				if(inventorys.containsKey(player)){
					inventorys.get(player).getButton(second_place_1).setItemStack( ((ItemStack)obj).clone() );
					player.openInventory( inventorys.get(player) );
				}
			}
			
		},9, "Wähle ein Second Item", InventorySize._18.getSize(), new ItemStack[]{new ItemStack(Material.IRON_PICKAXE),new ItemStack(Material.DIAMOND_PICKAXE),new ItemStack(Material.ARROW,32),new ItemStack(Material.FLINT_AND_STEEL),new ItemStack(Material.WEB),new ItemStack(Material.STONE,16),new ItemStack(Material.ENDER_PEARL),new ItemStack(Material.GOLDEN_APPLE,8),new ItemStack(Material.GOLDEN_APPLE,8,(short)1)});
		second_item1.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inventorys.get(player));
			}
			
		},UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14), "§cZurück")));
		this.base.addPage(second_item1);
		second_item2=new InventoryChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object obj) {
				if(inventorys.containsKey(player)){
					inventorys.get(player).getButton(second_place_2).setItemStack( ((ItemStack)obj).clone() );
					player.openInventory( inventorys.get(player) );
				}
			}
			
		},9, "Wähle ein Second Item", InventorySize._18.getSize(), new ItemStack[]{new ItemStack(Material.IRON_PICKAXE),new ItemStack(Material.DIAMOND_PICKAXE),new ItemStack(Material.ARROW,32),new ItemStack(Material.FLINT_AND_STEEL),new ItemStack(Material.WEB),new ItemStack(Material.STONE,16),new ItemStack(Material.ENDER_PEARL),new ItemStack(Material.GOLDEN_APPLE,8),new ItemStack(Material.GOLDEN_APPLE,8,(short)1)});
		second_item2.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inventorys.get(player));
			}
			
		},UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14), "§cZurück")));
		this.base.addPage(second_item2);
		ItemStack[] i = loadPotions();
		potion=new InventoryChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object obj) {
				if(inventorys.containsKey(player)){
					inventorys.get(player).getButton(potion_place).setItemStack( ((ItemStack)obj).clone() );
					player.openInventory( inventorys.get(player) );
				}
			}
			
		},(45-i.length), "Wähle ein Potion Item", InventorySize._45.getSize(),i);
		potion.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inventorys.get(player));
			}
			
		},UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14), "§cZurück")));
		this.base.addPage(potion);
		this.enchant=new InventoryChoose(new Click(){

			@Override
			public void onClick(Player player, ActionType a, Object object) {
				player.openInventory( inventorys.get(player) );
			}
			
		}, "Enchanten", InventorySize._45.getSize(), loadEnchant());
		enchant.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.openInventory(inventorys.get(player));
			}
			
		},UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)14), "§cZurück")));
		this.base.addPage(enchant);
	}

	public ItemStack[] loadEnchant(){
		ItemStack[] items = new ItemStack[45];
		int i = 1;
		for(Enchantment ench : Enchantment.values()){
			for(int lvl = ench.getStartLevel(); lvl < ench.getMaxLevel();lvl++){
				items[i]=UtilItem.RenameItem(new ItemStack(Material.ENCHANTED_BOOK,lvl), ench.getName());
				i++;
				if(i==44)break;
			}

			if(i==44)break;
		}
		
		return items;
	}
	
	public ItemStack[] loadPotions(){
		ItemStack[] items = new ItemStack[]{
				new ItemStack(Material.POTION,3,(short)8193),
				new ItemStack(Material.POTION,3,(short)8194),
				new ItemStack(Material.POTION,3,(short)8195),
				new ItemStack(Material.POTION,3,(short)8197),
				new ItemStack(Material.POTION,3,(short)8201),
				new ItemStack(Material.POTION,3,(short)8225),
				new ItemStack(Material.POTION,3,(short)8226),
				new ItemStack(Material.POTION,3,(short)8229),
				new ItemStack(Material.POTION,3,(short)8233),
				new ItemStack(Material.POTION,3,(short)8257),
				new ItemStack(Material.POTION,3,(short)8258),
				new ItemStack(Material.POTION,3,(short)8259),
				new ItemStack(Material.POTION,3,(short)8265),
				new ItemStack(Material.POTION,3,(short)8289),
				new ItemStack(Material.POTION,3,(short)8290),
				new ItemStack(Material.POTION,3,(short)8297), //16
				
				new ItemStack(Material.POTION,3,(short)16385),
				new ItemStack(Material.POTION,3,(short)16387),
				new ItemStack(Material.POTION,3,(short)16389),
				new ItemStack(Material.POTION,3,(short)16393),
				new ItemStack(Material.POTION,3,(short)16417),
				new ItemStack(Material.POTION,3,(short)16418),
				new ItemStack(Material.POTION,3,(short)16421),
				new ItemStack(Material.POTION,3,(short)16425),
				new ItemStack(Material.POTION,3,(short)16449),
				new ItemStack(Material.POTION,3,(short)16450),
				new ItemStack(Material.POTION,3,(short)16451),
				new ItemStack(Material.POTION,3,(short)16457),
				new ItemStack(Material.POTION,3,(short)16481),
				new ItemStack(Material.POTION,3,(short)16482),
				new ItemStack(Material.POTION,3,(short)16489),
				new ItemStack(Material.POTION,3,(short)16484),
				new ItemStack(Material.POTION,3,(short)16458),
				new ItemStack(Material.POTION,3,(short)16456),
				new ItemStack(Material.POTION,3,(short)16452),
				new ItemStack(Material.POTION,3,(short)16428),
				new ItemStack(Material.POTION,3,(short)16420),
				new ItemStack(Material.POTION,3,(short)16396),
				new ItemStack(Material.POTION,3,(short)16394),
				new ItemStack(Material.POTION,3,(short)16392),
				new ItemStack(Material.POTION,3,(short)16388),//25
		};
		return items;
	}
	
	public void addKitInventory(Player player){
		if(inventorys.containsKey(player)){
			player.openInventory(inventorys.get(player));
			return;
		}
	}
	
	@EventHandler
	public void Quit(PlayerQuitEvent ev){
		if(inventorys.containsKey(ev.getPlayer())){
			inventorys.get(ev.getPlayer()).clear();
			for(IButton b : inventorys.get(ev.getPlayer()).getButtons())b.remove();
			inventorys.get(ev.getPlayer()).getButtons().clear();
		}
	}
	
	public void addKitInventory(Player player,final VersusKit kit){
		if(inventorys.containsKey(player)){
			player.openInventory(inventorys.get(player));
			return;
		}
		final InventoryPageBase page = new InventoryPageBase(InventorySize._36.getSize(), player.getName()+"s Kit:");
		page.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				kit.helm=page.getItem(helm_place);
				kit.chestplate=page.getItem(chestplate_place);
				kit.leggings=page.getItem(leggings_place);
				kit.boots=page.getItem(boots_place);
				kit.inv[0]=page.getItem(main_place_1);
				kit.inv[1]=page.getItem(main_place_2);
				kit.inv[2]=page.getItem(second_place_1);
				kit.inv[3]=page.getItem(second_place_2);
				kit.inv[4]=page.getItem(potion_place);
				statsManager.setString(player, UtilInv.itemStackArrayToBase64(kit.toItemArray()), Stats.KIT);
//				statsManager.SaveAllPlayerData(player);
				player.closeInventory();
			}
			
		}, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)5), "§aKit Speichern")));
		page.addButton(helm_place, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(helm_choose);
				}
			}
			
		}, kit.helm));
		page.addButton(chestplate_place, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(chestplate_choose);
				}
			}
			
		}, kit.chestplate));
		page.addButton(leggings_place, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(leggings_choose);
				}
			}
			
		}, kit.leggings));
		page.addButton(boots_place, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(boots_choose);
				}
			}
			
		}, kit.boots));
		page.addButton(main_place_1, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(main_item_inventory1);
				}
			}
			
		}, (kit.inv[0]!=null ? kit.inv[0] : UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)6), " "))));
		page.addButton(main_place_2, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(main_item_inventory2);
				}
			}
			
		}, (kit.inv[1]!=null ? kit.inv[1] : UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)6), " "))));
		page.addButton(second_place_1, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(second_item1);
				}
			}
			
		}, (kit.inv[2]!=null ? kit.inv[2] : UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)6), " "))));
		page.addButton(second_place_2, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(second_item2);
				}
			}
			
		}, (kit.inv[3]!=null ? kit.inv[3] : UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)6), " "))));
		page.addButton(potion_place, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(inventorys.containsKey(player)){
					player.openInventory(potion);
				}
			}
			
		}, (kit.inv[4]!=null ? kit.inv[4] : UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(short)6), " "))));
		page.fill(Material.STAINED_GLASS_PANE, 15);
		base.addPage(page);
		player.openInventory(page);
		inventorys.put(player, page);
	}
}
