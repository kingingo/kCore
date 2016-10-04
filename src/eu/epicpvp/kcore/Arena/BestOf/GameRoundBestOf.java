package eu.epicpvp.kcore.Arena.BestOf;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import eu.epicpvp.datenserver.definitions.dataserver.gamestats.GameType;
import eu.epicpvp.kcore.Arena.ArenaType;
import eu.epicpvp.kcore.Arena.GameRound;
import eu.epicpvp.kcore.Hologram.nametags.NameTagMessage;
import eu.epicpvp.kcore.Hologram.nametags.NameTagType;
import eu.epicpvp.kcore.Util.UtilPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import eu.epicpvp.kcore.kConfig.kConfig;
import lombok.Getter;

public class GameRoundBestOf extends GameRound{

	@Getter
	private HashMap<Integer,UUID> wins;
	@Getter
	private int round;
	@Getter
	private GameType[] types;
	@Getter
	private NameTagMessage nameTagMessage;

	public GameRoundBestOf(Player owner,Player player) {
		super(owner.getUniqueId(), new UUID[]{owner.getUniqueId(),player.getUniqueId()}, ArenaType._TEAMx2);
		kConfig config = UtilServer.getUserData().getConfig(owner);
		this.wins=new HashMap<>();
		this.types=new GameType[config.getInt("BestOf.Rounds")];
		this.nameTagMessage=new NameTagMessage(NameTagType.PACKET, UtilServer.getBestOf().getSpawn().clone().add(0, UtilServer.getLineY(config.getInt("BestOf.Rounds")), 0), new String[config.getInt("BestOf.Rounds")]);

		String t;
		for(int i = 0; i<config.getInt("BestOf.Rounds");i++){
			t=config.getString("BestOf.Round."+(i+1));
			nameTagMessage.getLines()[i]=" ";
			if(!t.equalsIgnoreCase("random")){
				types[i]=GameType.get(t);
			}
		}

		this.round=0;
		UtilServer.getBestOf().sendHologramm(this);
	}

	public boolean canNextRound(){
		return UtilPlayer.isOnline(getPlayers()[1]) && UtilPlayer.isOnline(getPlayers()[1]);
	}

	public UUID getWinner(){
		if(!wins.isEmpty()){
			int win = 0;

			for(UUID w : wins.values()){
				if(w.toString().equalsIgnoreCase(getOwner().toString()))win++;
			}

			if(types.length%2==0 && types.length == (round-1)){
				if(win == (wins.size()-win)){
					return null;
				}
			}

			return (win>(wins.size()-win) ? getOwner()  : targetPlayer());
		}

		return getOwner();
	}

	public UUID targetPlayer(){
		for(UUID p : getPlayers())if(!p.toString().equalsIgnoreCase(getOwner().toString()))return p;
		return null;
	}

	public void win(UUID player){
		if(UtilServer.getBestOf().getPlayers_name().containsKey(player)){
			if(types[this.round-1]!=null){
				if(nameTagMessage.getLines().length>=(this.round-1)){
					System.out.println("TYPE: "+types.length +" ROUND: "+this.round+" "+nameTagMessage.getLines().length);
					nameTagMessage.getLines()[this.round-1]="§7[§eWinner Runde "+(this.round)+"§7] "+types[this.round-1].name()+" §7§ §a"+UtilServer.getBestOf().getPlayers_name().get(player);
				};

			}else{
				nameTagMessage.getLines()[this.round-1]="§7[§eWinner Runde "+(this.round)+"§7] Random §7§ §a";
			}
			UtilServer.getBestOf().sendHologramm(this);
		}
		this.wins.put(this.round, player);
	}

	public boolean lastRound(){
		return (this.round>=this.types.length);
	}

	public GameType nextRound(){
		this.round++;

		if(this.round<=this.types.length){
			return this.types[this.round-1];
		}
		return GameType.NONE;
	}

	public void remove(){
		this.round=0;
		this.wins.clear();
		this.wins=null;
		setOwner(null);
		setPlayers(null);
		setType(null);
	}
}
