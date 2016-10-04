package eu.epicpvp.kcore.newGilde;

import org.bukkit.entity.Player;

import eu.epicpvp.datenclient.client.LoadedPlayer;
import eu.epicpvp.datenclient.gilde.GildManager;
import eu.epicpvp.datenclient.gilde.GildSection;
import eu.epicpvp.datenclient.gilde.Gilde;
import dev.wolveringer.gilde.GildeType;
import dev.wolveringer.nbt.NBTTagCompound;
import eu.epicpvp.kcore.Listener.kListener;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;

public class GildeHandler extends kListener{

	@Getter
	private GildManager gildeManager;
	private GildeType type;

	public GildeHandler(GildeType type){
		super(UtilServer.getPluginInstance(),"GildeHandler");
		this.type=type;
		this.gildeManager=new GildManager(UtilServer.getClient());
		UtilServer.setGildeHandler(this);
	}

	public void saveData(int playerId){
		getSection(playerId).saveCostumData();
	}

	public NBTTagCompound getData(int playerId){
		GildSection section = getSection(playerId);
		if(section.getCostumData()==null)section.reloadDataSync();
		return section.getCostumData();
	}

	public GildeType getType(){
		return type;
	}

	public boolean areGildeFriends(Player m1, Player m2){
		return areGildeFriends(UtilPlayer.getPlayerId(m1), UtilPlayer.getPlayerId(m2));
	}

	public boolean areGildeFriends(int m1, int m2){
		GildSection sec1 = getSection( m1 );

		if(sec1!=null){
			return sec1.getPlayers().contains(m2);
		}
		return false;
	}

	public boolean hasGilde(Player player){
		return hasGilde(UtilPlayer.getPlayerId(player));
	}

	public boolean hasGilde(int playerId){
		return (getSection(playerId)!=null);
	}

	public GildSection getSection(LoadedPlayer player){
		return getSection(player.getPlayerId());
	}

	public GildSection getSection(Player player){
		return getSection(UtilPlayer.getPlayerId(player));
	}

	public Gilde getGilde(Player player){
		return getGilde(UtilPlayer.getPlayerId(player));
	}

	public Gilde getGilde(int playerId){
		return getGildeManager().getGildeSync(UtilServer.getClient().getPlayer(playerId), getType());
	}

	public GildSection getSection(int playerId){
		Gilde gilde = getGilde(playerId);

		if(gilde!=null){
			GildSection section = gilde.getSelection(getType());
			if(section!=null&&section.isActive()){
				return section;
			}
		}
		return null;
	}
}
