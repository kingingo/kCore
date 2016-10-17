package eu.epicpvp.kcore.enchantment;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomEnchantment {

	private String enchantment;
	private EnchantmentDo edo;
	
	public CustomEnchantment(String enchantment, EnchantmentDo edo){
		this.enchantment=enchantment;
		this.edo=edo;
		
		CustomEnchantmentListener.getListener().register(this);
	}
	
	public boolean contains(ItemStack item){
		if(item.hasItemMeta()&&item.getItemMeta().hasLore()){
			if(item.getItemMeta().getLore().contains(enchantment)){
				return true;
			}
		}
		return false;
	}
	
	public void addEnchantment(ItemStack item, int lvl){
		if(item.hasItemMeta()){
			ItemMeta im = item.getItemMeta();
			
			if(!im.hasLore())im.setLore(Lists.newArrayList());
			List<String> lore = im.getLore();
			
			if(lore.contains(enchantment)){
				lore = lore.stream().filter( e -> !e.contains(enchantment) ).collect( Collectors.toList() );
			}
			
			lore.add("§7"+enchantment+" "+lvl);
		}
	}
	
	abstract class EnchantmentDo{
		public abstract void hit(Entity entity);
	}
}
