package eu.epicpvp.kcore.Command.Commands;

import java.util.HashMap;
import java.util.Locale;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.Permission.PermissionManager;
import eu.epicpvp.kcore.Permission.PermissionPlayer;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilTime;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.Potion.Tier;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class CommandPotion implements CommandExecutor {

	private final String potions;
	private final HashMap<PotionEffectType, Potion> types = new HashMap<>();
	private PermissionManager manager;

	public CommandPotion(PermissionManager manager) {
		this.manager = manager;
		this.types.put(PotionEffectType.FIRE_RESISTANCE, new Potion(PotionType.FIRE_RESISTANCE, Tier.TWO, true));
		this.types.put(PotionEffectType.HEAL, new Potion(PotionType.INSTANT_HEAL, Tier.TWO, true));
		this.types.put(PotionEffectType.HARM, new Potion(PotionType.INSTANT_DAMAGE, Tier.TWO, true));
		this.types.put(PotionEffectType.INVISIBILITY, new Potion(PotionType.INVISIBILITY, Tier.TWO, true));
		this.types.put(PotionEffectType.JUMP, new Potion(PotionType.JUMP, Tier.TWO, true));
		this.types.put(PotionEffectType.POISON, new Potion(PotionType.POISON, Tier.TWO, true));
		this.types.put(PotionEffectType.REGENERATION, new Potion(PotionType.REGEN, Tier.TWO, true));
		this.types.put(PotionEffectType.SPEED, new Potion(PotionType.SPEED, Tier.TWO, true));
		this.types.put(PotionEffectType.DAMAGE_RESISTANCE, new Potion(PotionType.STRENGTH, Tier.TWO, true));
		this.types.put(PotionEffectType.SLOW, new Potion(PotionType.SLOWNESS, Tier.TWO, true));
		this.types.put(PotionEffectType.NIGHT_VISION, new Potion(PotionType.NIGHT_VISION, Tier.TWO, true));
		this.types.put(PotionEffectType.WEAKNESS, new Potion(PotionType.WEAKNESS, Tier.TWO, true));
		StringBuilder sb = new StringBuilder();
		for (PotionEffectType type : types.keySet()) {
			sb.append(type.getName()).append(", ");
		}

		this.potions = sb.substring(0, sb.length() - 2);
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "potion", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		Player player = (Player) sender;
		if (player.hasPermission(PermissionType.POTION.getPermissionToString())) {
			if (args.length == 0) {
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/potion [potion]");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + potions);
			} else {
				String s = UtilTime.getTimeManager().check(cmd.getName(), player);
				if (s != null) {
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "USE_BEFEHL_TIME", s));
				} else {
					PotionEffectType mob = PotionEffectType.getByName(args[0]);

					if (mob == null || !types.containsKey(mob)) {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "POTION_TYPE_NOT_FOUND"));
						return false;
					}
					if (!player.hasPermission(PermissionType.POTION.getPermissionToString() + "." + mob.getName().toLowerCase(Locale.ENGLISH))) {
						if (!player.hasPermission(PermissionType.POTION_ALL.getPermissionToString())) {
							player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "NO_PERMISSION"));
							return false;
						}
					}

					int amount = 1;

					PermissionPlayer pplayer = manager.getPermissionPlayer(player);
					for (int i = 130; i > 0; i--) {
						if (pplayer.hasPermission(PermissionType.POTION_AMOUNT.getPermissionToString() + "." + i)) {
							amount = i;
							break;
						}
					}

//					for (Group g : manager.getPermissionPlayer(player).getGroups()) {
//						for (Permission perm : g.getPermissions()) {
//							String permStr = perm.getPermission();
//							if (permStr.contains(PermissionType.POTION_AMOUNT.getPermissionToString() + ".")) {
//								amount = Integer.valueOf(permStr.substring(permStr.lastIndexOf(".") + 1));
//								break;
//							}
//						}
//					}
//
//					if (amount == 1) {
//						for (Permission perm : manager.getPermissionPlayer(player).getPermissions()) {
//							String permStr = perm.getPermission();
//							if (permStr.contains(PermissionType.POTION_AMOUNT.getPermissionToString() + ".")) {
//								amount = Integer.valueOf(permStr.substring(permStr.lastIndexOf(".") + 1));
//								break;
//							}
//						}
//					}

					Long l = UtilTime.getTimeManager().hasPermission(player, cmd.getName());
					if (l != 0) {
						UtilTime.getTimeManager().add(cmd.getName(), player, l);
					}

					if (amount > 64) {
						for (int i = 0; i < (amount / 64); i++)
							player.getInventory().addItem(this.types.get(mob).toItemStack(64));

						int i = amount % 64;
						if (i > 0) player.getInventory().addItem(this.types.get(mob).toItemStack(i));
					} else if (amount > 0) {
						player.getInventory().addItem(this.types.get(mob).toItemStack(amount));
					}
					player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "POTION_GOT"));
				}
			}
		}
		return false;
	}
}
