package eu.epicpvp.kcore.enchantment.enchantments.bow;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Util.UtilMath;
import eu.epicpvp.kcore.enchantment.enchantments.ArrowHitEnchantmentListener;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LightningEnchantmentListener extends ArrowHitEnchantmentListener{

	private int chance;
	
	@Override
	public boolean doesFit(ItemStack item) {
		return item.getType()==Material.BOW;
	}

	@Override
	protected ItemStack checkForEnchantment(Player player) {
		return player.getItemInHand();
	}

	protected void onDirectAttack(Player damager, Entity hurt,Arrow arrow, int level) {
		int tempt = level;
		while(tempt != 0){
			if(UtilMath.randomInteger(100) < chance){
				hurt.getWorld().strikeLightning(hurt.getLocation());
			}
			tempt--;
		}
	}
}
