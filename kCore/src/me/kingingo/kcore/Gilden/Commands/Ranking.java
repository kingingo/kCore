package me.kingingo.kcore.Gilden.Commands;

import java.sql.ResultSet;

import me.kingingo.kcore.Enum.Text;
import me.kingingo.kcore.Gilden.GildenManager;
import me.kingingo.kcore.MySQL.MySQL;

import org.bukkit.entity.Player;

public class Ranking {

	public static void use(Player p,String[] args,GildenManager manager){
		if(args.length==0){
		   manager.Ranking(p);
		}else{
			p.sendMessage(Text.GILDE_PREFIX.getText()+" /gilde ranking");
		}
	}
	
}
