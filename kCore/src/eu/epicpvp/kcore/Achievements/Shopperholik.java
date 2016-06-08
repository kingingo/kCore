package eu.epicpvp.kcore.Achievements;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.ItemShop.Events.PlayerBuyItemEvent;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class Shopperholik extends Achievement{

	public Shopperholik() {
		super("§aShopperholik", Arrays.asList(" ","§a"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §7Führe 10.000 Transaktionen im Item Shop durch"," "),1000,10000);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void PlayerBuyItem(PlayerBuyItemEvent ev){
		int playerId = UtilPlayer.getPlayerId(ev.getPlayer());
		if(this.getPlayerProgress().containsKey(playerId)){
			this.getPlayerProgress().put(playerId, getProgress(playerId)+1);
			done(playerId);
		}
	}
}
