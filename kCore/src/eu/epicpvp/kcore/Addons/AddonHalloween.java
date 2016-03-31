package eu.epicpvp.kcore.Addons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.PacketAPI.Packets.kPacketPlayOutEntityEquipment;
import eu.epicpvp.kcore.PacketAPI.packetlistener.event.PacketListenerSendEvent;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;

public class AddonHalloween extends kListener{

	@Getter
	private ItemStack pumpkin;
	
	public AddonHalloween(JavaPlugin instance) {
		super(instance, "AddonHalloween");
		this.pumpkin=UtilItem.RenameItem(new ItemStack(Material.PUMPKIN), "§6§lHelloween Pumpkin");
		UtilServer.createPacketListener(instance);
		setPumpkin();
	}
	
	public void setPumpkin(){
		kPacketPlayOutEntityEquipment packet = null;
		for(Player p1 : UtilServer.getPlayers()){
			for(Player p : UtilServer.getPlayers()){
				if(p1.getUniqueId()==p.getUniqueId())continue;
				
				if(packet==null){
					packet=new kPacketPlayOutEntityEquipment(p.getEntityId(), kPacketPlayOutEntityEquipment.EquipmentSlot.HELM, pumpkin.clone());
				}else{
					packet.setEntityID(p.getEntityId());
				}
				
				UtilPlayer.sendPacket(p1, packet);
			}
		}
		packet=null;
	}
	
	@EventHandler
	public void Send(PacketListenerSendEvent ev){
		if(ev.getPlayer()!=null&&ev.getPacket()!=null){
			if(ev.getPacket() instanceof PacketPlayOutEntityEquipment){
				kPacketPlayOutEntityEquipment packet = new kPacketPlayOutEntityEquipment(((PacketPlayOutEntityEquipment)ev.getPacket()));
				
				if(ev.getPlayer().getEntityId() != packet.getEntityID() && packet.getSlot() == kPacketPlayOutEntityEquipment.EquipmentSlot.HELM.getS()){
					packet.setItemStack( pumpkin.clone() );
					ev.setPacket(packet.getPacket());
				}
				
				packet.setPacket(null);
				packet=null;
			}
		}
	}

}
