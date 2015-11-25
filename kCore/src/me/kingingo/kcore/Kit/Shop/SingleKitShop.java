package me.kingingo.kcore.Kit.Shop;

import java.util.ArrayList;

import me.kingingo.karcade.Game.Events.GameStartEvent;
import me.kingingo.kcore.Kit.Kit;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkStartEvent;
import me.kingingo.kcore.Permission.PermissionManager;
import me.kingingo.kcore.Util.Coins;
import me.kingingo.kcore.Util.Gems;
import me.kingingo.kcore.Util.InventorySize;
import me.kingingo.kcore.Util.UtilServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

public class SingleKitShop extends KitShop{

	public SingleKitShop(JavaPlugin instance, Gems gems, Coins coins,PermissionManager manager, String name, InventorySize size,Kit[] kits) {
		super(instance, gems, coins, manager, name, size, kits);
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void Start(GameStartEvent ev){
		for(Kit k : getKits()){
			for(Perk perk : k.getPerks()){
				Bukkit.getPluginManager().registerEvents(perk, getPermManager().getInstance());
			}
		}
		ArrayList<Player> list = new ArrayList<>();
		for(Player player : UtilServer.getPlayers())list.add(player);
		Bukkit.getPluginManager().callEvent(new PerkStartEvent( list ));
		list.clear();
		list=null;
	}
}
