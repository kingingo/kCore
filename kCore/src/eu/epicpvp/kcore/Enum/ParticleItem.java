package eu.epicpvp.kcore.Enum;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilParticle;
import lombok.Getter;

public enum ParticleItem {
MOB_APPEARANCE(UtilItem.RenameItem(new ItemStack(Material.FIREWORK_CHARGE), "§bMob Appearance"),UtilParticle.MOB_APPEARANCE),
ITEM_TAKE(UtilItem.RenameItem(new ItemStack(Material.EMERALD), "§bItem Take"),UtilParticle.ITEM_TAKE),
WATER_DROP(UtilItem.RenameItem(new ItemStack(Material.WATER_BUCKET), "§bWater Drop"),UtilParticle.WATER_DROP),
BLOCK_DUST(UtilItem.RenameItem(new ItemStack(Material.GLOWSTONE_DUST), "§bBlock Dust"),UtilParticle.BLOCK_DUST),
BLOCK_CRACK(UtilItem.RenameItem(new ItemStack(Material.STONE), "§bBlock Crack"),UtilParticle.BLOCK_CRACK),
ITEM_CRACK(UtilItem.RenameItem(new ItemStack(Material.ITEM_FRAME), "§bItem Crack"),UtilParticle.BUBBLE),
BARRIER(UtilItem.RenameItem(new ItemStack(Material.BARRIER), "§bBarrier"),UtilParticle.ITEM_CRACK),
HEART(UtilItem.RenameItem(new ItemStack(Material.APPLE), "§bHeart"),UtilParticle.HEART),
SLIME(UtilItem.RenameItem(new ItemStack(Material.SLIME_BALL), "§bSlime"),UtilParticle.SLIME),
SNOW_SHOVEL(UtilItem.RenameItem(new ItemStack(Material.IRON_SPADE), "§bSnow Shovel"),UtilParticle.SNOW_SHOVEL),
SNOWBALL(UtilItem.RenameItem(new ItemStack(Material.SNOW_BALL), "§bSnowball"),UtilParticle.SNOWBALL),
REDSTONE(UtilItem.RenameItem(new ItemStack(Material.REDSTONE), "§bRedstone"),UtilParticle.REDSTONE),
CLOUD(UtilItem.RenameItem(new ItemStack(Material.WOOL), "§bCloud"),UtilParticle.CLOUD),
FOOTSTEP(UtilItem.RenameItem(new ItemStack(Material.LEATHER_BOOTS), "§bFootstep"),UtilParticle.FOOTSTEP),
LAVA(UtilItem.RenameItem(new ItemStack(Material.LAVA_BUCKET), "§bLava"),UtilParticle.LAVA),
FLAME(UtilItem.RenameItem(new ItemStack(Material.FLINT_AND_STEEL), "§bFlame"),UtilParticle.FLAME),
ENCHANTMENT_TABLE(UtilItem.RenameItem(new ItemStack(Material.ENCHANTMENT_TABLE), "§bEnchantment Table"),UtilParticle.ENCHANTMENT_TABLE),
PORTAL(UtilItem.RenameItem(new ItemStack(Material.PORTAL), "§bPortal"),UtilParticle.PORTAL),
NOTE(UtilItem.RenameItem(new ItemStack(Material.NOTE_BLOCK), "§bNote"),UtilParticle.NOTE),
TOWN_AURA(UtilItem.RenameItem(new ItemStack(351), "§bTown Aura"),UtilParticle.TOWN_AURA),
VILLAGER_ANGRY(UtilItem.RenameItem(new ItemStack(Material.COOKIE), "§bVillager Happy"),UtilParticle.VILLAGER_ANGRY),
VILLAGER_HAPPY(UtilItem.RenameItem(new ItemStack(Material.BLAZE_POWDER), "§bVillager Angry"),UtilParticle.VILLAGER_HAPPY),
DRIP_LAVA(UtilItem.RenameItem(new ItemStack(Material.LAVA_BUCKET), "§bDrip Lava"),UtilParticle.DRIP_LAVA),
DRIP_WATER(UtilItem.RenameItem(new ItemStack(Material.WATER_BUCKET), "§bDrip Water"),UtilParticle.DRIP_WATER),
SPELL_WITCH(UtilItem.RenameItem(new ItemStack(Material.BREWING_STAND_ITEM), "§bSpell Witch"),UtilParticle.SPELL_WITCH),
SPELL_MOB_AMBIENT(UtilItem.RenameItem(new ItemStack(Material.SKULL_ITEM,1,(byte)SkullType.ZOMBIE.ordinal()), "§bSpell Mob Ambient"),UtilParticle.SPELL_MOB_AMBIENT),
SPELL_MOB(UtilItem.RenameItem(new ItemStack(Material.SKULL_ITEM,1,(byte)SkullType.ZOMBIE.ordinal()), "§bSpell Mob"),UtilParticle.SPELL_MOB),
SPELL_INSTANT(UtilItem.RenameItem(new ItemStack(351,1,(byte)15), "§bSpell Instant"),UtilParticle.SPELL_INSTANT),
SPELL(UtilItem.RenameItem(new ItemStack(351,1,(byte)15), "§bSpell"),UtilParticle.SPELL),
SMOKE_LARGE(UtilItem.RenameItem(new ItemStack(351,1,(byte)7), "§bSmoke Large"),UtilParticle.SMOKE_LARGE),
SMOKE_NORMAL(UtilItem.RenameItem(new ItemStack(351,1,(byte)7), "§bSmoke Normal"),UtilParticle.SMOKE_NORMAL),
LARGE_SMOKE(UtilItem.RenameItem(new ItemStack(351,1,(byte)7), "§bLarge Smoke"),UtilParticle.LARGE_SMOKE),
CRIT_MAGIC(UtilItem.RenameItem(new ItemStack(Material.ARROW), "§bCrit Magic"),UtilParticle.CRIT_MAGIC),
CRIT(UtilItem.RenameItem(new ItemStack(Material.ARROW), "§bCrit"),UtilParticle.CRIT),
SUSPENDED_DEPTH(UtilItem.RenameItem(new ItemStack(351,1,(byte)12), "§bSuspended Depth"),UtilParticle.SUSPENDED_DEPTH),
SUSPENDED(UtilItem.RenameItem(new ItemStack(351,1,(byte)12), "§BSuspended"),UtilParticle.SUSPENDED),
WATER_WAKE(UtilItem.RenameItem(new ItemStack(Material.WATER_BUCKET), "§bWater Wake"),UtilParticle.WATER_WAKE),
WATER_SPLASH(UtilItem.RenameItem(new ItemStack(Material.WATER_BUCKET), "§bWater Splash"),UtilParticle.WATER_SPLASH),
WATER_BUBBLE(UtilItem.RenameItem(new ItemStack(Material.WATER_BUCKET), "§bWater Bubble"),UtilParticle.WATER_BUBBLE),
FIREWORKS_SPARK(UtilItem.RenameItem(new ItemStack(Material.FIREWORK), "§bFirework Spark"),UtilParticle.FIREWORKS_SPARK),
HAPPY_VILLAGER(UtilItem.RenameItem(new ItemStack(Material.COOKIE), "§bHappy Villager"),UtilParticle.HAPPY_VILLAGER),
ANGRY_VILLAGER(UtilItem.RenameItem(new ItemStack(Material.BLAZE_POWDER), "§bAngry Villager"),UtilParticle.ANGRY_VILLAGER),
SNOWBALL_POOF(UtilItem.RenameItem(new ItemStack(Material.SNOW_BALL), "§bSnowball Poof"),UtilParticle.SNOWBALL_POOF),
RED_DUST(UtilItem.RenameItem(new ItemStack(351,1,(byte)1), "§bRed Dust"),UtilParticle.RED_DUST),
SPLASH(UtilItem.RenameItem(new ItemStack(Material.WATER_BUCKET), "§bSplash"),UtilParticle.SPLASH),
WITCH_MAGIC(UtilItem.RenameItem(new ItemStack(351,1,(byte)5), "§bWitch Magic"),UtilParticle.WITCH_MAGIC),
INSTANT_SPELL(UtilItem.RenameItem(new ItemStack(Material.NETHER_STAR), "§bInstant Spell"),UtilParticle.INSTANT_SPELL),
MOB_SPELL_AMBIENT(UtilItem.RenameItem(new ItemStack(Material.FEATHER), "§bMob Speel Ambient"),UtilParticle.MOB_SPELL_AMBIENT),
MOB_SPELL(UtilItem.RenameItem(new ItemStack(351,1,(byte)4), "§bMob Spell"),UtilParticle.MOB_SPELL),
MAGIC_CRIT(UtilItem.RenameItem(new ItemStack(Material.DIAMOND_SWORD), "§bMagic Crit"),UtilParticle.MAGIC_CRIT),
DEPTH_SUSPEND(UtilItem.RenameItem(new ItemStack(351,1,(byte)8), "§bDepth Suspend"),UtilParticle.DEPTH_SUSPEND),
SUSPEND(UtilItem.RenameItem(new ItemStack(Material.WATER_BUCKET), "§BSuspend"),UtilParticle.SUSPEND),
LARGE_EXPLODE(UtilItem.RenameItem(new ItemStack(Material.TNT), "§bLarge Explosion"),UtilParticle.LARGE_EXPLODE),
HUGE_EXPLOSION(UtilItem.RenameItem(new ItemStack(Material.TNT), "§bHuge Explosion"),UtilParticle.HUGE_EXPLOSION),
EXPLODE(UtilItem.RenameItem(new ItemStack(Material.TNT), "§bExplosion"),UtilParticle.EXPLODE),
EXPLOSION_NORMAL(UtilItem.RenameItem(new ItemStack(Material.TNT), "§bExplosion Normal"),UtilParticle.EXPLOSION_NORMAL),
EXPLOSION_LARGE(UtilItem.RenameItem(new ItemStack(Material.TNT), "§bExplosion Large"),UtilParticle.EXPLOSION_LARGE),
EXPLOSION_HUGE(UtilItem.RenameItem(new ItemStack(Material.TNT), "§bExplosion Huge"),UtilParticle.EXPLOSION_HUGE),
BUBBLE(UtilItem.RenameItem(new ItemStack(Material.WATER_BUCKET), "§bBubble"),UtilParticle.BUBBLE);
	
	@Getter
	private ItemStack item;
	@Getter
	private UtilParticle particle;

	private ParticleItem(ItemStack item,UtilParticle particle){
		this.item=item;
		this.particle=particle;
	}
	
	public static ParticleItem valueOf(ItemStack item){
		for(ParticleItem par : values()){
			if(UtilItem.ItemNameEquals(par.getItem(), item)){
				return par;
			}
		}
		return null;
	}
}
