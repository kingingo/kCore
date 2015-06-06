package me.kingingo.kcore.Packet.Packets;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.google.gson.reflect.TypeToken;

import lombok.Getter;
import me.kingingo.kcore.Enum.Team;
import me.kingingo.kcore.Packet.Packet;
import me.kingingo.kcore.Util.UtilInv;
import me.kingingo.kcore.Util.UtilList;
import me.kingingo.kcore.Versus.VersusKit;
import me.kingingo.kcore.Versus.VersusType;

public class VERSUS_SETTINGS extends Packet{

	@Getter
	private VersusType type;
	@Getter
	private HashMap<Player,Team> players;
	@Getter
	private VersusKit kit;
	
	public VERSUS_SETTINGS(VersusType type,VersusKit kit,HashMap<Player,Team> players){
		this.type=type;
		this.players=players;
		this.kit=kit;
	}
	
	public VERSUS_SETTINGS(String[] data){
		Set(data);
	}
	
	public VERSUS_SETTINGS(String data){
		Set(data);
	}
	
	public VERSUS_SETTINGS create(String[] packet){
		return new VERSUS_SETTINGS(packet);
	}
	
	public void Set(String data){
		Set(data.split("-/-"));
	}
	
	public void Set(String[] split){
		this.type=VersusType.valueOf(split[1]);
		try {
			this.players=UtilList.deserialize(split[2], new TypeToken<HashMap<Player,Team>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.kit=new VersusKit();
		try {
			this.kit.fromItemArray( UtilInv.itemStackArrayFromBase64(split[3]) );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getName(){
		return "VERSUS_SETTINGS";
	}
	
	public String toString(){
		try {
			return String.format(getName()+"-/-%s-/-%s-/-%s", type.name(), UtilList.serialize(players), UtilInv.itemStackArrayToBase64(kit.toItemArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "null";
	}
	
}
