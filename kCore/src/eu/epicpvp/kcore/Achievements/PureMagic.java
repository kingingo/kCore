package eu.epicpvp.kcore.Achievements;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.enchantment.EnchantItemEvent;

import eu.epicpvp.kcore.Achievements.Handler.Achievement;
import eu.epicpvp.kcore.Enum.Zeichen;
import eu.epicpvp.kcore.Util.UtilPlayer;

public class PureMagic extends Achievement{

	public PureMagic() {
		super("§aPure Magie", Arrays.asList(" ","§a"+Zeichen.DOUBLE_ARROWS_R.getIcon()+" §7Verzaubere 100 Items."," "),1000,100);
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void EnchantItem(EnchantItemEvent ev){
		int playerId = UtilPlayer.getPlayerId(ev.getEnchanter());
		if(this.getPlayerProgress().containsKey(playerId)){
			this.getPlayerProgress().put(playerId, getProgress(playerId)+1);
			done(playerId);
		}
	}
}
