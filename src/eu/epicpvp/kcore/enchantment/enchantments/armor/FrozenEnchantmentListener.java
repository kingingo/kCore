package eu.epicpvp.kcore.enchantment.enchantments.armor;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.enchantment.enchantments.ArmorAttackEnchantmentListener;
import eu.epicpvp.kcore.enchantment.enchantments.DirectAttackEnchantmentListener;

public class FrozenEnchantmentListener extends ArmorAttackEnchantmentListener{

	@Override
	protected boolean onDirectAttacked(Entity damager, Player hurt, int level,DamageCause cause) {
		if(damager instanceof LivingEntity){
			LivingEntity le = (LivingEntity)damager;
			
			if(!le.hasPotionEffect(PotionEffectType.SLOW)){
				le.addPotionEffect(UtilPlayer.getPotionEffect(PotionEffectType.SLOW, 4, 1), true);
			}
		}
		return false;
	}
}
