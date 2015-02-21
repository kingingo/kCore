package me.kingingo.kcore.LaunchItem;

import lombok.Getter;
import me.kingingo.kcore.Util.TimeSpan;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilItem;
import me.kingingo.kcore.Util.UtilMath;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LaunchItem {

	@Getter
	private Entity[] droppedItem;
	@Getter
	private Player player;
	@Getter
	private Long time;
	@Getter 
	private LaunchItemEventHandler handler;
	
	public interface LaunchItemEventHandler {
        public void onLaunchItem(LaunchItemEvent event);
    }
	
	public LaunchItem(Player p,Location loc,int id,int anzahl,int sec,final LaunchItemEventHandler handler) {
		this.time=System.currentTimeMillis()+TimeSpan.SECOND*sec;
		this.droppedItem=new Entity[anzahl];
   		for(int i = 0 ; i < anzahl; i++){
   			this.droppedItem[i]=(Entity) loc.getWorld().dropItemNaturally(loc,UtilItem.RenameItem(new ItemStack(id,1), "Item"+UtilMath.r(100)));
   	   		this.droppedItem[i].setVelocity(loc.add(UtilMath.r(5),UtilMath.r(5),UtilMath.r(5)).getDirection());
   		}
   		UtilInv.remove(p, new ItemStack(id,1),anzahl);
   		this.player=p;
   		this.handler=handler;
	}
	
	public LaunchItem(Player p,int id,int anzahl,int sec,final LaunchItemEventHandler handler) {
		this.time=System.currentTimeMillis()+TimeSpan.SECOND*sec;
		this.droppedItem=new Entity[anzahl];
   		for(int i = 0 ; i < anzahl; i++){
   			this.droppedItem[i]=(Entity) p.getWorld().dropItemNaturally(p.getEyeLocation(),UtilItem.RenameItem(new ItemStack(id,1), "Item"+UtilMath.r(100)));
   	   		this.droppedItem[i].setVelocity(p.getLocation().add(UtilMath.r(5),UtilMath.r(5),UtilMath.r(5)).getDirection());
   		}
   		UtilInv.remove(p, new ItemStack(id,1),anzahl);
   		this.player=p;
   		this.handler=handler;
	}
	
	public LaunchItem(Player p,int id,int sec,final LaunchItemEventHandler handler) {
		this.time=System.currentTimeMillis()+TimeSpan.SECOND*sec;
		this.droppedItem=new Entity[1];
   		this.droppedItem[0]=(Entity) p.getWorld().dropItemNaturally(p.getEyeLocation(),UtilItem.RenameItem(new ItemStack(id,1), "Item"+UtilMath.r(100)));
   		this.droppedItem[0].setVelocity(p.getLocation().getDirection());
   		UtilInv.remove(p, new ItemStack(id,1),1);
   		this.player=p;
   		this.handler=handler;
	}
	
	public LaunchItem(Player p,int sec,final LaunchItemEventHandler handler) {
		this.time=System.currentTimeMillis()+TimeSpan.SECOND*sec;
		this.droppedItem=new Entity[1];
   		this.droppedItem[0]=(Entity) p.getWorld().dropItemNaturally(p.getEyeLocation(),UtilItem.RenameItem(new ItemStack(p.getItemInHand().getType(),1,p.getItemInHand().getData().getData()), "Item"+UtilMath.r(100)));
   		this.droppedItem[0].setVelocity(p.getLocation().getDirection());
   		UtilInv.remove(p, p.getItemInHand().getType(), p.getItemInHand().getData().getData(), 1);
   		this.player=p;
   		this.handler=handler;
	}
	
	public void remove(){
		for(Entity e : droppedItem)e.remove();
	}
	
	public boolean check(){
		if(time<System.currentTimeMillis()){
			LaunchItemEvent e = new LaunchItemEvent(this);
			handler.onLaunchItem(e);
			remove();
			droppedItem=null;
			time=null;
			handler=null;
			player=null;
			return true;
		}
		return false;
	}
}
