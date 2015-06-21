package me.kingingo.kcore.Util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class UtilItem {
	
	public static ItemStack getEnchantmentBook(Enchantment ench ,int lvl){
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		item.addUnsafeEnchantment(ench, lvl);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
		meta.addEnchant(ench, lvl, true);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack EnchantItem(ItemStack item,Enchantment ench,int lvl){
		item.addEnchantment(ench, lvl);
		return item;
	}
	
	public static ItemStack EnchantItem(ItemStack item, String... ench){
		for(String e : ench){
			int lvl = Integer.valueOf(e.split(":")[1]);
			Enchantment en = Enchantment.getByName(e.split(":")[0]);
			item.addEnchantment(en, lvl);
		}
		return item;
	}
	
	public static ItemStack[] setArmorColor(ItemStack[] armor,Color color){
		if(armor[0]==null||armor[0].getType()!=Material.LEATHER_HELMET){
			armor[0]=UtilItem.LSetColor(new ItemStack(Material.LEATHER_HELMET), color);
			armor[1]=UtilItem.LSetColor(new ItemStack(Material.LEATHER_CHESTPLATE), color);
			armor[2]=UtilItem.LSetColor(new ItemStack(Material.LEATHER_LEGGINGS), color);
			armor[3]=UtilItem.LSetColor(new ItemStack(Material.LEATHER_BOOTS), color);
		}else{
			armor[0]=UtilItem.LSetColor(armor[0], color);
			armor[1]=UtilItem.LSetColor(armor[1], color);
			armor[2]=UtilItem.LSetColor(armor[2], color);
			armor[3]=UtilItem.LSetColor(armor[3], color);
		}
		return armor;
	}
	
	public static Enchantment[] enchantmentsBoots(){
		return new Enchantment[]{Enchantment.DURABILITY,Enchantment.PROTECTION_ENVIRONMENTAL,Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_FIRE,Enchantment.THORNS};
	}
	
	public static Enchantment[] enchantmentsLeggings(){
		return new Enchantment[]{Enchantment.DURABILITY,Enchantment.PROTECTION_ENVIRONMENTAL,Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_FIRE,Enchantment.THORNS};
	}
	
	public static Enchantment[] enchantmentsChestplate(){
		return new Enchantment[]{Enchantment.DURABILITY,Enchantment.PROTECTION_ENVIRONMENTAL,Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_FIRE,Enchantment.THORNS};
	}
	
	public static Enchantment[] enchantmentsHelm(){
		return new Enchantment[]{Enchantment.DURABILITY,Enchantment.PROTECTION_ENVIRONMENTAL,Enchantment.PROTECTION_EXPLOSIONS,Enchantment.PROTECTION_PROJECTILE,Enchantment.PROTECTION_FIRE,Enchantment.WATER_WORKER,Enchantment.THORNS};
	}
	
	public static Enchantment[] enchantmentsAxt(){
		return new Enchantment[]{Enchantment.DAMAGE_ALL,Enchantment.DAMAGE_ARTHROPODS,Enchantment.DAMAGE_UNDEAD,Enchantment.SILK_TOUCH,Enchantment.DIG_SPEED};
	}
	
	public static Enchantment[] enchantmentsSword(){
		return new Enchantment[]{Enchantment.DAMAGE_ALL,Enchantment.DAMAGE_ARTHROPODS,Enchantment.KNOCKBACK,Enchantment.FIRE_ASPECT,Enchantment.LOOT_BONUS_MOBS,Enchantment.DAMAGE_UNDEAD};
	}
	
	public static Enchantment[] enchantmentsBow(){
		return new Enchantment[]{Enchantment.ARROW_DAMAGE,Enchantment.ARROW_FIRE,Enchantment.ARROW_INFINITE,Enchantment.ARROW_KNOCKBACK};
	}
	
	public static ItemStack[] colorRunArmor(ItemStack[] armor,Color[] colors){
		//WENN KEINE ARMOR GESETZT IST!
		if(armor[0]==null||armor[0].getType()!=Material.LEATHER_HELMET)setArmorColor(armor,colors[0]);
		
		for(int i = 0; i<armor.length; i++){
			//SUCHT DAS LETZTE RÜSTUNGSTEIL
			if( ((LeatherArmorMeta)armor[i].getItemMeta()).getColor().equals(colors[0]) ){
				//SETZT DIE AMOR WIEDER AUF COLOR[0]
				setArmorColor(armor,colors[0]);
				//SETZT DAS NÄCHSTE AMOR TEIL AUF COLOR[1]
				armor[ (i==3 ? 0 : (i+1)) ]=LSetColor(armor[ (i==3 ? 0 : (i+1)) ], colors[1] );
				break;
			}
		}
		colors[0]=colors[1];
		//COLOR[1] == SETZT DEN NÄCHSTEN COLOR CODE EIN!
		colors[1]=nextColor( colors[0] );
		return armor;
	}
	
	public static Material rdmWerkzeug(){
		switch(UtilMath.r(5)){
		case 0: return rdmAxt();
		case 1: return rdmSchwert();
		case 2: return rdmHoe();
		case 3: return rdmSpitzhacke();
		case 4: return rdmSchaufel();
		default: return null;
		}
	}
	
	public static Material rdmHoe(){
		switch(UtilMath.r(5)){
		case 0: return Material.IRON_HOE;
		case 1: return Material.DIAMOND_HOE;
		case 2: return Material.WOOD_HOE;
		case 3: return Material.GOLD_HOE;
		case 4: return Material.STONE_HOE;
		default: return null;
		}
	}
	
	public static Material rdmSpitzhacke(){
		switch(UtilMath.r(5)){
		case 0: return Material.IRON_PICKAXE;
		case 1: return Material.DIAMOND_PICKAXE;
		case 2: return Material.WOOD_PICKAXE;
		case 3: return Material.GOLD_PICKAXE;
		case 4: return Material.STONE_PICKAXE;
		default: return null;
		}
	}
	
	public static Material rdmSchaufel(){
		switch(UtilMath.r(5)){
		case 0: return Material.IRON_SPADE;
		case 1: return Material.DIAMOND_SPADE;
		case 2: return Material.WOOD_SPADE;
		case 3: return Material.GOLD_SPADE;
		case 4: return Material.STONE_SPADE;
		default: return null;
		}
	}
	
	public static Material rdmAxt(){
		switch(UtilMath.r(5)){
		case 0: return Material.IRON_AXE;
		case 1: return Material.DIAMOND_AXE;
		case 2: return Material.WOOD_AXE;
		case 3: return Material.GOLD_AXE;
		case 4: return Material.STONE_AXE;
		default: return null;
		}
	}
	
	public static Material rdmSchwert(){
		switch(UtilMath.r(5)){
		case 0: return Material.IRON_SWORD;
		case 1: return Material.DIAMOND_SWORD;
		case 2: return Material.WOOD_SWORD;
		case 3: return Material.GOLD_SWORD;
		case 4: return Material.STONE_SWORD;
		default: return null;
		}
	}
	
	public static boolean isPickaxe(ItemStack item){
		return (item.getType()==Material.IRON_PICKAXE||item.getType()==Material.WOOD_PICKAXE||item.getType()==Material.DIAMOND_PICKAXE ||item.getType()==Material.GOLD_PICKAXE ||item.getType()==Material.STONE_PICKAXE);
	}
	
	public static boolean isAxt(ItemStack item){
		return (item.getType()==Material.IRON_AXE||item.getType()==Material.WOOD_AXE||item.getType()==Material.DIAMOND_AXE ||item.getType()==Material.GOLD_AXE ||item.getType()==Material.STONE_AXE);
	}
	
	public static boolean isSword(ItemStack item){
		return (item.getType()==Material.IRON_SWORD||item.getType()==Material.STONE_SWORD||item.getType()==Material.DIAMOND_SWORD ||item.getType()==Material.WOOD_SWORD ||item.getType()==Material.GOLD_SWORD);
	}
	
	public static boolean isWeapon(ItemStack item){
		if(item.getTypeId()<=294&&item.getTypeId()>=290){
			return true;
		}else if(item.getTypeId()<=286&&item.getTypeId()>=283){
			return true;
		}else if(item.getTypeId()<=279&&item.getTypeId()>=267){
			return true;
		}else if(item.getTypeId()>=256&&item.getTypeId()<=258){
			return true;
		}else if(item.getType()==Material.BOW){
			return true;
		}
		return false;
	}
	
	public static boolean isLeatherArmor(ItemStack item){
		return (item.getTypeId()>=298&&item.getTypeId()<=301);
	}
	
	public static boolean isChainmailArmor(ItemStack item){
		return (item.getTypeId()>=302&&item.getTypeId()<=305);
	}
	
	public static boolean isIronArmor(ItemStack item){
		return (item.getTypeId()>=306&&item.getTypeId()<=309);
	}
	
	public static boolean isDiamondArmor(ItemStack item){
		return (item.getTypeId()>=310&&item.getTypeId()<=313);
	}
	
	public static boolean isGoldArmor(ItemStack item){
		return (item.getTypeId()>=314&&item.getTypeId()<=317);
	}
	
	public static boolean isChestplate(ItemStack item){
		return (item.getType()==Material.LEATHER_CHESTPLATE||item.getType()==Material.DIAMOND_CHESTPLATE ||item.getType()==Material.GOLD_CHESTPLATE ||item.getType()==Material.IRON_CHESTPLATE);
	}
	
	public static boolean isBoots(ItemStack item){
		return (item.getType()==Material.LEATHER_BOOTS||item.getType()==Material.DIAMOND_BOOTS ||item.getType()==Material.GOLD_BOOTS ||item.getType()==Material.IRON_BOOTS);
	}
	
	public static boolean isLeggings(ItemStack item){
		return (item.getType()==Material.LEATHER_LEGGINGS||item.getType()==Material.DIAMOND_LEGGINGS ||item.getType()==Material.GOLD_LEGGINGS ||item.getType()==Material.IRON_LEGGINGS);
	}
	
	public static boolean isHelm(ItemStack item){
		return (item.getType()==Material.LEATHER_HELMET||item.getType()==Material.DIAMOND_HELMET ||item.getType()==Material.GOLD_HELMET ||item.getType()==Material.IRON_HELMET);
	}
	
	public static boolean isArmor(ItemStack item){
		if(item.getTypeId()>=298&&item.getTypeId()<=317){
			return true;
		}
		return false;
	}
	
	public static ItemStack[] convertItemStackArray(net.minecraft.server.v1_8_R3.ItemStack[] item){
		ItemStack[] items = new ItemStack[item.length];
		for(int i = 0; i<item.length;i++){
			items[i]=CraftItemStack.asBukkitCopy(item[i]);
		}
		return items;
	}
	
	public static net.minecraft.server.v1_8_R3.ItemStack[] convertItemStackArray(ItemStack[] item){
		net.minecraft.server.v1_8_R3.ItemStack[] items = new net.minecraft.server.v1_8_R3.ItemStack[item.length];
		for(int i = 0; i<item.length;i++){
			items[i]=CraftItemStack.asNMSCopy(item[i]);
		}
		return items;
	}
	
	public static org.bukkit.inventory.ItemStack SetDescriptions(org.bukkit.inventory.ItemStack i, List<String> msg){
	    ItemMeta im = i.getItemMeta();
	    im.setLore(msg);
	    i.setItemMeta(im);
	    return i;
	  }

	  public static org.bukkit.inventory.ItemStack SetDescriptions(org.bukkit.inventory.ItemStack i, String[] msg) {
	    List msg1 = new ArrayList();
	    for (String m : msg) {
	      msg1.add(m);
	    }
	    ItemMeta im = i.getItemMeta();
	    im.setLore(msg1);
	    i.setItemMeta(im);
	    return i;
	  }
	  
	  public static ItemStack addEnchantmentGlow(ItemStack item){ 
	        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
	        NBTTagCompound tag = null;
	        if (!nmsStack.hasTag()) {
	            tag = new NBTTagCompound();
	            nmsStack.setTag(tag);
	        }
	        if (tag == null) tag = nmsStack.getTag();
	        NBTTagList ench = new NBTTagList();
	        tag.set("ench", ench);
	        nmsStack.setTag(tag);
	        return CraftItemStack.asCraftMirror(nmsStack);
	    }
	  
	  public static boolean ItemNameEquals(ItemStack i, ItemStack i1){
		  if(i==null||i1==null)return false;
		  if(i.hasItemMeta()&&i.getItemMeta().hasDisplayName()){
			  if(i1.hasItemMeta()&&i1.getItemMeta().hasDisplayName()){
				  if(i.getItemMeta().getDisplayName().equalsIgnoreCase(i1.getItemMeta().getDisplayName())){
					  return true;
				  }else{
					  return false;
				  }
			  }else{
				  return false;
			  }
		  }else{
			  return false;
		  }
	  }

	  public static org.bukkit.inventory.ItemStack Item(org.bukkit.inventory.ItemStack i, List<String> msg, String msg1)
	  {
	    i = RenameItem(i, msg1);

	    i = SetDescriptions(i, msg);

	    return i;
	  }

	  public static org.bukkit.inventory.ItemStack Item(org.bukkit.inventory.ItemStack i, String[] msg, String msg1){
	    i = RenameItem(i, msg1);

	    i = SetDescriptions(i, msg);

	    return i;
	  }

	  public static org.bukkit.inventory.ItemStack removeAttributes(org.bukkit.inventory.ItemStack i)
	  {
	    if (i == null) {
	      return i;
	    }
	    if (i.getType() == Material.BOOK_AND_QUILL) {
	      return i;
	    }
	    org.bukkit.inventory.ItemStack item = i.clone();
	    net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
	    NBTTagCompound tag = null;
	    if (!nmsStack.hasTag())
	    {
	      NBTTagCompound tag2 = new NBTTagCompound();
	      nmsStack.setTag(tag2);
	    }
	    else
	    {
	      tag = nmsStack.getTag();
	    }
	    NBTTagList am = new NBTTagList();
	    tag.set("AttributeModifiers", am);
	    nmsStack.setTag(tag);
	    return CraftItemStack.asCraftMirror(nmsStack);
	  }
	  
	  public static ItemStack Head(String player){
		   	ItemStack is = new ItemStack(Material.SKULL_ITEM, 1);
		    is.setDurability((short)3);
		    SkullMeta meta = (SkullMeta)is.getItemMeta();
		    meta.setOwner(player);
		    is.setItemMeta(meta);
		    return is;
	  }

	  public static boolean RepairItem(ItemStack item){
		  if(item!=null&&item.getType()!=Material.AIR){
			  try {
				  if (item.getDurability() != 0){
					  if(item.getType().getMaxDurability() < 1||item.getType().isBlock())return false;
					  item.setDurability((short)0);
					  return true;
				  }
			  }catch (Exception i) {
		           return false;
		      }
	           return false;
		  }
          return false;
	  }
	  
	  public static org.bukkit.inventory.ItemStack RenameItem(org.bukkit.inventory.ItemStack i, String msg)
	  {
	    ItemMeta im = i.getItemMeta();

	    im.setDisplayName(msg);

	    i.setItemMeta(im);

	    return i;
	  }

	  public static ItemStack[] rainbowArmor(ItemStack[] armor){
		if(armor[0]==null||armor[0].getType()!=Material.LEATHER_HELMET)setArmorColor(armor,Color.RED);
		
		Color c=nextColor( ((LeatherArmorMeta)armor[0].getItemMeta()).getColor() );
		for(int i = 0; i<armor.length; i++)armor[i]=LSetColor(armor[i], c);
		return armor;
	  }
	  
	  public static Color nextColor(Color c) {
	    return Color.fromBGR(mixColor(c.getBlue()),mixColor(c.getGreen()),mixColor(c.getRed()));
	  }

	  private static boolean rIncrease = Math.random() > 0.5;
	  private static int mixColor(int r){
		  if (rIncrease) {
		      r += (int) (Math.random() * 20);
		      if (r >= 255) {
		        rIncrease = false;
		        r = 255;
		      }
		    } else {
		      r -= (int) (Math.random() * 20);
		      if (r <= 0) {
		        rIncrease = true;
		        r = 0;
		      }
		    }
		  return r;
	  }
	  
	  public static org.bukkit.inventory.ItemStack LSetColor(org.bukkit.inventory.ItemStack i, DyeColor c) {
		  return LSetColor(i, c.getColor());
	  }
	  
	  public static org.bukkit.inventory.ItemStack LSetColor(org.bukkit.inventory.ItemStack i, Color c) {
	    LeatherArmorMeta lam = (LeatherArmorMeta)i.getItemMeta();
	    lam.setColor(c);
	    i.setItemMeta(lam);
	    return i;
	  }
	
	  public static DyeColor getColorDye(ItemStack wool){
		  if(wool.getDurability()==0)return DyeColor.WHITE;
		  if(wool.getDurability()==1)return DyeColor.ORANGE;
		  if(wool.getDurability()==2)return DyeColor.MAGENTA;
		  if(wool.getDurability()==3)return DyeColor.LIGHT_BLUE;
		  if(wool.getDurability()==4)return DyeColor.YELLOW;
		  if(wool.getDurability()==5)return DyeColor.LIME;
		  if(wool.getDurability()==6)return DyeColor.PINK;
		  if(wool.getDurability()==7)return DyeColor.GRAY;
		  if(wool.getDurability()==8)return DyeColor.GRAY;
		  if(wool.getDurability()==9)return DyeColor.CYAN;
		  if(wool.getDurability()==10)return DyeColor.PURPLE;
		  if(wool.getDurability()==11)return DyeColor.BLUE;
		  if(wool.getDurability()==12)return DyeColor.BROWN;
		  if(wool.getDurability()==13)return DyeColor.GREEN;
		  if(wool.getDurability()==14)return DyeColor.RED;
		  if(wool.getDurability()==15)return DyeColor.BLACK;
		  return null;
	  }
	  
}
