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
		super(InventorySize._54.getSize(), "Wings Shop");
		Bukkit.getPluginManager().registerEvents(this, instance);
		this.players=new HashMap<>();
		this.wings=new ArrayList<>();
		this.statsManager=StatsManagerRepository.getStatsManager(GameType.PROPERTIES);
		this.instance=instance;
		this.wings.add(new AngelWings("Angel Wings (Weiß)",PermissionType.WINGS_ANGEL_WHITE,true,true, Color.WHITE, Color.WHITE, Color.YELLOW));
		this.wings.add(new AngelWings("Angel Wings (Schwarz)",PermissionType.WINGS_ANGEL_BLACK,false,true, Color.BLACK, Color.BLACK, Color.BLACK));
		this.wings.add(new AngelWings("Angel Wings (Grau)",PermissionType.WINGS_ANGEL_GRAY,false,true, Color.GRAY, Color.GRAY, Color.GRAY));
		this.wings.add(new AngelWings("Angel Wings (Blau)",PermissionType.WINGS_ANGEL_BLUE,false,true, Color.BLUE, Color.BLUE, Color.BLUE));
		this.wings.add(new AngelWings("Angel Wings (Grün)",PermissionType.WINGS_ANGEL_GREEN,false,true, Color.GREEN, Color.GREEN, Color.GREEN));
		this.wings.add(new AngelWings("Angel Wings (Orange)",PermissionType.WINGS_ANGEL_ORANGE,false,true, Color.ORANGE, Color.ORANGE, Color.ORANGE));
		this.wings.add(new AngelWings("Angel Wings (Gelb)",PermissionType.WINGS_ANGEL_YELLOW,false,true, Color.YELLOW, Color.YELLOW, Color.YELLOW));
		
		this.wings.add(new ButterflyWings("Butterfly Wings (Gelb / Rot)",PermissionType.WINGS_BUTTERFLY_YELLOW_RED,true, Color.YELLOW, Color.RED, Color.YELLOW));
		this.wings.add(new ButterflyWings("Butterfly Wings (Rot / Blau)",PermissionType.WINGS_BUTTERFLY_RED_BLUE,true, Color.RED, Color.BLUE, Color.YELLOW));
		this.wings.add(new ButterflyWings("Butterfly Wings (Schwarz / Orange)",PermissionType.WINGS_BUTTERFLY_BLACK_ORANGE,true, Color.BLACK, Color.ORANGE, Color.YELLOW));
		this.wings.add(new ButterflyWings("Butterfly Wings (Aqua / Blau)",PermissionType.WINGS_BUTTERFLY_AQUA_BLUE,true, Color.AQUA, Color.BLUE, Color.YELLOW));
		this.wings.add(new ButterflyWings("Butterfly Wings (Lila / Gelb)",PermissionType.WINGS_BUTTERFLY_PURPLE_YELLOW,true, Color.PURPLE, Color.YELLOW, Color.YELLOW));
		this.wings.add(new ButterflyWings("Butterfly Wings (Aqua / Weiß)",PermissionType.WINGS_BUTTERFLY_AQUA_WHITE,true, Color.AQUA, Color.WHITE, Color.YELLOW));
		this.wings.add(new ButterflyWings("Butterfly Wings (Aqua / Gelb)",PermissionType.WINGS_BUTTERFLY_BLACK_AQUA,true, Color.BLACK, Color.AQUA, Color.YELLOW));
		
		this.wings.add(new BatWings("Bat Wings (Schwarz / Rot)",PermissionType.WINGS_BAT_BLACK_RED,true, Color.BLACK, Color.RED, Color.RED));
		this.wings.add(new BatWings("Bat Wings (Blau / Rot)",PermissionType.WINGS_BAT_BLUE_RED,true, Color.BLUE, Color.RED, Color.RED));
		this.wings.add(new BatWings("Bat Wings (Orange / Gelb)",PermissionType.WINGS_BAT_ORANGE_YELLOW,true, Color.ORANGE, Color.YELLOW, Color.RED));
		this.wings.add(new BatWings("Bat Wings (Blau / Hell Blau)",PermissionType.WINGS_BAT_BLUE_AQUA,true, Color.BLUE, Color.AQUA, Color.RED));
		this.wings.add(new BatWings("Bat Wings (Grün / Hell Grün)",PermissionType.WINGS_BAT_GREEN_LIME,true, Color.GREEN, Color.LIME, Color.RED));
		this.wings.add(new BatWings("Bat Wings (Schwarz / Weiß)",PermissionType.WINGS_BAT_BLACK_WHITE,true, Color.BLACK, Color.WHITE, Color.RED));
		this.wings.add(new BatWings("Bat Wings (Weiß / Gelb)",PermissionType.WINGS_BAT_WHITE_YELLOW,true, Color.WHITE, Color.YELLOW, Color.RED));
		
		this.wings.add(new InsectWings("Insect Wings (Weiß)", PermissionType.WINGS_INSECT_WHITE, true, Color.WHITE, Color.WHITE, Color.YELLOW));
		this.wings.add(new InsectWings("Insect Wings (Schwarz)", PermissionType.WINGS_INSECT_BLACK, true, Color.BLACK, Color.BLACK, Color.YELLOW));
		this.wings.add(new InsectWings("Insect Wings (Orange)", PermissionType.WINGS_INSECT_ORANGE, true, Color.ORANGE, Color.ORANGE, Color.YELLOW));
		this.wings.add(new InsectWings("Insect Wings (Aqua)", PermissionType.WINGS_INSECT_AQUA, true, Color.AQUA, Color.AQUA, Color.YELLOW));
		this.wings.add(new InsectWings("Insect Wings (Lila)", PermissionType.WINGS_INSECT_PURPLE, true, Color.PURPLE, Color.PURPLE, Color.YELLOW));
		this.wings.add(new InsectWings("Insect Wings (Rot)", PermissionType.WINGS_INSECT_RED, true, Color.RED, Color.RED, Color.YELLOW));
		this.wings.add(new InsectWings("Insect Wings (Gelb)", PermissionType.WINGS_INSECT_YELLOW, true, Color.YELLOW, Color.YELLOW, Color.YELLOW));
		
