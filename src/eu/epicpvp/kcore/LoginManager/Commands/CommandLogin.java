package eu.epicpvp.kcore.LoginManager.Commands;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dev.wolveringer.client.LoadedPlayer;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.LoginManager.LoginManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.Title;
import lombok.Getter;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLogin implements CommandExecutor {

	@Getter
	private LoginManager loginManager;

	public CommandLogin(LoginManager loginManager) {
		this.loginManager = loginManager;
	}

	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "login", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, org.bukkit.command.Command cmd, String cmdLabel, String[] args) {
		if (!(cs instanceof Player)) {
			return false;
		}
		Player player = (Player) cs;

		if (!getLoginManager().getLogin().containsKey(player.getName().toLowerCase())) {
			return false;
		}
		if (args.length == 0) {
			player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§c/login [Password]");
			return true;
		} else {
			String saved = getLoginManager().getLogin().get(player.getName().toLowerCase());
			String input = args[0];
			String inputHashed = hashPassword(input, player.getName().toLowerCase());
			if (inputHashed == null) {
				player.sendMessage("§cAn error happend while trying to log you in. Please report this with the current time on our teamspeak.");
				return true;
			}
			if (input.equalsIgnoreCase(saved)) {
				LoadedPlayer loadedplayer = getLoginManager().getClient().getPlayerAndLoad(player.getName());
				loadedplayer.setPasswordSync(inputHashed);
				login(player);
			} else if (inputHashed.equals(saved)) {
				login(player);
			} else {
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "LOGIN_DENY"));
			}
			return true;
		}
	}

	private void login(Player player) {
		getLoginManager().getLogin().remove(player.getName().toLowerCase());
		Title title = new Title("  ", " ");
		title.resetTitle(player);
		player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "LOGIN_ACCEPT"));
		getLoginManager().addPlayerToBGList(player);
	}

	/**
	 * Salts the password with the username, then hashes the utf8 bytes of the string
	 * @param password the password to hash. its lowercased automatically
	 * @param username the username to hash. its lowercased automatically
	 * @return the hashed password with a § sign infront to make it impossible that anyone can type a hashed password
	 */
	public static String hashPassword(String password, String username) {
		password = password.toLowerCase();
		username = username.toLowerCase();
		String toDigest = password + username;
		try {
			MessageDigest md = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256); //Using this field instead of a string directly because I hate string constants not in fields
			byte[] digest = md.digest(toDigest.getBytes(StandardCharsets.UTF_8));
			return "§" + String.format("%064x", new BigInteger(1, digest)); //stackoverflow says this puts the bytes into the known string representation of sha256
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}