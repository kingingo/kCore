package me.kingingo.kcore.Packet.Packets;

import java.util.UUID;

import org.bukkit.Material;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kcore.Packet.Packet;

public class PLAYER_DELIVERY extends Packet{

	@Setter
	@Getter
	private String player;
	@Getter
	@Setter
	private UUID uuid;
	@Getter
	@Setter
	private Material material;
	
	public PLAYER_DELIVERY(){}
	
	public PLAYER_DELIVERY(String[] packet){
		Set(packet);
	}
	
	public PLAYER_DELIVERY(String player,UUID uuid,Material material){
		this.player=player;
		this.uuid=uuid;
		this.material=material;
	}
	
	public PLAYER_DELIVERY create(String[] packet){
		return new PLAYER_DELIVERY(packet);
	}
	
	public String getName(){
		return "PLAYER_DELIVERY";
	}
	
	public void Set(String[] split){
		this.player=split[1];
		this.uuid=UUID.fromString(split[2]);
		this.material=Material.getMaterial(split[3]);
	}
	
	public void Set(String packet){
		player=packet;
	}
	
	public String toString(){
		return String.format(getName()+"-/-%s-/-%s-/-%s", player,uuid,material);
	}
	
}
