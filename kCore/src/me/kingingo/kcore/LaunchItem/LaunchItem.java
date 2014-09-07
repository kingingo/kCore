package me.kingingo.kcore.LaunchItem;

import lombok.Getter;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilMath;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LaunchItem {

	@Getter
	private Entity droppedItem;
	@Getter
	private Player player;
	@Getter
	private Long time;
	@Getter LaunchItemEventHandler handler;
	
	public interface LaunchItemEventHandler {
        public void onLaunchItem(LaunchItemEvent event);
    }
	
	public LaunchItem(Player p,int sec,final LaunchItemEventHandler handler) {
		this.time=System.currentTimeMillis()+TimeSpan.SECOND*sec;
   		this.droppedItem=(Entity) p.getWorld().dropItemNaturally(p.getEyeLocation(),UtilItem.RenameItem(new ItemStack(p.getItemInHand().getType(),1,p.getItemInHand().getData().getData()), "Item"+UtilMath.r(100)));
   		this.droppedItem.setVelocity(p.getLocation().getDirection());
   		UtilInv.remove(p, p.getItemInHand().getType(), p.getItemInHand().getData().getData(), 1);
   		this.player=p;
   		this.handler=handler;
	}
	
	public void remove(){
		this.droppedItem.remove();
	}
	
	public boolean check(){
		if(time<System.currentTimeMillis()){
			LaunchItemEvent e = new LaunchItemEvent(this);
			handler.onLaunchItem(e);
			droppedItem.remove();
			return true;
		}
		return false;
	}
	
}
