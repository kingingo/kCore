package me.kingingo.kcore.Kit.Perks;

import me.kingingo.kcore.Command.CommandHandler;
import me.kingingo.kcore.Command.CommandHandler.Sender;
import me.kingingo.kcore.Kit.Perk;
import me.kingingo.kcore.Language.Language;
import me.kingingo.kcore.Util.UtilItem;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PerkItemName extends Perk implements CommandExecutor{
	
	public PerkItemName(CommandHandler cmd) {
		super("ItemName",UtilItem.RenameItem(new ItemStack(Material.NAME_TAG),"§eItemName"));
		if(cmd!=null)cmd.register(PerkItemName.class, this);
	}
	
	public PerkItemName() {
		super("ItemName");
	}
	
	@EventHandler
	public void Pickup(PlayerPickupItemEvent ev){
		if(getPerkData().hasPlayer(this, ev.getPlayer())){
			if(ev.getItem().getItemStack().getType()==Material.IRON_AXE||ev.getItem().getItemStack().getType()==Material.DIAMOND_AXE||ev.getItem().getItemStack().getType()==Material.WOOD_AXE||ev.getItem().getItemStack().getType()==Material.GOLD_AXE){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Axt von§6 "+ev.getPlayer().getName());
			}else if(ev.getItem().getItemStack().getType()==Material.IRON_PICKAXE||ev.getItem().getItemStack().getType()==Material.DIAMOND_PICKAXE||ev.getItem().getItemStack().getType()==Material.WOOD_PICKAXE||ev.getItem().getItemStack().getType()==Material.GOLD_PICKAXE){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Spitzhacke von§6 "+ev.getPlayer().getName());
			}else if(ev.getItem().getItemStack().getType()==Material.IRON_SWORD||ev.getItem().getItemStack().getType()==Material.DIAMOND_SWORD||ev.getItem().getItemStack().getType()==Material.WOOD_SWORD||ev.getItem().getItemStack().getType()==Material.GOLD_SWORD){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Schwert von§6 "+ev.getPlayer().getName());
			}else if(ev.getItem().getItemStack().getType()==Material.IRON_SPADE||ev.getItem().getItemStack().getType()==Material.DIAMOND_SPADE||ev.getItem().getItemStack().getType()==Material.WOOD_SPADE||ev.getItem().getItemStack().getType()==Material.GOLD_SPADE){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Schaufel von§6 "+ev.getPlayer().getName());
			}else if(ev.getItem().getItemStack().getType()==Material.IRON_HOE||ev.getItem().getItemStack().getType()==Material.DIAMOND_HOE||ev.getItem().getItemStack().getType()==Material.WOOD_HOE||ev.getItem().getItemStack().getType()==Material.GOLD_HOE){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Harke von§6 "+ev.getPlayer().getName());
			}else if(ev.getItem().getItemStack().getTypeId()>=298&&ev.getItem().getItemStack().getTypeId()<=317){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Ruestungsteil von§6 "+ev.getPlayer().getName());
			}else if(ev.getItem().getItemStack().getType()==Material.BOW){
				UtilItem.RenameItem(ev.getItem().getItemStack(), "§3Bogen von§6 "+ev.getPlayer().getName());
			}
		}
	}

	@me.kingingo.kcore.Command.CommandHandler.Command(command = "itemname", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, Command cmd, String arg2,String[] args) {
		Player p = (Player)cs;
		
		if(getPerkData().hasPlayer(this, p)){
			if(args.length==0){
				p.sendMessage(Language.getText(p, "PREFIX")+"§c/itemname [Name]");
			}else{
				if(p.getItemInHand()!=null&&p.getItemInHand().getType()!=Material.AIR){
					UtilItem.RenameItem(p.getItemInHand(), args[0].replaceAll("&", "§"));
					p.sendMessage(Language.getText(p, "PREFIX")+"§aDas Item wurde umbenannt zu §e"+args[0].replaceAll("&", "§"));
				}else{
					p.sendMessage(Language.getText(p, "PREFIX")+"§cDu musst ein Item in der Hand halten.");
				}
			}
		}
		return false;
	}

}
