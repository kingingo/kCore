package me.kingingo.kcore.Util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_8_R2.NBTTagCompound;
import net.minecraft.server.v1_8_R2.NBTTagList;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class UtilItem {
	
	public static ItemStack EnchantItem(ItemStack item, String... ench){
		for(String e : ench){
			int lvl = Integer.valueOf(e.split(":")[1]);
			Enchantment en = Enchantment.getByName(e.split(":")[0]);
			item.addEnchantment(en, lvl);
		}
		return item;
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
	
	public static boolean isArmor(ItemStack item){
		if(item.getTypeId()>=298&&item.getTypeId()<=317){
			return true;
		}
		return false;
	}
	
	public static ItemStack[] convertItemStackArray(net.minecraft.server.v1_8_R2.ItemStack[] item){
		ItemStack[] items = new ItemStack[item.length];
		for(int i = 0; i<item.length;i++){
			items[i]=CraftItemStack.asBukkitCopy(item[i]);
		}
		return items;
	}
	
	public static net.minecraft.server.v1_8_R2.ItemStack[] convertItemStackArray(ItemStack[] item){
		net.minecraft.server.v1_8_R2.ItemStack[] items = new net.minecraft.server.v1_8_R2.ItemStack[item.length];
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
	        net.minecraft.server.v1_8_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
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
	    net.minecraft.server.v1_8_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
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
