package me.kingingo.kcore.Util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_7_R3.NBTTagCompound;
import net.minecraft.server.v1_7_R3.NBTTagList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class UtilItem {

	public static org.bukkit.inventory.ItemStack SetDescriptions(org.bukkit.inventory.ItemStack i, List<String> msg)
	  {
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

	  public static org.bukkit.inventory.ItemStack Item(org.bukkit.inventory.ItemStack i, List<String> msg, String msg1)
	  {
	    i = RenameItem(i, msg1);

	    i = SetDescriptions(i, msg);

	    return i;
	  }

	  public static org.bukkit.inventory.ItemStack Item(org.bukkit.inventory.ItemStack i, String[] msg, String msg1)
	  {
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
	    net.minecraft.server.v1_7_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
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
	
}
