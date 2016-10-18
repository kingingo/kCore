package eu.epicpvp.kcore.enchantment.enchantments.tools;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.enchantment.enchantments.BlockBreakEnchantmentListener;

public class SmeltingEnchantmentListener extends BlockBreakEnchantmentListener{

	@Override
	public boolean doesFit(ItemStack item) {
		return UtilItem.isPickaxe(item) || UtilItem.isShovel(item);
	}

	@Override
	protected ItemStack checkForEnchantment(Player player) {
		return player.getItemInHand();
	}
	
	protected boolean onBreak(Player player,Block block, int level) {
		if(!block.getDrops(checkForEnchantment(player)).isEmpty()){
			Material drop = null;
			
			switch(block.getType()){
			case COBBLESTONE:
				drop=Material.STONE;
				break;
			case IRON_ORE:
				drop=Material.IRON_INGOT;
				break;
			case GOLD_ORE:
				drop=Material.GOLD_INGOT;
				break;
			case COAL_ORE:
				drop=Material.COAL;
				break;
			case SAND:
				drop=Material.GLASS;
				break;
			case CLAY:
				drop=Material.BRICK;
				break;
			}
			
			if(drop!=null){
				ItemStack itemToDrop = new ItemStack(drop, block.getDrops(checkForEnchantment(player)).size());
				player.getWorld().dropItemNaturally(block.getLocation(), itemToDrop);
				player.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 12);
				block.setType(Material.AIR);
				return true;
			}
		}
		return false;
	}
}
