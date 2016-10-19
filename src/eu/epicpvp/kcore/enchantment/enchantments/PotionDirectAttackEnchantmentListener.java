package eu.epicpvp.kcore.enchantment.enchantments;

import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

@AllArgsConstructor
public class PotionDirectAttackEnchantmentListener extends DirectAttackEnchantmentListener {

	private PotionEffectType type;
	private int sec;
	private int potion_lvl;

	@Override
	public boolean doesFit(ItemStack item) {
		return UtilItem.isSword(item) || UtilItem.isAxt(item);
	}

	@Override
	protected ItemStack checkForEnchantment(Player player) {
		return player.getItemInHand();
	}

	@Override
	protected boolean onAttack(Player damager, Entity hurt, int level, DamageCause cause) {
		if (hurt instanceof Player) {
			Player victim = (Player) hurt;

			if (!hasCooldown(damager) && !victim.hasPotionEffect(type)) {
				UtilPlayer.addPotionEffect(victim, type, sec + level, potion_lvl);
			}
		}
		return false;
	}
}
