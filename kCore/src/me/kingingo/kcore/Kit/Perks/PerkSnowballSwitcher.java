package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkStartEvent;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class PerkSnowballSwitcher extends Perk{

	private ItemStack s;
	
	public PerkSnowballSwitcher() {
		super("SnowballSwitcher");
		this.s=UtilItem.addEnchantmentGlow(UtilItem.RenameItem(new ItemStack(Material.SNOW_BALL), "§bSwitcher"));
	}
	
	@EventHandler
	public void Perk(PerkStartEvent ev){
		if(!getPerkData().getPlayers().containsKey(this))return;
		for(Player p : getPerkData().getPlayers().get(this))p.getInventory().addItem(s.clone());
	}

	Snowball snowball;
	Location loc;
	@EventHandler
	public void Damage(EntityDamageByEntityEvent ev){
		if(ev.getDamager() instanceof Snowball){
			snowball=(Snowball)ev.getDamager();
			if(!(snowball.getShooter() instanceof Player))return;
			if(getPerkData().hasPlayer(this, ((Player)snowball.getShooter()))){
				if(ev.getEntity() instanceof Player){
					loc=ev.getEntity().getLocation();
					ev.getEntity().teleport(((Player)snowball.getShooter()).getLocation());
					((Player)snowball.getShooter()).teleport(loc);
				}
			}
		}
	}
	
	@EventHandler
	public void ProjectileLaunch(ProjectileLaunchEvent ev){
		if(ev.getEntity() instanceof Snowball){
			snowball = (Snowball)ev.getEntity();
			if(!(snowball.getShooter() instanceof Player))return;
			if(getPerkData().hasPlayer(this, ((Player)snowball.getShooter()))){
				((Player)snowball.getShooter()).getInventory().addItem(s.clone());
			}
		}
	}

}
