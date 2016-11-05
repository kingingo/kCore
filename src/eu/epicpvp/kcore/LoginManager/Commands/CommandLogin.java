package eu.epicpvp.kcore.LoginManager.Commands;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import eu.epicpvp.kcore.AACHack.util.Rate;
import eu.epicpvp.kcore.Command.CommandHandler.Sender;
import eu.epicpvp.kcore.LoginManager.LoginManager;
import eu.epicpvp.kcore.Translation.TranslationHandler;
import eu.epicpvp.kcore.Util.Title;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLogin implements CommandExecutor {

	@Getter
	private LoginManager loginManager;
	private LoadingCache<String,Rate> loginAttemptsRates = CacheBuilder.newBuilder()
			.expireAfterWrite(3, TimeUnit.MINUTES)
			.build(new CacheLoader<String, Rate>() {
				@Override
				public Rate load(@NonNull String key) throws Exception {
					return new Rate(2, TimeUnit.SECONDS);
				}
			});

	public CommandLogin(LoginManager loginManager) {
		this.loginManager = loginManager;
	}

	@Override
	@eu.epicpvp.kcore.Command.CommandHandler.Command(command = "login", sender = Sender.PLAYER)
	public boolean onCommand(CommandSender cs, org.bukkit.command.Command cmd, String cmdLabel, String[] args) {
		if (!(cs instanceof Player)) return false;
		Player player = (Player) cs;

		if (!getLoginManager().getLogin().containsKey(player.getName().toLowerCase())) return false;
		if (args.length == 0) {
			player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§c/login [Password]");
			return true;
		} else {
			Rate loginAttemptsRate = loginAttemptsRates.getUnchecked(player.getName().toLowerCase());
			if (loginAttemptsRate.getOccurredEventsInMaxTime() > 2) {
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + "§cYou tried passwords too fast. Please wait a bit between attempts.");
				return true;
			}
			if (args[0].equalsIgnoreCase(getLoginManager().getLogin().get(player.getName().toLowerCase()))) {
				getLoginManager().getLogin().remove(player.getName().toLowerCase());
				Title title = new Title("  ", " ");
				title.resetTitle(player);
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "LOGIN_ACCEPT"));
				getLoginManager().addPlayerToBGList(player);
			} else {
				loginAttemptsRate.eventTriggered();
				player.sendMessage(TranslationHandler.getText(player, "PREFIX") + TranslationHandler.getText(player, "LOGIN_DENY"));
			}
			return true;
		}
	}
}
