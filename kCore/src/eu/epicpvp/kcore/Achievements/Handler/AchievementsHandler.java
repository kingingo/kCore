package eu.epicpvp.kcore.Achievements.Handler;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Inventory.InventoryPageBase;
import eu.epicpvp.kcore.Inventory.Inventory.InventoryCopy;
import eu.epicpvp.kcore.Inventory.Item.Click;
import eu.epicpvp.kcore.Inventory.Item.Buttons.ButtonBase;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Util.InventorySize;
import eu.epicpvp.kcore.Util.InventorySplit;
import eu.epicpvp.kcore.Util.UtilEvent.ActionType;
import eu.epicpvp.kcore.Util.UtilInv;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

@Getter
public class AchievementsHandler extends kListener{

	private JavaPlugin instance;
	private boolean config=true;
	private InventoryCopy inventory;
	private ArrayList<Achievement> achievements;
	
	public AchievementsHandler(JavaPlugin instance) {
		this(instance,null);
	}
	
	public AchievementsHandler(JavaPlugin instance,Achievement... achievements) {
		super(instance, "AchievementsHandler");
		this.instance=instance;
		this.achievements=new ArrayList<>();
		UtilServer.setAchievementsHandler(this);
		UtilServer.getCommandHandler().register(CommandAchievements.class, new CommandAchievements(this));
		if(achievements!=null){
			register(achievements);
			initInventory();
		}
	}
	
	public void register(Achievement... achievements){
		for(Achievement achievement : achievements){
			achievement.register(this);
		}
	}
	
	public void initInventory(){
		Integer[] slots = UtilInv.getSlotsBorder(InventorySize._54, InventorySplit._18, InventorySplit._36);
		
		this.inventory = new InventoryCopy(InventorySize._54, "Achievements", new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				if(getAchievements().isEmpty())return;
				int playerId = UtilPlayer.getPlayerId(player);
				InventoryPageBase page = ((InventoryPageBase)object);
				int index = 0;
				for(int slot : slots){
					if(getAchievements().size() <= index)break;
					Achievement achievement = getAchievements().get(index);
					if(achievement.getPlayerProgress().containsKey(playerId)){
						page.setItem(slot, UtilItem.Item(new ItemStack(351,1,(byte)8), (achievement.isSecret() ? Arrays.asList(" ","§cGeheim") : achievement.getDescription(player)), achievement.getName()));
					}else{
						page.setItem(slot, UtilItem.Item(new ItemStack(351,1,(byte)10), achievement.getDescription(player), achievement.getName()));
					}
					index++;
				}
			}
			
		});
		this.inventory.addButton(0, new ButtonBase(new Click(){

			@Override
			public void onClick(Player player, ActionType type, Object object) {
				player.closeInventory();
			}
			
		},UtilItem.RenameItem(new ItemStack(Material.BARRIER), "§cSchließen")));
		this.inventory.setCreate_new_inv(true);
		UtilInv.getBase().addPage(this.inventory);
	}
	
}
