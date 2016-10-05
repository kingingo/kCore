package eu.epicpvp.kcore.Listener.nick;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.packetwrapper.WrapperPlayServerChat;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import eu.epicpvp.datenclient.client.LoadedPlayer;
import eu.epicpvp.kcore.Util.UtilServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class NickListener extends PacketAdapter {

	private static final Pattern PATTERN = Pattern.compile("\\{player_(?=(([a-zA-Z0-9_]){3,30})\\})");

	public NickListener(JavaPlugin plugin) {
		super(new AdapterParameteters()
				.plugin(plugin)
				.serverSide()
				.optionAsync()
				.listenerPriority(ListenerPriority.MONITOR) //only in monitor we can properly also handle packets form aac
				.types(PacketType.Play.Server.CHAT, PacketType.Play.Server.PLAYER_INFO, PacketType.Play.Server.NAMED_ENTITY_SPAWN, PacketType.Play.Server.ENTITY_METADATA,
						PacketType.Play.Server.SCOREBOARD_TEAM, PacketType.Play.Server.TITLE)
		);
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		event.setReadOnly(false); //just to be sure due to monitor state
		PacketType packetType = event.getPacketType();
		if (packetType == PacketType.Play.Server.CHAT) {
			WrapperPlayServerChat packet = new WrapperPlayServerChat(event.getPacket());
			String jsonBase = packet.getMessage().getJson();
			if (!PATTERN.matcher(jsonBase).find()) {
				return;
			}
			BaseComponent[] components = ComponentSerializer.parse(jsonBase);
			List<BaseComponent> newComponents = new ArrayList<>(components.length);
			for (BaseComponent component : components) {
				newComponents.addAll(replaceNames(component, UtilServer.getPermissionManager().hasPermission(event.getPlayer(), "epicpvp.nick.chat.see")));
			}
			BaseComponent[] newComponentsArray = newComponents.toArray(new BaseComponent[newComponents.size()]);
			String newJson = ComponentSerializer.toString(newComponentsArray);
			packet.getMessage().setJson(newJson);
		} else if (packetType == PacketType.Play.Server.PLAYER_INFO) {

		} else if (packetType == PacketType.Play.Server.NAMED_ENTITY_SPAWN) {

		} else if (packetType == PacketType.Play.Server.ENTITY_METADATA) {

		} else if (packetType == PacketType.Play.Server.SCOREBOARD_TEAM) {

		} else if (packetType == PacketType.Play.Server.TITLE) {

		}
	}

	private static List<BaseComponent> replaceNames(BaseComponent component, boolean info) {
		ArrayList<BaseComponent> out = new ArrayList<>();
		if (component instanceof TextComponent) {
			TextComponent textComponent = (TextComponent) component;

			String text = textComponent.getText();
			Matcher matcher = PATTERN.matcher(text);
			while (matcher.find()) {
				TextComponent add = new TextComponent(textComponent);//Copy Style
				add.setText(text.substring(0, matcher.start()));
				out.add(add);

				LoadedPlayer player = UtilServer.getClient().getPlayerAndLoad(matcher.group(1));

				TextComponent nickname = new TextComponent(textComponent); //Copy Style
				if (player.isLoaded())
					nickname.setText(!player.hasNickname() ? matcher.group(1) : info ? player.getName() : player.getNickname());
				else {
					nickname.setText(matcher.group(1));
//					System.err.println("Cant replace nickname (Player not loaded in replaceNames(BaseComponent,boolean), Player: "+m.group(1)+")");
				}
				if (player.isLoaded() && player.hasNickname() && info) {
					nickname.setItalic(true);
					nickname.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aDer Spieler §e" + player.getName() + " §aist genickt als §b" + player.getNickname()).create()));
				}
				out.add(nickname);

				textComponent.setText(text.substring(matcher.start() + ("{player_" + matcher.group(1) + "}").length()));
			}
			if (!textComponent.getText().isEmpty())
				out.add(textComponent);
		}
		if (component.getExtra() != null)
			for (BaseComponent s : component.getExtra())
				out.addAll(replaceNames(s, info));
		return out;
	}

	@Override
	public void onPacketReceiving(PacketEvent event) {
		//has to be here due to a protocollib issue
	}
}
