package eu.epicpvp.kcore.MysteryChest;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.GagdetShop.Gagdet.Gadget;
import eu.epicpvp.kcore.GagdetShop.Gagdet.MobGun;
import eu.epicpvp.kcore.GagdetShop.Gagdet.Pearl;
import eu.epicpvp.kcore.GagdetShop.Gagdet.PowerAxe;
import eu.epicpvp.kcore.GagdetShop.Gagdet.Ragebow;
import eu.epicpvp.kcore.GagdetShop.Gagdet.SlimeHead;
import eu.epicpvp.kcore.Kit.Kit;
import eu.epicpvp.kcore.Kit.KitType;
import eu.epicpvp.kcore.Kit.Perk;
import eu.epicpvp.kcore.Kit.Perks.PerkArrowFire;
import eu.epicpvp.kcore.Kit.Perks.PerkArrowPotionEffect;
import eu.epicpvp.kcore.Kit.Perks.PerkDeathDropOnly;
import eu.epicpvp.kcore.Kit.Perks.PerkDoubleJump;
import eu.epicpvp.kcore.Kit.Perks.PerkDoubleXP;
import eu.epicpvp.kcore.Kit.Perks.PerkDropper;
import eu.epicpvp.kcore.Kit.Perks.PerkEquipment;
import eu.epicpvp.kcore.Kit.Perks.PerkGetXP;
import eu.epicpvp.kcore.Kit.Perks.PerkGoldenApple;
import eu.epicpvp.kcore.Kit.Perks.PerkHat;
import eu.epicpvp.kcore.Kit.Perks.PerkHeal;
import eu.epicpvp.kcore.Kit.Perks.PerkHealPotion;
import eu.epicpvp.kcore.Kit.Perks.PerkItemName;
import eu.epicpvp.kcore.Kit.Perks.PerkLessDamage;
import eu.epicpvp.kcore.Kit.Perks.PerkLessDamageCause;
import eu.epicpvp.kcore.Kit.Perks.PerkNoDropsByDeath;
import eu.epicpvp.kcore.Kit.Perks.PerkNoFalldamage;
import eu.epicpvp.kcore.Kit.Perks.PerkNoFiredamage;
import eu.epicpvp.kcore.Kit.Perks.PerkNoHunger;
import eu.epicpvp.kcore.Kit.Perks.PerkNoPotion;
import eu.epicpvp.kcore.Kit.Perks.PerkNoWaterdamage;
import eu.epicpvp.kcore.Kit.Perks.PerkPotionByDeath;
import eu.epicpvp.kcore.Kit.Perks.PerkPotionClear;
import eu.epicpvp.kcore.Kit.Perks.PerkRespawnBuff;
import eu.epicpvp.kcore.Kit.Perks.PerkRunner;
import eu.epicpvp.kcore.Kit.Perks.PerkSneakDamage;
import eu.epicpvp.kcore.Kit.Perks.PerkSpawnByDeath;
import eu.epicpvp.kcore.Kit.Perks.PerkStopPerk;
import eu.epicpvp.kcore.Kit.Perks.PerkStrength;
import eu.epicpvp.kcore.MysteryChest.Items.MysteryItem;
import eu.epicpvp.kcore.Permission.PermissionType;
import eu.epicpvp.kcore.Permission.Group.GroupTyp;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.UtilItem;
import eu.epicpvp.kcore.Util.UtilNumber;

public class CommandMysteryChest implements CommandExecutor {

	private MysteryChestManager chestManager;

	public CommandMysteryChest(MysteryChestManager chestManager) {
		this.chestManager = chestManager;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "MysteryChest", alias = { "mc",
			"mchest" }, sender = Sender.PLAYER)
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		Player player = (Player) sender;

