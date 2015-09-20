package me.kingingo.kcore.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import me.kingingo.kcore.Enum.ServerType;
import me.kingingo.kcore.Inventory.Inventory.InventoryLotto2.InventoryLotto2Type;
import me.kingingo.kcore.Inventory.Item.BooleanClick;
import me.kingingo.kcore.Inventory.Item.Click;
import me.kingingo.kcore.Inventory.Item.LottoPackage;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Permission.kPermission;
import me.kingingo.kcore.StatsManager.Stats;
import me.kingingo.kcore.StatsManager.StatsManager;
import me.kingingo.kcore.TreasureChest.StandingTreasureChest.TreasureChestPackage;
import me.kingingo.kcore.TreasureChest.StandingTreasureChest.TreasureChestType;
import me.kingingo.kcore.Util.UtilEvent.ActionType;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;

public class UtilItem {
	
	private static HashMap<TreasureChestType,ArrayList<TreasureChestPackage>> ItemList;
	private static final Map<String, DyeColor> colorMap = new HashMap();
	private static final Map<String, FireworkEffect.Type> fireworkShape = new HashMap();
	private static HashMap<InventoryLotto2Type,ArrayList<LottoPackage>> lottoList;
	
	public static ItemStack getEnchantmentBook(Enchantment ench ,int lvl){
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		item.addUnsafeEnchantment(ench, lvl);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
		meta.addEnchant(ench, lvl, true);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack parseStringMeta(ItemStack item, int fromArg,String[] string) throws Exception{
		Pattern splitPattern = Pattern.compile("[:+',;.]");
		for (int i = fromArg; i < string.length; i++){
			item=addStringMeta(item,splitPattern,string[i]);
	    }
		return item;
	}
	
	public static ItemStack addStringMeta(ItemStack item, Pattern splitPattern, String string) throws Exception{
	    String[] split = splitPattern.split(string, 2);
	    
	    if ((split.length > 1) && (split[0].equalsIgnoreCase("name"))){
	      String displayName = split[1].replaceAll("_", " ").replaceAll("&", "§");
	      ItemMeta meta = item.getItemMeta();
	      meta.setDisplayName(displayName);
	      item.setItemMeta(meta);
	    }else if ((split.length > 1) && ((split[0].equalsIgnoreCase("lore")) || (split[0].equalsIgnoreCase("desc")))){
	      List lore = new ArrayList();
	      for (String line : split[1].split("\\|"))
	      {
	        lore.add(line.replaceAll("_", " ").replaceAll("&", "§"));
	      }
	      ItemMeta meta = item.getItemMeta();
	      meta.setLore(lore);
	      item.setItemMeta(meta);
	    }else if ((split.length > 1) && (split[0].equalsIgnoreCase("power")) && (item.getType() == Material.FIREWORK)){
	      int power = UtilNumber.isInt(split[1]) ? Integer.parseInt(split[1]) : 0;
	      FireworkMeta meta = (FireworkMeta)item.getItemMeta();
	      meta.setPower(power > 3 ? 4 : power);
	      item.setItemMeta(meta);
	    } else if (item.getType() == Material.FIREWORK) {
	      item=addFireworkMeta(true,splitPattern, string, item);
	    }else if ((split.length > 1) && ((split[0].equalsIgnoreCase("player")) || (split[0].equalsIgnoreCase("owner"))) && (item.getType() == Material.SKULL_ITEM)){
	        if (item.getDurability() == 3)
	        {
	          String owner = split[1];
	          SkullMeta meta = (SkullMeta)item.getItemMeta();
	          meta.setOwner(owner);
	          item.setItemMeta(meta);
	        }
	        else
	        {
	          throw new Exception(Language.getText("onlyPlayerSkulls"));
	        }
	      }else{
	    	item=parseEnchantmentStrings(item,true,split);
	    }
		return item;
	}
	
	private static ItemStack parseEnchantmentStrings(ItemStack stack,boolean allowUnsafe, String[] split) throws Exception{
	    Enchantment enchantment = Enchantments.getByName(split[0]);
	    if ((enchantment == null))
	    {
	      return stack;
	    }

	    int level = -1;
	    if (split.length > 1)
	    {
	      try
	      {
	        level = Integer.parseInt(split[1]);
	      }
	      catch (NumberFormatException ex)
	      {
	        level = -1;
	      }
	    }

	    if ((level < 0) || ((!allowUnsafe) && (level > enchantment.getMaxLevel())))
	    {
	      level = enchantment.getMaxLevel();
	    }
	    stack=addEnchantment(stack, allowUnsafe, enchantment, level);
	    return stack;
	  }
	
	public static ItemStack addEnchantment(ItemStack stack,boolean allowUnsafe, Enchantment enchantment, int level) throws Exception
	  {
	    if (enchantment == null)
	    {
	      throw new Exception(Language.getText("enchantmentNotFound"));
	    }
	    try
	    {
	      if (stack.getType().equals(Material.ENCHANTED_BOOK))
	      {
	        EnchantmentStorageMeta meta = (EnchantmentStorageMeta)stack.getItemMeta();
	        if (level == 0)
	        {
	          meta.removeStoredEnchant(enchantment);
	        }
	        else
	        {
	          meta.addStoredEnchant(enchantment, level, allowUnsafe);
	        }
	        stack.setItemMeta(meta);
	      }
	      else if (level == 0)
	      {
	        stack.removeEnchantment(enchantment);
	      }
	      else if (allowUnsafe)
	      {
	        stack.addUnsafeEnchantment(enchantment, level);
	      }
	      else
	      {
	        stack.addEnchantment(enchantment, level);
	      }

	    }
	    catch (Exception ex)
	    {
	      throw new Exception("Enchantment " + enchantment.getName() + ": " + ex.getMessage(), ex);
	    }
	    
	    return stack;
	  }
	
	public static ItemStack addFireworkMeta(boolean allowShortName,Pattern splitPattern, String string,ItemStack stack) throws Exception{
		if(colorMap.isEmpty()&&fireworkShape.isEmpty()){
			for (DyeColor color : DyeColor.values())
		    {
		      colorMap.put(color.name(), color);
		    }
		    for (FireworkEffect.Type type : FireworkEffect.Type.values())
		    {
		      fireworkShape.put(type.name(), type);
		    }
		}
		
	    if (stack.getType() == Material.FIREWORK)
	    {
	      FireworkEffect.Builder builder = FireworkEffect.builder();
	      String[] split = splitPattern.split(string, 2);

	      if (split.length < 2)
	      {
	        return stack;
	      }

	      if ((split[0].equalsIgnoreCase("color")) || (split[0].equalsIgnoreCase("colour")) || ((allowShortName) && (split[0].equalsIgnoreCase("c")))){
	          FireworkEffect effect = builder.build();
	          FireworkMeta fmeta = (FireworkMeta)stack.getItemMeta();
	          fmeta.addEffect(effect);
	          stack.setItemMeta(fmeta);
	          builder = FireworkEffect.builder();
	        

	        List primaryColors = new ArrayList();
	        String[] colors = split[1].split(",");
	        for (String color : colors)
	        {
	          if (colorMap.containsKey(color.toUpperCase())){
	            primaryColors.add(((DyeColor)colorMap.get(color.toUpperCase())).getFireworkColor());
	          }else{
	            throw new Exception(Language.getText("invalidFireworkFormat"));
	          }
	        }
	        builder.withColor(primaryColors);
	      }
	      else if ((split[0].equalsIgnoreCase("shape")) || (split[0].equalsIgnoreCase("type")) || ((allowShortName) && ((split[0].equalsIgnoreCase("s")) || (split[0].equalsIgnoreCase("t")))))
	      {
	        FireworkEffect.Type finalEffect = null;
	        split[1] = (split[1].equalsIgnoreCase("large") ? "BALL_LARGE" : split[1]);
	        if (fireworkShape.containsKey(split[1].toUpperCase()))
	        {
	          finalEffect = (FireworkEffect.Type)fireworkShape.get(split[1].toUpperCase());
	        }else{
	            throw new Exception(Language.getText("invalidFireworkFormat"));
	        }
	        if (finalEffect != null)
	        {
	          builder.with(finalEffect);
	        }
	      }
	      else if ((split[0].equalsIgnoreCase("fade")) || ((allowShortName) && (split[0].equalsIgnoreCase("f"))))
	      {
	        List fadeColors = new ArrayList();
	        String[] colors = split[1].split(",");
	        for (String color : colors)
	        {
	          if (colorMap.containsKey(color.toUpperCase())){
	            fadeColors.add(((DyeColor)colorMap.get(color.toUpperCase())).getFireworkColor());
	          }else{
	        	  throw new Exception(Language.getText("invalidFireworkFormat"));
	          }
	        }
	        if (!fadeColors.isEmpty())
	        {
	          builder.withFade(fadeColors);
	        }
	      }
	      else if ((split[0].equalsIgnoreCase("effect")) || ((allowShortName) && (split[0].equalsIgnoreCase("e"))))
	      {
	        String[] effects = split[1].split(",");
	        for (String effect : effects)
	        {
	          if (effect.equalsIgnoreCase("twinkle"))
	          {
	            builder.flicker(true);
	          }
	          else if (effect.equalsIgnoreCase("trail"))
	          {
	            builder.trail(true);
	          }else{
	        	  throw new Exception(Language.getText("invalidFireworkFormat"));
	          }
	        }
	      }
	    }
	    return stack;
	  }
	
	public static HashMap<InventoryLotto2Type,ArrayList<LottoPackage>> loadLotto(ServerType type,final PermissionManager permissionManager,final StatsManager statsManager,Coins coins){
		if(lottoList==null){
			lottoList=new HashMap<InventoryLotto2Type,ArrayList<LottoPackage>>();
			
			lottoList.put(InventoryLotto2Type.COMMON, new ArrayList<LottoPackage>());
			lottoList.put(InventoryLotto2Type.UNCOMMON, new ArrayList<LottoPackage>());
			lottoList.put(InventoryLotto2Type.RARE, new ArrayList<LottoPackage>());
			lottoList.put(InventoryLotto2Type.LEGENDARY, new ArrayList<LottoPackage>());
			lottoList.put(InventoryLotto2Type.DIVINE, new ArrayList<LottoPackage>());
			
			if(type==ServerType.PVP){
				//ARMOR #########################################################
				lottoList.get(InventoryLotto2Type.DIVINE).add(new LottoPackage(null, null, EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_HELMET), new String[]{"§7Lotto Item"}, "§bFull-Enchant Diamand Helm"), new String[]{"all"}), InventoryLotto2Type.DIVINE));
				lottoList.get(InventoryLotto2Type.DIVINE).add(new LottoPackage(null, null, EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_CHESTPLATE), new String[]{"§7Lotto Item"}, "§bFull-Enchant Diamand Chestplate"), new String[]{"all"}), InventoryLotto2Type.DIVINE));
				lottoList.get(InventoryLotto2Type.DIVINE).add(new LottoPackage(null, null, EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_LEGGINGS), new String[]{"§7Lotto Item"}, "§bFull-Enchant Diamand Leggings"), new String[]{"all"}), InventoryLotto2Type.DIVINE));
				lottoList.get(InventoryLotto2Type.DIVINE).add(new LottoPackage(null, null, EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_BOOTS), new String[]{"§7Lotto Item"}, "§bFull-Enchant Diamant Boots"), new String[]{"all"}), InventoryLotto2Type.DIVINE));
			
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null, EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_HELMET), new String[]{"§7Lotto Item"}, "§bFull-Enchant Iron Helm"), new String[]{"all"}), InventoryLotto2Type.LEGENDARY));
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null, EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_CHESTPLATE), new String[]{"§7Lotto Item"}, "§bFull-Enchant Iron Chestplate"), new String[]{"all"}), InventoryLotto2Type.LEGENDARY));
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null, EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_LEGGINGS), new String[]{"§7Lotto Item"}, "§bFull-Enchant Iron Leggings"), new String[]{"all"}), InventoryLotto2Type.LEGENDARY));
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null, EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_BOOTS), new String[]{"§7Lotto Item"}, "§bFull-Enchant Iron Boots"), new String[]{"all"}), InventoryLotto2Type.LEGENDARY));
			
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null, EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_HELMET), new String[]{"§7Lotto Item"}, "§bFull-Enchant Gold Helm"), new String[]{"all"}), InventoryLotto2Type.LEGENDARY));
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null, EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_CHESTPLATE), new String[]{"§7Lotto Item"}, "§bFull-Enchant Gold Chestplate"), new String[]{"all"}), InventoryLotto2Type.LEGENDARY));
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null, EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_LEGGINGS), new String[]{"§7Lotto Item"}, "§bFull-Enchant Gold Leggings"), new String[]{"all"}), InventoryLotto2Type.LEGENDARY));
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null, EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_BOOTS), new String[]{"§7Lotto Item"}, "§bFull-Enchant Gold Boots"), new String[]{"all"}), InventoryLotto2Type.LEGENDARY));
				//ARMOR #########################################################
				
				//WEAPONS #########################################################
				lottoList.get(InventoryLotto2Type.DIVINE).add(new LottoPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_PICKAXE), new String[]{"§7Lotto Item"}, "§bFull-Enchant Diamand Pickaxe"), new String[]{"all"}),InventoryLotto2Type.DIVINE));
				lottoList.get(InventoryLotto2Type.DIVINE).add(new LottoPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_SPADE), new String[]{"§7Lotto Item"}, "§bFull-Enchant Diamand Shovel"), new String[]{"all"}),InventoryLotto2Type.DIVINE));
				lottoList.get(InventoryLotto2Type.DIVINE).add(new LottoPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_SWORD), new String[]{"§7Lotto Item"}, "§bFull-Enchant Diamand Sword"), new String[]{"all"}),InventoryLotto2Type.DIVINE));
				lottoList.get(InventoryLotto2Type.DIVINE).add(new LottoPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.BOW), new String[]{"§7Lotto Item"}, "§bFull-Enchant Bow"), new String[]{"all"}),InventoryLotto2Type.DIVINE));
				
				
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_PICKAXE), new String[]{"§7Lotto Item"}, "§bFull-Enchant Iron Pickaxe"), new String[]{"all"}),InventoryLotto2Type.LEGENDARY));
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_SPADE), new String[]{"§7Lotto Item"}, "§bFull-Enchant Iron Shovel"), new String[]{"all"}),InventoryLotto2Type.LEGENDARY));
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_SWORD), new String[]{"§7Lotto Item"}, "§bFull-Enchant Iron Sword"), new String[]{"all"}),InventoryLotto2Type.LEGENDARY));
				
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_PICKAXE), new String[]{"§7Lotto Item"}, "§bFull-Enchant Gold Pickaxe"), new String[]{"all"}),InventoryLotto2Type.LEGENDARY));
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_SPADE), new String[]{"§7Lotto Item"}, "§bFull-Enchant Gold Spade"), new String[]{"all"}),InventoryLotto2Type.LEGENDARY));
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_SWORD), new String[]{"§7Lotto Item"}, "§bFull-Enchant Gold Sword"), new String[]{"all"}),InventoryLotto2Type.LEGENDARY));
				//WEAPONS #########################################################
				
				//ITEMS #########################################################
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(null, null,UtilItem.Item(new ItemStack(Material.BEDROCK,64), new String[]{"§7Lotto Item"}, "§bBedrock"),InventoryLotto2Type.RARE));
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(null, null,UtilItem.Item(new ItemStack(Material.MOB_SPAWNER,5), new String[]{"§7Lotto Item"}, "§bMob-spawner"),InventoryLotto2Type.RARE));
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(null, null,UtilItem.Item(new ItemStack(Material.GOLDEN_APPLE,48,(byte)1), new String[]{"§7Lotto Item"}, "§bOp Äpfel"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(null, null,UtilItem.Item(new ItemStack(Material.BEDROCK,32), new String[]{"§7Lotto Item"}, "§bBedrock"),InventoryLotto2Type.RARE));
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(null, null,UtilItem.Item(new ItemStack(Material.MOB_SPAWNER,3), new String[]{"§7Lotto Item"}, "§bMob-spawner"),InventoryLotto2Type.RARE));
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(null, null,UtilItem.Item(new ItemStack(Material.GOLDEN_APPLE,32,(byte)1), new String[]{"§7Lotto Item"}, "§bOp Äpfel"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.UNCOMMON).add(new LottoPackage(null, null,UtilItem.Item(new ItemStack(Material.BEDROCK,16), new String[]{"§7Lotto Item"}, "§bBedrock"),InventoryLotto2Type.UNCOMMON));
				lottoList.get(InventoryLotto2Type.UNCOMMON).add(new LottoPackage(null, null,UtilItem.Item(new ItemStack(Material.MOB_SPAWNER,1), new String[]{"§7Lotto Item"}, "§bMob-spawner"),InventoryLotto2Type.UNCOMMON));
				lottoList.get(InventoryLotto2Type.UNCOMMON).add(new LottoPackage(null, null,UtilItem.Item(new ItemStack(Material.GOLDEN_APPLE,16,(byte)1), new String[]{"§7Lotto Item"}, "§bOp Äpfel"),InventoryLotto2Type.UNCOMMON));
				
				lottoList.get(InventoryLotto2Type.COMMON).add(new LottoPackage(null, null,UtilItem.Item(new ItemStack(Material.DIAMOND,16), new String[]{"§7Lotto Item"}, "§bDiamond"),InventoryLotto2Type.COMMON));
				
				Potion potion;
				for(int i = 8193; i<=16489;i++){
					try{
						potion= Potion.fromItemStack(new ItemStack(Material.POTION,32,(byte)i));
						if(!potion.getEffects().isEmpty()){
							lottoList.get(InventoryLotto2Type.COMMON).add(new LottoPackage(null, null, UtilItem.Item(potion.toItemStack(32), new String[]{"§7Lotto Item"}, "§bPotion"), InventoryLotto2Type.COMMON));
						}
					}catch(Exception e){
						
					}
				}
				//ITEMS #########################################################
				
				//PERKS #########################################################
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_JUMP);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_JUMP);
					}
					
				},UtilItem.Item(new ItemStack(Material.IRON_BOOTS,1), new String[]{"§7Lotto Perks"}, "§cPerk Double Jump"),InventoryLotto2Type.LEGENDARY));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_NO_HUNGER);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_NO_HUNGER);
					}
					
				},UtilItem.Item(new ItemStack(Material.COOKIE,1), new String[]{"§7Lotto Perks"}, "§cPerk No Hunger"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_NO_FIRE);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_NO_FIRE);
					}
					
				},UtilItem.Item(new ItemStack(Material.FIRE,1), new String[]{"§7Lotto Perks"}, "§cPerk Anti Fire"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_DOUBLE_XP);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_DOUBLE_XP);
					}
					
				},UtilItem.Item(new ItemStack(Material.EXP_BOTTLE), new String[]{"§7Lotto Perks"}, "§cPerk Double XP"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_GET_XP);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_GET_XP);
					}
					
				},UtilItem.Item(new ItemStack(Material.EXP_BOTTLE,1), new String[]{"§7Lotto Perks"}, "§cPerk Get XP"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_ITEM_NAME);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_ITEM_NAME);
					}
					
				},UtilItem.Item(new ItemStack(Material.NAME_TAG,1), new String[]{"§7Lotto Perks"}, "§cPerk Item Name"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_DROPPER);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_DROPPER);
					}
					
				},UtilItem.Item(new ItemStack(Material.BUCKET,1), new String[]{"§7Lotto Perks"}, "§cPerk Dropper"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_GOLENAPPLE);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_GOLENAPPLE);
					}
					
				},UtilItem.Item(new ItemStack(Material.POTION,1,(byte)8236), new String[]{"§7Lotto Perks"}, "§cPerk Potion Clear"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_HEALER);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_HEALER);
					}
					
				},UtilItem.Item(new ItemStack(Material.POTION,1,(byte)16389), new String[]{"§7Lotto Perks"}, "§cPerk Heal Potion"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_HAT);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_HAT);
					}
					
				},UtilItem.Item(new ItemStack(Material.SKULL_ITEM,1,(byte)3), new String[]{"§7Lotto Perks"}, "§cPerk EnemyHead"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_RUNNER);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_RUNNER);
					}
					
				},UtilItem.Item(new ItemStack(Material.POTION,1,(byte)8194), new String[]{"§7Lotto Perks"}, "§cPerk Runner"),InventoryLotto2Type.LEGENDARY));
				
				lottoList.get(InventoryLotto2Type.LEGENDARY).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_APPLE);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_APPLE);
					}
					
				},UtilItem.Item(new ItemStack(Material.GOLDEN_APPLE,1), new String[]{"§7Lotto Perks"}, "§cPerk GoldenApple"),InventoryLotto2Type.LEGENDARY));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_ARROW_POTIONEFFECT);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_ARROW_POTIONEFFECT);
					}
					
				},UtilItem.Item(new ItemStack(Material.ARROW,1), new String[]{"§7Lotto Perks"}, "§cPerk Arrow Potion Effect"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_WATER_DAMAGE);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_WATER_DAMAGE);
					}
					
				},UtilItem.Item(new ItemStack(Material.WATER,1), new String[]{"§7Lotto Perks"}, "§cPerk noWaterdamage"),InventoryLotto2Type.RARE));
				//PERKS #########################################################
				
				//COINS #########################################################
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 2500+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7Lotto Coins"}, "§b2500 Epics"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 2000+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7Lotto Coins"}, "§b2000 Epics"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 1500+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7Lotto Coins"}, "§b1500 Epics"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 1250+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7Lotto Coins"}, "§b1250 Epics"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 1000+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7Lotto Coins"}, "§b1000 Epics"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.RARE).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 1000+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7Lotto Coins"}, "§b1000 Epics"),InventoryLotto2Type.RARE));
				
				lottoList.get(InventoryLotto2Type.UNCOMMON).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 750+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7Lotto Coins"}, "§b750 Epics"),InventoryLotto2Type.UNCOMMON));
				
				lottoList.get(InventoryLotto2Type.UNCOMMON).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 500+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7Lotto Coins"}, "§b500 Epics"),InventoryLotto2Type.UNCOMMON));
				
				lottoList.get(InventoryLotto2Type.UNCOMMON).add(new LottoPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 250+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7Lotto Coins"}, "§b250 Epics"),InventoryLotto2Type.UNCOMMON));
				//COINS #########################################################
			}
		}
		return lottoList;
	}

	public static HashMap<TreasureChestType,ArrayList<TreasureChestPackage>> treasureChestItemList(){
		return treasureChestItemList(null, null, null,null);
	}
	
	public static HashMap<TreasureChestType,ArrayList<TreasureChestPackage>> treasureChestItemList(ServerType type,final PermissionManager permissionManager,final StatsManager statsManager,Coins coins){
		if(ItemList==null){
			ItemList = new HashMap<>();
			
			ItemList.put(TreasureChestType.UNCOMMON, new ArrayList<TreasureChestPackage>());
			ItemList.put(TreasureChestType.RARE, new ArrayList<TreasureChestPackage>());
			ItemList.put(TreasureChestType.MYTHICAL, new ArrayList<TreasureChestPackage>());
			
			if(type==ServerType.PVP){
				//RÜSTUNG #########################################################
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null,null,EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_HELMET), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamanthelm"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_LEGGINGS), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamanthose"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_CHESTPLATE), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantbrustpanzer"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_BOOTS), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantschuhe"), new String[]{"all"})));
				
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_HELMET), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamanthelm"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_LEGGINGS), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamanthose"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_CHESTPLATE), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantbrustpanzer"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_BOOTS), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantschuhe"), new String[]{"all"})));
				
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_HELMET), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamanthelm"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_LEGGINGS), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamanthose"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_CHESTPLATE), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantbrustpanzer"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_BOOTS), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantschuhe"), new String[]{"all"})));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_HELMET), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamanthelm"), new String[]{"all"})));
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_LEGGINGS), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamanthose"), new String[]{"all"})));
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_CHESTPLATE), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantbrustpanzer"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_BOOTS), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantschuhe"), new String[]{"all"})));
				
				//RÜSTUNG #########################################################
				
				//WEAPONS #########################################################
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_PICKAXE), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantspitzhacke"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_SPADE), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantschaufel"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.DIAMOND_SWORD), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantschwert"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.BOW), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Bow"), new String[]{"all"})));
				
				
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_PICKAXE), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantspitzhacke"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_SPADE), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantschaufel"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.IRON_SWORD), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantschwert"), new String[]{"all"})));
				
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_PICKAXE), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantspitzhacke"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_SPADE), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantschaufel"), new String[]{"all"})));
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(null, null,EnchantItem(UtilItem.Item(new ItemStack(Material.GOLD_SWORD), new String[]{"§7TreasureChest Item"}, "§bFull-Enchant Diamantschwert"), new String[]{"all"})));
				//WEAPONS #########################################################
				
				//ITEMS #########################################################
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(null, null,UtilItem.Item(new ItemStack(Material.BEDROCK,64), new String[]{"§7TreasureChest Item"}, "§bBedrock")));
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(null, null,UtilItem.Item(new ItemStack(Material.MOB_SPAWNER,5), new String[]{"§7TreasureChest Item"}, "§bMob-spawner")));
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(null, null,UtilItem.Item(new ItemStack(Material.GOLDEN_APPLE,48,(byte)1), new String[]{"§7TreasureChest Item"}, "§bOp Äpfel")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(null, null,UtilItem.Item(new ItemStack(Material.BEDROCK,32), new String[]{"§7TreasureChest Item"}, "§bBedrock")));
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(null, null,UtilItem.Item(new ItemStack(Material.MOB_SPAWNER,3), new String[]{"§7TreasureChest Item"}, "§bMob-spawner")));
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(null, null,UtilItem.Item(new ItemStack(Material.GOLDEN_APPLE,32,(byte)1), new String[]{"§7TreasureChest Item"}, "§bOp Äpfel")));
				
				ItemList.get(TreasureChestType.UNCOMMON).add(new TreasureChestPackage(null, null,UtilItem.Item(new ItemStack(Material.BEDROCK,16), new String[]{"§7TreasureChest Item"}, "§bBedrock")));
				ItemList.get(TreasureChestType.UNCOMMON).add(new TreasureChestPackage(null, null,UtilItem.Item(new ItemStack(Material.MOB_SPAWNER,1), new String[]{"§7TreasureChest Item"}, "§bMob-spawner")));
				ItemList.get(TreasureChestType.UNCOMMON).add(new TreasureChestPackage(null, null,UtilItem.Item(new ItemStack(Material.GOLDEN_APPLE,16,(byte)1), new String[]{"§7TreasureChest Item"}, "§bOp Äpfel")));
				//ITEMS #########################################################
				
				//PERKS #########################################################
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_JUMP);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_JUMP);
					}
					
				},UtilItem.Item(new ItemStack(Material.IRON_BOOTS,1), new String[]{"§7TreasureChest Perks"}, "§cPerk Double Jump")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_NO_HUNGER);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_NO_HUNGER);
					}
					
				},UtilItem.Item(new ItemStack(Material.COOKIE,1), new String[]{"§7TreasureChest Perks"}, "§cPerk No Hunger")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_NO_FIRE);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_NO_FIRE);
					}
					
				},UtilItem.Item(new ItemStack(Material.FIRE,1), new String[]{"§7TreasureChest Perks"}, "§cPerk Anti Fire")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_DOUBLE_XP);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_DOUBLE_XP);
					}
					
				},UtilItem.Item(new ItemStack(Material.EXP_BOTTLE), new String[]{"§7TreasureChest Perks"}, "§cPerk Double XP")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_GET_XP);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_GET_XP);
					}
					
				},UtilItem.Item(new ItemStack(Material.EXP_BOTTLE,1), new String[]{"§7TreasureChest Perks"}, "§cPerk Get XP")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_ITEM_NAME);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_ITEM_NAME);
					}
					
				},UtilItem.Item(new ItemStack(Material.NAME_TAG,1), new String[]{"§7TreasureChest Perks"}, "§cPerk Item Name")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_DROPPER);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_DROPPER);
					}
					
				},UtilItem.Item(new ItemStack(Material.BUCKET,1), new String[]{"§7TreasureChest Perks"}, "§cPerk Dropper")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_GOLENAPPLE);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_GOLENAPPLE);
					}
					
				},UtilItem.Item(new ItemStack(Material.POTION,1,(byte)8236), new String[]{"§7TreasureChest Perks"}, "§cPerk Potion Clear")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_HEALER);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_HEALER);
					}
					
				},UtilItem.Item(new ItemStack(Material.POTION,1,(byte)16389), new String[]{"§7TreasureChest Perks"}, "§cPerk Heal Potion")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_HAT);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_HAT);
					}
					
				},UtilItem.Item(new ItemStack(Material.SKULL_ITEM,1,(byte)3), new String[]{"§7TreasureChest Perks"}, "§cPerk EnemyHead")));
				
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_RUNNER);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_RUNNER);
					}
					
				},UtilItem.Item(new ItemStack(Material.POTION,1,(byte)8194), new String[]{"§7TreasureChest Perks"}, "§cPerk Runner")));
				
				ItemList.get(TreasureChestType.MYTHICAL).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_APPLE);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_APPLE);
					}
					
				},UtilItem.Item(new ItemStack(Material.GOLDEN_APPLE,1), new String[]{"§7TreasureChest Perks"}, "§cPerk GoldenApple")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_ARROW_POTIONEFFECT);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_ARROW_POTIONEFFECT);
					}
					
				},UtilItem.Item(new ItemStack(Material.ARROW,1), new String[]{"§7TreasureChest Perks"}, "§cPerk Arrow Potion Effect")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						permissionManager.addPermission(player, kPermission.PERK_WATER_DAMAGE);
					}
					
				}, new BooleanClick(){

					@Override
					public boolean onBooleanClick(Player player,
							ActionType type, Object object) {
						return permissionManager.haskPermission(player, kPermission.PERK_WATER_DAMAGE);
					}
					
				},UtilItem.Item(new ItemStack(Material.WATER,1), new String[]{"§7TreasureChest Perks"}, "§cPerk noWaterdamage")));
				//PERKS #########################################################
				
				//COINS #########################################################
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 2500+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7TreasureChest Coins"}, "§b2500 Epics")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 2000+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7TreasureChest Coins"}, "§b2000 Epics")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 1500+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7TreasureChest Coins"}, "§b1500 Epics")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 1250+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7TreasureChest Coins"}, "§b1250 Epics")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 1000+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7TreasureChest Coins"}, "§b1000 Epics")));
				
				ItemList.get(TreasureChestType.RARE).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 1000+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7TreasureChest Coins"}, "§b1000 Epics")));
				
				ItemList.get(TreasureChestType.UNCOMMON).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 750+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7TreasureChest Coins"}, "§b750 Epics")));
				
				ItemList.get(TreasureChestType.UNCOMMON).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 500+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7TreasureChest Coins"}, "§b500 Epics")));
				
				ItemList.get(TreasureChestType.UNCOMMON).add(new TreasureChestPackage(new Click(){

					@Override
					public void onClick(Player player, ActionType type,
							Object object) {
						statsManager.setInt(player, 250+statsManager.getInt(Stats.MONEY, player), Stats.MONEY);
					}
					
				}, null,UtilItem.Item(new ItemStack(Material.DOUBLE_PLANT,1), new String[]{"§7TreasureChest Coins"}, "§b250 Epics")));
				//COINS #########################################################
			}else if(type==ServerType.GAME){
				
			}
		}
		return ItemList;
	}
	
	public static ItemStack EnchantItem(ItemStack item,Enchantment ench,int lvl){
		item.addUnsafeEnchantment(ench, lvl);
		return item;
	}
	
	public static ItemStack EnchantItem(ItemStack item, String... ench){
		
		if(ench[0].equalsIgnoreCase("all")){
			if(isHelm(item))for(Enchantment e : enchantmentsHelm())EnchantItem(item, e, e.getMaxLevel());
			if(isChestplate(item))for(Enchantment e : enchantmentsChestplate())EnchantItem(item, e, e.getMaxLevel());
			if(isLeggings(item))for(Enchantment e : enchantmentsLeggings())EnchantItem(item, e, e.getMaxLevel());
			if(isBoots(item))for(Enchantment e : enchantmentsBoots())EnchantItem(item, e, e.getMaxLevel());
			
			if(isAxt(item))for(Enchantment e : enchantmentsAxt())EnchantItem(item, e, e.getMaxLevel());
			if(isPickaxe(item))for(Enchantment e : enchantmentsPickaxe())EnchantItem(item, e, e.getMaxLevel());
			if(isShovel(item))for(Enchantment e : enchantmentsShovel())EnchantItem(item, e, e.getMaxLevel());
			if(isSword(item))for(Enchantment e : enchantmentsSword())EnchantItem(item, e, e.getMaxLevel());
			
			if(item.getType()==Material.BOW)for(Enchantment e : enchantmentsBow())EnchantItem(item, e, e.getMaxLevel());
		}else{
			for(String e : ench){
				int lvl = Integer.valueOf(e.split(":")[1]);
				Enchantment en = Enchantment.getByName(e.split(":")[0]);
				item.addEnchantment(en, lvl);
			}
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
	
	public static Enchantment[] enchantmentsWeapon(){
		Enchantment[] axt = enchantmentsAxt();
		Enchantment[] sword = enchantmentsSword();
		Enchantment[] bow = enchantmentsBow();
		Enchantment[] en = new Enchantment[axt.length+sword.length+bow.length];
		
		int i = 0;
		for(Enchantment e : axt){
			en[i]=e;
			i++;
		}
		for(Enchantment e : sword){
			en[i]=e;
			i++;
		}
		for(Enchantment e : bow){
			en[i]=e;
			i++;
		}
		return en;
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
	
	public static Enchantment[] enchantmentsShovel(){
		return new Enchantment[]{Enchantment.SILK_TOUCH,Enchantment.DURABILITY,Enchantment.LUCK,Enchantment.DIG_SPEED};
	}
	
	public static Enchantment[] enchantmentsPickaxe(){
		return new Enchantment[]{Enchantment.SILK_TOUCH,Enchantment.DURABILITY,Enchantment.LUCK,Enchantment.DIG_SPEED};
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
	
	public static boolean isShovel(ItemStack item){
		return (item.getType()==Material.IRON_SPADE||item.getType()==Material.WOOD_SPADE||item.getType()==Material.DIAMOND_SPADE ||item.getType()==Material.GOLD_SPADE ||item.getType()==Material.STONE_SPADE);
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
	  
	  public static boolean ItemNameEqualsAll(ItemStack i, ItemStack i1){
		  if(i!=null&&i1!=null){
			  if(i.getType()!=i1.getType()){
				  return false;
			  }
			  
			  if(i.getAmount()!=i1.getAmount()){
				  return false;
			  }
			  
			  if(i.getData()!=i1.getData()){
				  	 System.out.println("FALSE--1");
				  return false;
			  }
			  
			  if(i.getDurability()!=i1.getDurability()){
				  	 System.out.println("FALSE0");
				  return false;
			  }
			  
			  if(i.hasItemMeta()==i1.hasItemMeta()){
				  if(i.hasItemMeta()){
					  if(i1.getItemMeta().hasDisplayName()==i1.getItemMeta().hasDisplayName()){
						  if(i.getItemMeta().hasDisplayName()){
							  if(!i.getItemMeta().getDisplayName().equalsIgnoreCase(i1.getItemMeta().getDisplayName())){
								  	 System.out.println("FALSE1");
									 return false; 
								  }
						  }
					  }
					  
					  if(i.getItemMeta().hasLore()==i.getItemMeta().hasLore()){
						  if(i.getItemMeta().hasLore()){
							  if(!(i.getItemMeta().getLore()==i.getItemMeta().getLore())){
								  	 System.out.println("FALSE2");
								  return false;
							  }
						  }
					  }
				  }
			  }
			  
			  if(i.getEnchantments()==i1.getEnchantments()){
				  	 System.out.println("FALSE3");
				  return false;
			  }
			  
			  return true;
		  }
		  	 System.out.println("FALSE");
		  return false;
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
