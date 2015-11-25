package me.kingingo.kcore.Kit.Shop;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import me.kingingo.karcade.Game.Multi.MultiGames;
import me.kingingo.karcade.Game.Multi.Events.MultiGameStartEvent;
import me.kingingo.karcade.Game.Multi.Events.MultiGameStateChangeEvent;
import me.kingingo.karcade.Game.Multi.Games.MultiGame;
import me.kingingo.kcore.Enum.GameState;
import me.kingingo.kcore.Enum.PlayerState;
import me.kingingo.kcore.Kit.Kit;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Kit.Perks.Event.PerkHasPlayerEvent;
import me.kingingo.kcore.Kit.Perks.Event.PerkStartEvent;
import me.kingingo.kcore.Util.InventorySize;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class MultiKitShop extends KitShop{

	@Getter
	private MultiGames games;
	private HashMap<MultiGame,ArrayList<Player>> list;
	
	public MultiKitShop(MultiGames games, String name, InventorySize size,Kit[] kits) {
		super(games.getManager().getInstance(), games.getGems(), games.getCoins(), games.getManager().getPermManager(), name, size, kits);
		this.games=games;
		this.list=new HashMap<>();
		
		for(Kit k : getKits()){
			for(Perk perk : k.getPerks()){
				Bukkit.getPluginManager().registerEvents(perk, getPermManager().getInstance());
			}
		}
	}
	
	@EventHandler
	public void has(PerkHasPlayerEvent ev){
		for(MultiGame game : list.keySet()){
			if(list.get(game).contains(ev.getPlayer())){
				if(game.getState()!=GameState.InGame&&game.getState()!=GameState.DeathMatch){
					ev.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority=EventPriority.NORMAL)
	public void StateChange(MultiGameStateChangeEvent ev){
		if(ev.getTo()==GameState.Restart){
			if(list.containsKey(ev.getGame())){
				list.get(ev.getGame()).clear();
				list.remove(ev.getGame());
			}
		}
	}
			
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void Start(MultiGameStartEvent ev){
		if(!list.containsKey(ev.getGame())){
			list.put(ev.getGame(), ev.getGame().getGameList().getPlayers(PlayerState.IN));
		}else{
			list.get(ev.getGame()).clear();
			list.remove(ev.getGame());
			list.put(ev.getGame(), ev.getGame().getGameList().getPlayers(PlayerState.IN));
		}
		
		Bukkit.getPluginManager().callEvent(new PerkStartEvent(ev.getGame().getGameList().getPlayers(PlayerState.IN)));
	}
}
