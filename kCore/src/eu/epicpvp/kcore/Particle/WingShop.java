package eu.epicpvp.kcore.Particle;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import dev.wolveringer.dataserver.gamestats.GameType;
import dev.wolveringer.dataserver.gamestats.StatsKey;
import dev.wolveringer.nbt.NBTTagCompound;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Particle.Cape.SupermanCape;
import eu.epicpvp.kcore.Particle.Wings.AngelWings;
import eu.epicpvp.kcore.Particle.Wings.BatWings;
import eu.epicpvp.kcore.Particle.Wings.ButterflyWings;
import eu.epicpvp.kcore.Particle.Wings.InsectWings;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.StatsManager.StatsManager;
import eu.epicpvp.kcore.StatsManager.StatsManagerRepository;
import eu.epicpvp.kcore.StatsManager.Event.PlayerStatsLoadedEvent;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilPlayer;
import lombok.Getter;

public class WingShop extends InventoryCopy implements Listener{

	@Getter
	private HashMap<Player,PlayerParticle> players;
	@Getter
	private JavaPlugin instance;
	@Getter
	private StatsManager statsManager;
	@Getter
	private ArrayList<ParticleShape> wings;
	
	public WingShop(JavaPlugin instance) {
		super(InventorySize._54.getSize(), "WingShop");
		Bukkit.getPluginManager().registerEvents(this, instance);
		this.players=new HashMap<>();
		this.wings=new ArrayList<>();
		this.statsManager=StatsManagerRepository.getStatsManager(GameType.PROPERTIES);
		this.instance=instance;
		this.wings.add(new AngelWings(UtilItem.RenameItem(new ItemStack(Material.SUGAR), "§7White Angel Wings"),PermissionType.WINGS_WHITE,true, Color.WHITE, Color.WHITE, Color.YELLOW));
		this.wings.add(new ButterflyWings(UtilItem.RenameItem(new ItemStack(Material.FEATHER), "§7Butterfly Wings"),PermissionType.WINGS_WHITE,true, Color.YELLOW, Color.RED, Color.YELLOW));
		this.wings.add(new BatWings(UtilItem.RenameItem(new ItemStack(Material.GOLD_BARDING), "§7Bat Wings"),PermissionType.WINGS_WHITE,true, Color.BLACK, Color.RED, Color.RED));
		this.wings.add(new SupermanCape(UtilItem.RenameItem(new ItemStack(Material.GOLD_INGOT), "§7Cape"), PermissionType.WINGS_WHITE, Color.WHITE, Color.RED, Color.YELLOW));
		this.wings.add(new InsectWings(UtilItem.RenameItem(new ItemStack(Material.IRON_INGOT), "§7Insect Wings"), PermissionType.WINGS_WHITE, true, Color.WHITE, Color.WHITE, Color.YELLOW));
		
		int next = 0;
		for(int slot : UtilInv.getSlotsBorder(InventorySize.invSize(getSize()), InventorySplit._18)){
			if(wings.size()<=next){
				setItem(slot, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), " "));
			}else{
				addButton(slot, new ParticleShapeButton(slot, this, wings.get(next)));
			}
			next++;
		}
		
		fill(Material.STAINED_GLASS_PANE,7);
		UtilInv.getBase().addPage(this);
	}
	
	@EventHandler
	public void load(PlayerStatsLoadedEvent ev){
		if(ev.getManager().getType() == GameType.PROPERTIES && UtilPlayer.isOnline(ev.getPlayerId())){
			Player player = UtilPlayer.searchExact(ev.getPlayerId());
			NBTTagCompound nbt = ev.getManager().getNBTTagCompound(player, StatsKey.PROPERTIES);
			
			if(nbt.hasKey("wings")){
				for(ParticleShape pa : wings){
					if(pa.getItem().getTypeId() == nbt.getInt("wings")){
						getPlayers().put(player, new PlayerParticle<>(player, pa));
						getPlayers().get(player).start(getInstance());
						break;
					}
				}
			}
		}
	}

	@EventHandler
	public void quit(PlayerQuitEvent ev){
		players.remove(ev.getPlayer());
	}
}
