package eu.epicpvp.kcore.Gilden.Commands;

import org.bukkit.entity.Player;

import eu.epicpvp.kcore.Gilden.GildenManager;
import eu.epicpvp.kcore.Translation.TranslationManager;

public class Ranking {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==1){
		   manager.Ranking(p);
		}else{
			p.sendMessage(TranslationManager.getText(p, "GILDE_PREFIX")+"/gilde ranking");
		}
	}
	
}