		if (player.isOp()) {
			if (args.length == 0) {
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/MysteryChest saveTemplate [Chest]");
				player.sendMessage(
						TranslationHandler.getText(player, "PREFIX") + "/MysteryChest createChest [Chest] [Template]");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX")
						+ "/MysteryChest addi [Chest] [Chance] [Perm] [GroupTyp] [CMD]");
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/MysteryChest start [Chest]");
			} else {
				if (args[0].equalsIgnoreCase("saveTemplate")) {
					if (args.length == 2) {
						chestManager.addBuilding(player, args[1]);
						player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§aDein Template §e" + args[1]
								+ "§a wurde gespeichert!");
					} else {
						player.sendMessage(
								TranslationHandler.getText(player, "PREFIX") + "/MysteryChest saveTemplate [Name]");
					}
				}else if (args[0].equalsIgnoreCase("give")) {
					if (args.length == 3) {
						chestManager.addAmount(player, UtilNumber.toInt(args[2]), args[1]);
						player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§aDu hast "+args[1]+" Chests erhalten!");
					} else {
						player.sendMessage(
								TranslationHandler.getText(player, "PREFIX") + "/MysteryChest give [Chest] [Amount]");
					}
				} else if (args[0].equalsIgnoreCase("createChest")) {
					if (args.length == 3) {
						chestManager.addChest(player.getItemInHand(), args[2], args[1]);
						player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§aDie Treasure Chest "
								+ args[1] + " mit dem Template " + args[2] + " wurde erstellt.");
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")
								+ "/MysteryChest createChest [Chest] [Template]");
					}
				} else if (args[0].equalsIgnoreCase("loadGadgets")) {
					if (args.length == 3) {
						Gadget[] gadgets = getGadgets();

						for (Gadget kit : gadgets) {
							chestManager.getChest(args[1]).addItem(
									UtilItem.RenameItem(kit.getItem(), kit.getItem().getItemMeta().getDisplayName()+ "§7 (§eGame§7)"),
									UtilNumber.toDouble(args[2]), "-",GroupTyp.GAME,
									"givegadget [p] "+kit.getName()+" R,50,250");
						}
						player.sendMessage(
								TranslationHandler.getText(player, "PREFIX") + "§aAlle Items wurden hinzugefügt");
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")
								+ "/MysteryChest loadGadgets [Chest] [Chance]");
					}
				}else if (args[0].equalsIgnoreCase("loadDisguise")) {
					if (args.length == 3) {
						MysteryItem[] kits = getDisguise();

						for (MysteryItem kit : kits) {
							chestManager.getChest(args[1]).addItem(
									UtilItem.RenameItem(kit,
											kit.getItemMeta().getDisplayName()+ "§7 (§eGame§7)"),
									UtilNumber.toDouble(args[2]), kit.getPermission(),GroupTyp.GAME,
									"k addperm [p] " + kit.getPermission() + " game");
						}
						player.sendMessage(
								TranslationHandler.getText(player, "PREFIX") + "§aAlle Items wurden hinzugefügt");
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")
								+ "/MysteryChest loadSheepWars [Chest] [Chance]");
					}
				} else if (args[0].equalsIgnoreCase("loadPetsGame")) {
					if (args.length == 3) {
						MysteryItem[] kits = getPets();

						for (MysteryItem kit : kits) {
							chestManager.getChest(args[1]).addItem(
									UtilItem.RenameItem(kit,
											kit.getItemMeta().getDisplayName()+ "§7 (§eGame§7)"),
									UtilNumber.toDouble(args[2]), kit.getPermission(),GroupTyp.GAME,
									"k addperm [p] " + kit.getPermission() + " game");
						}
						player.sendMessage(
								TranslationHandler.getText(player, "PREFIX") + "§aAlle Items wurden hinzugefügt");
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")
								+ "/MysteryChest loadSheepWars [Chest] [Chance]");
					}
				} else if (args[0].equalsIgnoreCase("loadPetsSky")) {
					if (args.length == 3) {
						MysteryItem[] kits = getPets();

						for (MysteryItem kit : kits) {
							chestManager.getChest(args[1]).addItem(
									UtilItem.RenameItem(kit,
											kit.getItemMeta().getDisplayName()+ "§7 (§eSkyBlock§7)"),
									UtilNumber.toDouble(args[2]), kit.getPermission(),GroupTyp.SKY,
									"k addperm [p] " + kit.getPermission() + " sky");
						}
						player.sendMessage(
								TranslationHandler.getText(player, "PREFIX") + "§aAlle Items wurden hinzugefügt");
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")
								+ "/MysteryChest loadSheepWars [Chest] [Chance]");
					}
				} else if (args[0].equalsIgnoreCase("loadPetsPvP")) {
					if (args.length == 3) {
						MysteryItem[] kits = getPets();

						for (MysteryItem kit : kits) {
							chestManager.getChest(args[1]).addItem(
									UtilItem.RenameItem(kit,
											kit.getItemMeta().getDisplayName()+ "§7 (§ePvP§7)"),
									UtilNumber.toDouble(args[2]), kit.getPermission(),GroupTyp.PVP,
									"k addperm [p] " + kit.getPermission() + " pvp");
						}
						player.sendMessage(
								TranslationHandler.getText(player, "PREFIX") + "§aAlle Items wurden hinzugefügt");
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")
								+ "/MysteryChest loadSheepWars [Chest] [Chance]");
					}
				}  else if (args[0].equalsIgnoreCase("loadPerksSky")) {
					if (args.length == 3) {
						Perk[] kits = getPerks();

						for (Perk kit : kits) {
							chestManager.getChest(args[1]).addItem(
									UtilItem.RenameItem(kit.getItem(),
											"§e[§c§l+§e] §aPerk§7: " + kit.getItem().getItemMeta().getDisplayName()
													+ "§7 (§eSkyBlock§7)"),
									UtilNumber.toDouble(args[2]), kit.getPermission().getPermissionToString(),GroupTyp.SKY,
									"k addperm [p] " + kit.getPermission().getPermissionToString() + " sky");
						}
						player.sendMessage(
								TranslationHandler.getText(player, "PREFIX") + "§aAlle Items wurden hinzugefügt");
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")
								+ "/MysteryChest loadSheepWars [Chest] [Chance]");
					}
				} else if (args[0].equalsIgnoreCase("loadPerksPvP")) {
					if (args.length == 3) {
						Perk[] kits = getPerks();

						for (Perk kit : kits) {
							chestManager.getChest(args[1]).addItem(
									UtilItem.RenameItem(kit.getItem(),
											"§e[§c§l+§e] §aPerk§7: " + kit.getItem().getItemMeta().getDisplayName()
													+ "§7 (§ePvP§7)"),
									UtilNumber.toDouble(args[2]), kit.getPermission().getPermissionToString(),GroupTyp.PVP,
									"k addperm [p] " + kit.getPermission().getPermissionToString() + " sky");
						}
						player.sendMessage(
								TranslationHandler.getText(player, "PREFIX") + "§aAlle Items wurden hinzugefügt");
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")
								+ "/MysteryChest loadSheepWars [Chest] [Chance]");
					}
				} else if (args[0].equalsIgnoreCase("loadSheepWars")) {
					if (args.length == 3) {
						Kit[] kits = getSheepKits();

						for (Kit kit : kits) {
							chestManager.getChest(args[1]).addItem(kit.getItem(), UtilNumber.toDouble(args[2]),
									kit.getPermission().getPermissionToString(),GroupTyp.GAME,
									"k addperm [p] " + kit.getPermission().getPermissionToString() + " game");
						}
						player.sendMessage(
								TranslationHandler.getText(player, "PREFIX") + "§aAlle Items wurden hinzugefügt");
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")
								+ "/MysteryChest loadSheepWars [Chest] [Chance]");
					}
				} else if (args[0].equalsIgnoreCase("loadSkyWars")) {
					if (args.length == 3) {
						Kit[] kits = getSkyWarsKits();

						for (Kit kit : kits) {
							chestManager.getChest(args[1]).addItem(kit.getItem(), UtilNumber.toDouble(args[2]),
									kit.getPermission().getPermissionToString(),GroupTyp.GAME,
									"k addperm [p] " + kit.getPermission().getPermissionToString() + " game");
						}
						player.sendMessage(
								TranslationHandler.getText(player, "PREFIX") + "§aAlle Items wurden hinzugefügt");
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")
								+ "/MysteryChest loadSkyWars [Chest] [Chance]");
					}
				} else if (args[0].equalsIgnoreCase("addi")) {
					if (args.length == 6) {
						chestManager.getChest(args[1]).addItem(player.getItemInHand(), UtilNumber.toDouble(args[2]),
								args[3],GroupTyp.get(args[4]), args[5]);
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")
								+ "§aDas Item wurde zu der Chest hinzugefügt!");
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX")
								+ "/MysteryChest addi [Chest] [Chance] [Perm] [GroupTyp] [CMD]");
					}
				} else if (args[0].equalsIgnoreCase("start")) {
					if (args.length == 2) {
						MysteryChest c = chestManager.getChest(args[1]);

						if (c != null) {
							c.start(player);
						}
					} else {
						player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "/MysteryChest start [Name]");
					}
				}
			}
		}
		return false;
	}
	
	public static MysteryItem[] getDisguise(){
		return new MysteryItem[]{
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)54), "§e§l[§c+§e§l]§a Zombie Disguise"), 20, PermissionType.DISGUISE_ZOMBIE.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)58), "§e§l[§c+§e§l]§a Enderman Disguise"), 20, PermissionType.DISGUISE_ENDERMAN.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)61), "§e§l[§c+§e§l]§a Blaze Disguise"), 20, PermissionType.DISGUISE_BLAZE.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)91), "§e§l[§c+§e§l]§a Wolf Disguise"), 20, PermissionType.DISGUISE_WOLF.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)92), "§e§l[§c+§e§l]§a Pig Disguise"), 20, PermissionType.DISGUISE_PIG.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)91), "§e§l[§c+§e§l]§a Schaf Disguise"), 20, PermissionType.DISGUISE_SHEEP.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)100), "§e§l[§c+§e§l]§a Creeper Disguise"), 20, PermissionType.DISGUISE_CREEPER.getPermissionToString(), GroupTyp.GAME, ""),
		};
	}
	
	public static MysteryItem[] getPets(){
		return new MysteryItem[]{
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)66), "§e§l[§c+§e§l]§a Witch Pet"), 20, PermissionType.PET_WITCH.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)54), "§e§l[§c+§e§l]§a Zombie Pet"), 20, PermissionType.PET_ZOMBIE.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)95), "§e§l[§c+§e§l]§a Wolf Pet"), 20, PermissionType.PET_WOLF.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)90), "§e§l[§c+§e§l]§a Pig Pet"), 20, PermissionType.PET_PIG.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)91), "§e§l[§c+§e§l]§a Schaf Pet"), 20, PermissionType.PET_SHEEP.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.IRON_BLOCK), "§e§l[§c+§e§l]§a IronGolem Pet"), 20, PermissionType.PET_IRON_GOLEM.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)92), "§e§l[§c+§e§l]§a Kuh Pet"), 20, PermissionType.PET_COW.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)52), "§e§l[§c+§e§l]§a Spider Pet"), 20, PermissionType.PET_SPIDER.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)100), "§e§l[§c+§e§l]§a Pferd Pet"), 20, PermissionType.PET_HORSE.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)101), "§e§l[§c+§e§l]§a Hase Pet"), 20, PermissionType.PET_RABBIT.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)94), "§e§l[§c+§e§l]§a Squid Pet"), 20, PermissionType.PET_SQUID.getPermissionToString(), GroupTyp.GAME, ""),
			new MysteryItem(UtilItem.RenameItem(new ItemStack(Material.MONSTER_EGG,1,(byte)98), "§e§l[§c+§e§l]§a Ocelot Pet"), 20, PermissionType.PET_OCELOT.getPermissionToString(), GroupTyp.GAME, ""),
		};
	}

	public static Gadget[] getGadgets(){
		return new Gadget[]{
			new MobGun(null),
			new Pearl(null),
			new PowerAxe(null),
			new Ragebow(null),
			new SlimeHead(null),
		};
	}
	
	public static Perk[] getPerks() {
		return new Perk[] { new PerkStrength(), new PerkNoPotion(PotionEffectType.POISON), new PerkNoWaterdamage(),
				new PerkArrowPotionEffect(), new PerkHat(), new PerkGoldenApple(), new PerkNoHunger(),
				new PerkHealPotion(1), new PerkNoFiredamage(), new PerkRunner(0.35F), new PerkDoubleJump(),
				new PerkDoubleXP(), new PerkDropper(), new PerkGetXP(), new PerkPotionClear(), new PerkItemName(null) };
	}

	public static Kit[] getSheepKits() {
		return new Kit[] {
				new Kit("§e[§c§l+§e] §aKit§7: ArrowMan§7 (§eSheepWars§7)",
						new String[] { "Der ArrowMan besitzt die 30% Chance", "das seine Pfeile brennen." },
						new ItemStack(Material.ARROW), PermissionType.SHEEPWARS_KIT_ARROWMAN, KitType.KAUFEN, 2000, 500,
						new Perk[] { new PerkArrowFire(30) }),
				new Kit("§e[§c§l+§e] §aKit§7: ItemStealer§7 (§eSheepWars§7)",
						new String[] { "Der ItemStealer hat nach seinem", " Tod 10 sekunden um seine",
								"Sachen aufzuheben solange", "kann er sie nur aufheben." },
						new ItemStack(Material.SHEARS), PermissionType.SHEEPWARS_KIT_ITEMSTEALER, KitType.KAUFEN, 2000,
						500, new Perk[] { new PerkDeathDropOnly(10) }),
				new Kit("§e[§c§l+§e] §aKit§7: Healer§7 (§eSheepWars§7)", new String[] { "Der Healer heilt schneller." },
						new ItemStack(Material.APPLE), PermissionType.SHEEPWARS_KIT_HEALER, KitType.KAUFEN, 2000, 500,
						new Perk[] { new PerkHeal(5) }),
				new Kit("§e[§c§l+§e] §aKit§7: Dropper§7 (§eSheepWars§7)",
						new String[] { "Der Dropper lässt seine Sachen", "beim Tod nicht fallen." },
						new ItemStack(Material.DROPPER), PermissionType.SHEEPWARS_KIT_DROPPER, KitType.KAUFEN, 2000,
						500, new Perk[] { new PerkNoDropsByDeath() }),
				new Kit("§e[§c§l+§e] §aKit§7: Anker§7 (§eSheepWars§7)",
						new String[] { "Der Anker bekommt kein Rückstoß." }, new ItemStack(Material.ANVIL),
						PermissionType.SHEEPWARS_KIT_ANKER, KitType.KAUFEN, 2000, 500, new Perk[] {

						}), new Kit("§e[§c§l+§e] §aKit§7: Perker§7 (§eSheepWars§7)", new String[] { "Der Perker stoppt beim angreiffen", "vom Gegner die Perk's" }, new ItemStack(Material.TORCH), PermissionType.SHEEPWARS_KIT_PERKER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkStopPerk(10) }), new Kit("§e[§c§l+§e] §aKit§7: TNTer§7 (§eSheepWars§7)", new String[] { "Der TNT hat die 10% Chance", "das an seiner Todes stelle", "ein TNT spawnt." }, new ItemStack(Material.TNT), PermissionType.SHEEPWARS_KIT_TNTER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkSpawnByDeath(EntityType.PRIMED_TNT, 10) }), new Kit("§e[§c§l+§e] §aKit§7: Buffer§7 (§eSheepWars§7)", new String[] { "Der Buffer bekommt wenn er Respawn", "Feuerresistance und Schadenresistance." }, new ItemStack(Material.POTION), PermissionType.SHEEPWARS_KIT_BUFFER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkRespawnBuff(new PotionEffect[] { new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 20, 2), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 20, 2) }) }), new Kit("§e[§c§l+§e] §aKit§7: Knight§7 (§eSheepWars§7)", new String[] { "Der Knight bekommt beim Sneaken", "höchstens 1 Herz schaden", "wenn er angegriffen wird." }, new ItemStack(Material.DIAMOND_CHESTPLATE), PermissionType.SHEEPWARS_KIT_KNIGHT, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkSneakDamage(3.0) }), new Kit("§e[§c§l+§e] §aKit§7: TheDeath§7 (§eSheepWars§7)", new String[] { "Der TheDeath drop beim Tod", "ein Blindheits Trank." }, new ItemStack(Material.IRON_SWORD), PermissionType.SHEEPWARS_KIT_THEDEATH, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkPotionByDeath(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 1)) }), new Kit("§e[§c§l+§e] §aKit§7: Springer§7 (§eSheepWars§7)", new String[] { "Der Springer bekommt kein Fallschaden." }, new ItemStack(Material.FEATHER), PermissionType.SHEEPWARS_KIT_SPRINGER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkNoFalldamage() }), new Kit("§e[§c§l+§e] §aKit§7: OldRush§7 (§eSheepWars§7)", new String[] { "Der OldRush kriegt kein Fallschaden", "15% Chance das seine Pfeile brennen", "10% Chance das beim Tod ein TNT Spawn." }, new ItemStack(Material.BED), PermissionType.SHEEPWARS_KIT_OLD_RUSH, KitType.SPEZIAL_KIT, 0, new Perk[] { new PerkNoFiredamage(), new PerkArrowFire(15), new PerkNoFalldamage(), new PerkSpawnByDeath(EntityType.PRIMED_TNT, 10) }) };
	}

	public static Kit[] getSkyWarsKits() {
		return new Kit[] {
				new Kit("§e[§c§l+§e] §aKit§7: Buchhalter§7 (§eSkyWars§7)", new String[] {
						"§8x1§7 Leder Ruestung", "§8x1§7 Feder mit Sch§rfe II", "§8x1§7 Buch und Feder",
						"§8x64§7 Buecherregale" }, new ItemStack(Material.BOOK_AND_QUILL),
						PermissionType.SKYWARS_KIT_BUCHHALTER, KitType.KAUFEN, 2000, 500,
						new Perk[] {
								new PerkEquipment(new ItemStack[] { new ItemStack(Material.BOOK_AND_QUILL),
										new ItemStack(Material.BOOKSHELF, 64),
										UtilItem.EnchantItem(new ItemStack(Material.FEATHER), Enchantment.DAMAGE_ALL,
												2),
										UtilItem.LSetColor(new ItemStack(Material.LEATHER_HELMET), DyeColor.GRAY),
										UtilItem.LSetColor(new ItemStack(Material.LEATHER_CHESTPLATE), DyeColor.GRAY),
										UtilItem.LSetColor(new ItemStack(Material.LEATHER_LEGGINGS), DyeColor.GRAY),
										UtilItem.LSetColor(new ItemStack(Material.LEATHER_BOOTS), DyeColor.GRAY) }), }),
				new Kit("§e[§c§l+§e] §aKit§7: Vodo§7 (§eSkyWars§7)",
						new String[] { "§8x1§7 Holzschwert mit Schärfe II", "§8x1§7 Lederbrustpanzer mit rfe III",
								"§8x2§7 Wurftr§nke der Langsamkeit" },
						new ItemStack(Material.POTION), PermissionType.SKYWARS_KIT_WUDU, KitType.KAUFEN, 2000, 500,
						new Perk[] { new PerkEquipment(new ItemStack[] {
								UtilItem.EnchantItem(new ItemStack(Material.WOOD_SWORD), Enchantment.DAMAGE_ALL, 2),
								UtilItem.EnchantItem(new ItemStack(Material.LEATHER_CHESTPLATE),
										Enchantment.PROTECTION_ENVIRONMENTAL, 3),
								new ItemStack(Material.POTION, 2, (byte) 16394) }), }),
				new Kit("§e[§c§l+§e] §aKit§7: Lohe§7 (§eSkyWars§7)",
						new String[] { "§8x1§7 Leder Ruestung", "§8x1§7 Lohenrute mit Verbrennung I",
								"§8x10§7 Feuerkugeln" },
						new ItemStack(Material.BLAZE_ROD), PermissionType.SKYWARS_KIT_LOHE, KitType.KAUFEN, 2000, 500,
						new Perk[] {
								new PerkEquipment(new ItemStack[] { new ItemStack(Material.FIREBALL, 10),
										UtilItem.EnchantItem(new ItemStack(Material.BLAZE_ROD), Enchantment.FIRE_ASPECT,
												1),
										UtilItem.LSetColor(new ItemStack(Material.LEATHER_HELMET), DyeColor.YELLOW),
										UtilItem.LSetColor(new ItemStack(Material.LEATHER_CHESTPLATE), DyeColor.YELLOW),
										UtilItem.LSetColor(new ItemStack(Material.LEATHER_LEGGINGS), DyeColor.YELLOW),
										UtilItem.LSetColor(new ItemStack(Material.LEATHER_BOOTS),
												DyeColor.YELLOW) }), }),
				new Kit("§e[§c§l+§e] §aKit§7: Sensenmann§7 (§eSkyWars§7)",
						new String[] { "§8x1§7 Goldene Sense mit Sch§rfe II", "§8x64§7 Kohlebl§cke",
								"§8x1§7 Lederschuhe mit Protection I", "§8x1§7 Lederhose mit Protection I",
								"§8x1§7 Lederbrustpanzer mit Dron I", "§8x1§7 Lederhelm mit Protection I" },
						new ItemStack(Material.COAL_BLOCK), PermissionType.SKYWARS_KIT_SENSENMAN, KitType.KAUFEN, 2000,
						500,
						new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.COAL_BLOCK, 64),
								UtilItem.EnchantItem(new ItemStack(Material.GOLD_HOE), Enchantment.DAMAGE_ALL, 2),
								UtilItem.EnchantItem(new ItemStack(Material.LEATHER_BOOTS),
										Enchantment.PROTECTION_ENVIRONMENTAL, 1),
								UtilItem.EnchantItem(new ItemStack(Material.LEATHER_CHESTPLATE), Enchantment.THORNS, 1),
								UtilItem.EnchantItem(new ItemStack(Material.LEATHER_LEGGINGS),
										Enchantment.PROTECTION_ENVIRONMENTAL, 1),
								UtilItem.EnchantItem(new ItemStack(Material.LEATHER_HELMET),
										Enchantment.PROTECTION_ENVIRONMENTAL, 1) }), }),
				new Kit("§e[§c§l+§e] §aKit§7: MLG§7 (§eSkyWars§7)",
						new String[] { "§8x64§7 Holz", "§8x12§7 TNT", "§8x1§7 Redstone Fackel", "§8x2§7 Wasser Eimer",
								"§8x1§7 Boot", "§8x8§7 Spinnenweben" },
						new ItemStack(Material.WATER_BUCKET), PermissionType.SKYWARS_KIT_MLG, KitType.KAUFEN, 2000, 500,
						new Perk[] {
								new PerkEquipment(new ItemStack[] { new ItemStack(Material.WOOD, 64),
										new ItemStack(Material.TNT, 12), new ItemStack(Material.REDSTONE_TORCH_ON, 1),
										new ItemStack(Material.WATER_BUCKET, 2), new ItemStack(Material.BOAT, 1),
										new ItemStack(Material.WEB, 8) }),
								new PerkLessDamage(5, EntityType.PRIMED_TNT),
								new PerkLessDamageCause(40, DamageCause.FALL) }),
				new Kit("§e[§c§l+§e] §aKit§7: Spinne§7 (§eSkyWars§7)",
						new String[] { "§8x12§7 Spinnweben", "§8x1§7 Sprungkraft II Trank", "§8x1§7 Angel",
								"§8x2§7 Wasser Eimer" },
						new ItemStack(Material.WEB), PermissionType.SKYWARS_KIT_SPINNE, KitType.KAUFEN, 2000, 500,
						new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.FISHING_ROD),
								new ItemStack(Material.POTION, 1, (byte) 8235), new ItemStack(Material.WEB, 12),
								new ItemStack(Material.WATER_BUCKET, 2) }) }),
				new Kit("§e[§c§l+§e] §aKit§7: Doktor§7 (§eSkyWars§7)",
						new String[] { "§8x1§7 Weiße Leder Ruestung", "§8x2§7 Heil Tr§nke II", "§8x2§7 Schere",
								"§8x8§7 Spinnenweben" },
						UtilItem.LSetColor(new ItemStack(Material.LEATHER_CHESTPLATE), DyeColor.WHITE),
						PermissionType.SKYWARS_KIT_DOKTOR, KitType.KAUFEN, 2000, 500,
						new Perk[] { new PerkEquipment(new ItemStack[] {
								UtilItem.LSetColor(new ItemStack(Material.LEATHER_HELMET), DyeColor.WHITE),
								UtilItem.LSetColor(new ItemStack(Material.LEATHER_CHESTPLATE), DyeColor.WHITE),
								UtilItem.LSetColor(new ItemStack(Material.LEATHER_LEGGINGS), DyeColor.WHITE),
								UtilItem.LSetColor(new ItemStack(Material.LEATHER_BOOTS), DyeColor.WHITE),
								new ItemStack(Material.SHEARS), new ItemStack(Material.POTION, 1, (byte) 8229) }) }),
				new Kit("§e[§c§l+§e] §aKit§7: Dagobert Duck§7 (§eSkyWars§7)",
						new String[] { "§8x1§7 Goldschwert", "§8x2§7 Gold§pfel", "§8x1§7 Goldbrustpanzer",
								"§8x8§7 Gold" },
						new ItemStack(Material.GOLD_CHESTPLATE), PermissionType.SKYWARS_KIT_DAGOBERT_DUCK,
						KitType.KAUFEN, 2000, 500,
						new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.GOLD_SWORD),
								new ItemStack(Material.GOLDEN_APPLE, 2), new ItemStack(Material.GOLD_CHESTPLATE),
								new ItemStack(Material.GOLD_INGOT, 8) }) }),
				new Kit("§e[§c§l+§e] §aKit§7: Forster§7 (§eSkyWars§7)",
						new String[] { "§8x1§7 Steinaxt Schärfe 1", "§8x1§7 Schere", "§8x16§7 Laub", "§8x5§7 Äpfel" },
						new ItemStack(Material.LEAVES), PermissionType.SKYWARS_KIT_FORSTER, KitType.KAUFEN, 2000, 500,
						new Perk[] { new PerkEquipment(new ItemStack[] {
								UtilItem.EnchantItem(new ItemStack(Material.STONE_AXE), Enchantment.DAMAGE_ALL, 1),
								new ItemStack(Material.APPLE, 2), new ItemStack(Material.LEAVES, 16),
								new ItemStack(Material.SHEARS,
										1) }) }),
				new Kit("§e[§c§l+§e] §aKit§7: Panzer§7 (§eSkyWars§7)",
						new String[] { "§8x1§7 Diamanthelm mit Dornen 1,Unbreaking 1",
								"§8x1§7 Eisenbrustpanzer mit Unbreaking 1", "§8x1§7 Eisenhose mit Unbreaking 1",
								"§8x1§7 Eisenschuhe mit Unbreaking 1" },
						new ItemStack(Material.SLIME_BALL), PermissionType.SKYWARS_KIT_PANZER, KitType.KAUFEN, 2000,
						500,
						new Perk[] { new PerkEquipment(new ItemStack[] {
								UtilItem.EnchantItem(UtilItem.EnchantItem(new ItemStack(Material.IRON_CHESTPLATE),
										Enchantment.DURABILITY, 1), Enchantment.THORNS, 1),
								UtilItem.EnchantItem(new ItemStack(Material.DIAMOND_HELMET), Enchantment.DURABILITY, 1),
								UtilItem.EnchantItem(new ItemStack(Material.IRON_LEGGINGS), Enchantment.DURABILITY, 1),
								UtilItem.EnchantItem(new ItemStack(Material.IRON_BOOTS), Enchantment.DURABILITY,
										1) }) }),
				new Kit("§e[§c§l+§e] §aKit§7: Glueckshase§7 (§eSkyWars§7)",
						new String[] { "§8x1§7 Eisenspitzhacke mit Glueck 2" }, new ItemStack(Material.RABBIT_FOOT),
						PermissionType.SKYWARS_KIT_HASE, KitType.KAUFEN, 2000, 500,
						new Perk[] { new PerkEquipment(
								new ItemStack[] { UtilItem.EnchantItem(new ItemStack(Material.IRON_PICKAXE),
										Enchantment.LOOT_BONUS_BLOCKS, 2) }) }),
				new Kit("§e[§c§l+§e] §aKit§7: Hulk§7 (§eSkyWars§7)",
						new String[] { "§8x1§7 Leder Rüstung mit Schutz 1", "§8x1§7 Stärke 2 Trank" },
						UtilItem.LSetColor(new ItemStack(Material.LEATHER_HELMET), DyeColor.GREEN),
						PermissionType.SKYWARS_KIT_HULK, KitType.KAUFEN, 2000, 500,
						new Perk[] {
								new PerkEquipment(
										new ItemStack[] {
												UtilItem.EnchantItem(
														UtilItem.LSetColor(new ItemStack(Material.LEATHER_BOOTS),
																DyeColor.GREEN),
														Enchantment.PROTECTION_ENVIRONMENTAL, 1),
												UtilItem.EnchantItem(
														UtilItem.LSetColor(new ItemStack(Material.LEATHER_LEGGINGS),
																DyeColor.GREEN),
														Enchantment.PROTECTION_ENVIRONMENTAL, 1),
												UtilItem.EnchantItem(new ItemStack(Material.IRON_CHESTPLATE),
														Enchantment.PROTECTION_ENVIRONMENTAL, 1),
												UtilItem.EnchantItem(
														UtilItem.LSetColor(new ItemStack(Material.LEATHER_HELMET),
																DyeColor.GREEN),
														Enchantment.PROTECTION_ENVIRONMENTAL, 1),
										// new
										// ItemStack(Material.POTION,1,(byte)16393)
										}) }), new Kit("§e[§c§l+§e] §aKit§7: SuperMario§7 (§eSkyWars§7)", new String[] { "§8x1§7 Roter Lederbrustpanzer", "§8x1§7 Blaue Lederhose", "§8x1§7 Sprungkraft 1 Trank" }, UtilItem.LSetColor(new ItemStack(Material.LEATHER_HELMET), DyeColor.RED), PermissionType.SKYWARS_KIT_MARIO, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { UtilItem.LSetColor(new ItemStack(Material.LEATHER_LEGGINGS), DyeColor.RED), UtilItem.LSetColor(new ItemStack(Material.LEATHER_CHESTPLATE), DyeColor.BLUE), new ItemStack(Material.POTION, 1, (byte) 16459) }) }), new Kit("§e[§c§l+§e] §aKit§7: Stuntman§7 (§eSkyWars§7)", new String[] { "§8x1§7 Diamantschuhe mit Federfall IV" }, new ItemStack(Material.FEATHER), PermissionType.SKYWARS_KIT_STUNTMAN, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { UtilItem.EnchantItem(new ItemStack(Material.DIAMOND_BOOTS), Enchantment.PROTECTION_FALL, 5) }) }), new Kit("§e[§c§l+§e] §aKit§7: Slime§7 (§eSkyWars§7)", new String[] { "§8x16§7 Slime Bleocke", "§8x5§7 Jump Wurftrank" }, new ItemStack(Material.SLIME_BALL), PermissionType.SKYWARS_KIT_SLIME, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.SLIME_BLOCK, 16), new ItemStack(Material.POTION, 5, (byte) 16427) }) }), new Kit("§e[§c§l+§e] §aKit§7: Koch§7 (§eSkyWars§7)", new String[] { "§8x1§7 Holzschwert Schärfe 2", "§8x8§7 Steaks", "§8x1§7 Kuchen", "§8x3§7 Goldenäpfel" }, new ItemStack(Material.CAKE), PermissionType.SKYWARS_KIT_KOCH, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { UtilItem.RenameItem(UtilItem.EnchantItem(new ItemStack(Material.WOOD_SWORD), Enchantment.DAMAGE_ALL, 2), "§7Gabel"), new ItemStack(Material.COOKED_BEEF, 8), new ItemStack(Material.CAKE, 1), new ItemStack(Material.GOLDEN_APPLE, 3) }) }), new Kit("§e[§c§l+§e] §aKit§7: Sprengmeister§7 (§eSkyWars§7)", new String[] { "§8x4§7 Creeper Spawner", "§8x10§7 TnT", "§8x1§7 Feuerzeug" }, new ItemStack(Material.TNT), PermissionType.SKYWARS_KIT_SPRENGMEISTER, KitType.KAUFEN, 2000, 500, new Perk[] {}), new Kit("§e[§c§l+§e] §aKit§7: Jaeger§7 (§eSkyWars§7)", new String[] { "§8x1§7 Bogen", "§8x10 §7Pfeile" }, new ItemStack(Material.ARROW), PermissionType.SKYWARS_KIT_JAEGER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.BOW), new ItemStack(Material.ARROW, 10) }) }), new Kit("§e[§c§l+§e] §aKit§7: Enchanter§7 (§eSkyWars§7)", new String[] { "§8x1§7 Enchantment Table", "§8x64 §7Enchantment Bottles" }, new ItemStack(Material.ENCHANTMENT_TABLE), PermissionType.SKYWARS_KIT_ENCHANTER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.ENCHANTMENT_TABLE), new ItemStack(Material.LAPIS_ORE, 16), new ItemStack(Material.EXP_BOTTLE, 64) }) }), new Kit("§e[§c§l+§e] §aKit§7: Heiler§7 (§eSkyWars§7)", new String[] { "§8x3§7 Tränke der Heilung", "§8x3§7 Regenerations Tränke" }, new ItemStack(Material.POTION, 1, (short) 16389), PermissionType.SKYWARS_KIT_HEILER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.POTION, 3, (short) 16389), new ItemStack(Material.POTION, 3, (short) 16385) }) }), new Kit("§e[§c§l+§e] §aKit§7: Späher§7 (§eSkyWars§7)", new String[] { "§8x1§7 Steinschwert", "§8x3 §7Schnelligkeits Tränke" }, new ItemStack(Material.STONE_SWORD), PermissionType.SKYWARS_KIT_SPAEHER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.STONE_SWORD), new ItemStack(Material.POTION, 3, (short) 16386) }) }), new Kit("§e[§c§l+§e] §aKit§7: Kampfmeister§7 (§eSkyWars§7)", new String[] { "§8x1§7 Anvil", "§8x64§7 Enchantment Bottles", "§8x1§7 Diamant Helm", "§8x1§7 Enchantment Buch (Protection III)" }, new ItemStack(Material.ANVIL), PermissionType.SKYWARS_KIT_KAMPFMEISTER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { UtilItem.getEnchantmentBook(Enchantment.PROTECTION_ENVIRONMENTAL, 3), new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.EXP_BOTTLE, 64), new ItemStack(Material.ANVIL) }) }), new Kit("§e[§c§l+§e] §aKit§7: Ritter§7 (§eSkyWars§7)", new String[] { "§8x1§7 Eisenschwert (Schärfe II)" }, new ItemStack(Material.IRON_SWORD), PermissionType.SHEEPWARS_KIT_ARROWMAN, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { UtilItem.EnchantItem(new ItemStack(Material.IRON_SWORD), Enchantment.DAMAGE_ALL, 2) }) }), new Kit("§e[§c§l+§e] §aKit§7: Feuermeister§7 (§eSkyWars§7)", new String[] { "§8x1§7Feuerzeug", "§8x2§7 Lava-Eimer", "§8x2§7 Feuer Tr§nke" }, new ItemStack(Material.LAVA_BUCKET), PermissionType.SKYWARS_KIT_FEUERMEISTER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.POTION, 2, (short) 16387), new ItemStack(Material.FLINT_AND_STEEL, 1), new ItemStack(Material.LAVA_BUCKET, 2) }) }), new Kit("§e[§c§l+§e] §aKit§7: Droide§7 (§eSkyWars§7)", new String[] { "§8x1§7 Regenerations Trank", "§8x2§7 Tr§nke der Heilung", "§8x2§7 Vergiftungs Tränke", "§8x2§7 Schadens Tränke" }, new ItemStack(Material.POTION), PermissionType.SKYWARS_KIT_DROIDE, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.POTION, 1, (short) 16385), new ItemStack(Material.POTION, 2, (short) 16388), new ItemStack(Material.POTION, 2, (short) 16389), new ItemStack(Material.POTION, 2, (short) 16396) }) }), new Kit("§e[§c§l+§e] §aKit§7: Stoßer§7 (§eSkyWars§7)", new String[] { "§8x1§7 Holzschwert mit Rückstoß 1" }, new ItemStack(Material.WOOD_SWORD), PermissionType.SKYWARS_KIT_STOSSER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { UtilItem.EnchantItem(new ItemStack(Material.WOOD_SWORD), Enchantment.KNOCKBACK, 1) }) }), new Kit("§e[§c§l+§e] §aKit§7: Hase§7 (§eSkyWars§7)", new String[] { "§8x2§7 Schnelligkeits Treanke", "§8x2§7 Sprungkraft Treanke", "§8x8§7 Kartotten" }, new ItemStack(Material.CARROT), PermissionType.SKYWARS_KIT_HASE, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.POTION, 2, (byte) 0), new ItemStack(Material.POTION, 2, (byte) 0), new ItemStack(Material.CARROT, 8) }) }), new Kit("§e[§c§l+§e] §aKit§7: Rusher§7 (§eSkyWars§7)", new String[] { "§8x25§7 Granit Bloecke", "§8x1§7 Steinschwert Schaerfe 1", "§8x1§7 Goldbrustpanzer" }, new ItemStack(Material.GOLD_CHESTPLATE), PermissionType.SKYWARS_KIT_RUSHER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.COBBLESTONE, 25), new ItemStack(Material.GOLD_CHESTPLATE), UtilItem.EnchantItem(new ItemStack(Material.STONE_SWORD), Enchantment.DAMAGE_ALL, 1) }) }), new Kit("§e[§c§l+§e] §aKit§7: Polizist§7 (§eSkyWars§7)", new String[] { "§8x1§7 Lederbrustpanzer", "§8x1§7 Lederhose", "§8x1§7 Stock mit Schaerfe 2" }, UtilItem.LSetColor(new ItemStack(Material.LEATHER_CHESTPLATE), DyeColor.BLUE), PermissionType.SKYWARS_KIT_POLIZIST, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { UtilItem.LSetColor(new ItemStack(Material.LEATHER_CHESTPLATE), DyeColor.BLUE), UtilItem.LSetColor(new ItemStack(Material.LEATHER_LEGGINGS), DyeColor.BLUE), UtilItem.RenameItem(UtilItem.EnchantItem(new ItemStack(Material.STICK), Enchantment.DAMAGE_ALL, 2), "§7Knueppel") }) }), new Kit("§e[§c§l+§e] §aKit§7: Farmer§7 (§eSkyWars§7)", new String[] { "§8x1§7 Eisenspitzhacke mit Schaerfe 2 u. Efficiens 2", "§8x16§7 Eier" }, new ItemStack(Material.IRON_PICKAXE), PermissionType.SKYWARS_KIT_FARMER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { new ItemStack(Material.EGG, 16), UtilItem.EnchantItem(UtilItem.EnchantItem(new ItemStack(Material.IRON_PICKAXE), Enchantment.DIG_SPEED, 2), Enchantment.DAMAGE_ALL, 2) }) }), new Kit("§e[§c§l+§e] §aKit§7: Fischer§7 (§eSkyWars§7)", new String[] { "§8x1§7 Angel mit unbreaking 2", "§8x1§7 Kettenrüstung mit Schutz 2" }, new ItemStack(Material.CHAINMAIL_CHESTPLATE), PermissionType.SKYWARS_KIT_FISCHER, KitType.KAUFEN, 2000, 500, new Perk[] { new PerkEquipment(new ItemStack[] { UtilItem.EnchantItem(new ItemStack(Material.FISHING_ROD), Enchantment.DURABILITY, 2), UtilItem.EnchantItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE), Enchantment.PROTECTION_ENVIRONMENTAL, 2) }) }) };
	}
}