//		this.wings.add(new SupermanCape("Cape (Rot)", PermissionType.CAPE_RED, Color.RED, Color.RED, Color.YELLOW));
//		this.wings.add(new SupermanCape("Cape (Blau)", PermissionType.CAPE_BLUE, Color.BLUE, Color.BLUE, Color.YELLOW));
//		this.wings.add(new SupermanCape("Cape (Gelb)", PermissionType.CAPE_YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW));
//		this.wings.add(new SupermanCape("Cape (Weiß)", PermissionType.CAPE_WHITE, Color.WHITE, Color.WHITE, Color.YELLOW));
//		this.wings.add(new SupermanCape("Cape (Grün)", PermissionType.CAPE_GREEN, Color.GREEN, Color.GREEN, Color.YELLOW));
//		this.wings.add(new SupermanCape("Cape (Orange)", PermissionType.CAPE_ORANGE, Color.ORANGE, Color.ORANGE, Color.YELLOW));
//		this.wings.add(new SupermanCape("Cape (Lila)", PermissionType.CAPE_PURPLE, Color.PURPLE, Color.PURPLE, Color.YELLOW));
		
		int next = 0;
		for(int slot : UtilInv.getSlotsBorder(InventorySize.invSize(getSize()), InventorySplit._18)){
			if(wings.size()<=next){
				setItem(slot, UtilItem.RenameItem(new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)14), " "));
			}else{
				addButton(slot, new ParticleShapeButton(slot, this, wings.get(next)));
			}
			next++;
		}

		setCreate_new_inv(true);
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
					if(pa.getName().equalsIgnoreCase(nbt.getString("wings"))){
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
