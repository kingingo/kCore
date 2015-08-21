package me.kingingo.kcore.Command.Admin;

import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Permission.kPermission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFlyspeed implements CommandExecutor{
	
	private Player player;
	
	@me.kingingo.kcore.Command.CommandHandler.Command(command = "flyspeed", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,String[] args) {
		player = (Player)sender;
		if(player.hasPermission(kPermission.FLYSPEED.getPermissionToString())){
			if(args.length==0){
				player.sendMessage(Language.getText(player, "PREFIX")+"/flyspeed [1-10]");
			}else if(args.length==1){
				if(player.getAllowFlight()){
					try{
						float speed = getRealMoveSpeed(getMoveSpeed(args[0]),true,false);
						player.setFlySpeed(speed);
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "kFLY_SPEED",args[0]));
					}catch(NumberFormatException e){
						player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "NO_INTEGER"));
					}
				}else{
					player.sendMessage(Language.getText(player, "PREFIX")+Language.getText(player, "kFLY_NOT_ON"));
				}
			}
		}
		return false;
	}
	
	private double max(){
		return Math.abs(0.8);
	}
	
	private float getMoveSpeed(String moveSpeed) throws NumberFormatException
	  {
	    float userSpeed;
	    try
	    {
	      userSpeed = Float.parseFloat(moveSpeed);
	      if (userSpeed > 10.0F)
	      {
	        userSpeed = 10.0F;
	      }
	      else if (userSpeed < 1.0E-004F)
	      {
	        userSpeed = 1.0E-004F;
	      }
	    }
	    catch (NumberFormatException e)
	    {
	      throw new NumberFormatException();
	    }
	    return userSpeed;
	  }
	
	private float getRealMoveSpeed(float userSpeed, boolean isFly, boolean isBypass)
	  {
	    float defaultSpeed = isFly ? 0.1F : 0.2F;
	    float maxSpeed = 1.0F;
	    if (!isBypass)
	    {
	      maxSpeed = (float)max();
	    }

	    if (userSpeed < 1.0F)
	    {
	      return defaultSpeed * userSpeed;
	    }

	    float ratio = (userSpeed - 1.0F) / 9.0F * (maxSpeed - defaultSpeed);
	    return ratio + defaultSpeed;
	  }
}
