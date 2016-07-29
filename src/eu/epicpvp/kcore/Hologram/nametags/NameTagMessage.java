package eu.epicpvp.kcore.Hologram.nametags;

import java.awt.image.BufferedImage;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

import eu.epicpvp.kcore.Util.UtilServer;
import lombok.Getter;
import lombok.Setter;

public class NameTagMessage extends ImageMessage {

	@Getter
	@Setter
	private Location location;
	@Getter
	@Setter
	private double lineSpacing = 0.25d;
	@Getter
	private NameTagType type;

	private NameTagPacketSpawner packetSpawner;
	private NameTagEntitySpawner entitySpawner;

	public NameTagMessage(NameTagType type, Location location, String... lines) {
		super(lines);
		this.location = location;
		this.type = type;
		initialize(this.lines.length);
	}

	public NameTagMessage(NameTagType type, BufferedImage image, int height, char imgChar) {
		super(image, height, imgChar);
		this.type = type;
		initialize(height);
	}

	public NameTagMessage(NameTagType type, ChatColor[][] chatColors, char imgChar) {
		super(chatColors, imgChar);
		this.location = Preconditions.checkNotNull(location, "location cannot be NULL");
		this.type = type;
		initialize(chatColors.length);
	}

	public NameTagMessage(NameTagType type, double y, String... imgLines) {
		super(imgLines);
		this.type = type;
		initialize(imgLines.length, y);
	}

	public void remove() {
		clear();
		this.location = null;
		this.lineSpacing = 0;
		this.lines = null;
		this.type = null;
		this.packetSpawner = null;
		this.entitySpawner = null;
	}

	private void initialize(int height, double y) {
		if (getType() == NameTagType.PACKET) {
			this.packetSpawner = new NameTagPacketSpawner(height, y);
		} else {
			this.entitySpawner = new NameTagEntitySpawner(height, y);
		}
	}

	private void initialize(int height) {
		if (getType() == NameTagType.PACKET) {
			this.packetSpawner = new NameTagPacketSpawner(height);
		} else {
			this.entitySpawner = new NameTagEntitySpawner(height);
		}
	}

	public void send() {
		if (getType() == NameTagType.PACKET) {
			for (Player player : UtilServer.getPlayers())
				sendToPlayer(player);
		} else {
			for (int i = 0; i < this.lines.length; i++) {
				this.entitySpawner.setNameTag(i, location, -i * this.lineSpacing, this.lines[i]);
			}
		}
	}

	public void sendToPlayer(Player player) {
		sendToPlayer(player, this.location != null ? this.location : player.getLocation());
	}

	public void sendToPlayer(Player player, Location location) {
		if (getType() == NameTagType.PACKET) {
			this.location = location;
			for (int i = 0; i < this.lines.length; i++) {
				this.packetSpawner.setNameTag(i, player, location, -i * this.lineSpacing, this.lines[i]);
			}
		}
	}

	public void move(Location location) {
		if (getType() == NameTagType.PACKET) {
			for (Player player : UtilServer.getPlayers())
				move(player, location);
		} else {
			Location copy = location.clone();

			for (int i = 0; i < this.lines.length; i++) {
				this.entitySpawner.moveNameTag(i, copy);
				copy.setY(copy.getY() - this.lineSpacing);
			}
		}
	}

	public void move(Player player, Location location) {
		if (getType() == NameTagType.PACKET) {
			setLocation(location);
			Location copy = location.clone();

			for (int i = 0; i < this.lines.length; i++) {
				this.packetSpawner.moveNameTag(i, player, copy);
				copy.setY(copy.getY() - this.lineSpacing);
			}
		}
	}

	public void clear() {
		if (getType() == NameTagType.PACKET) {
			for (Player player : UtilServer.getPlayers())
				clear(player);
		} else {
			if (this.entitySpawner == null) {
				new NullPointerException("entitySpawner == NULL");
				return;
			}
			this.entitySpawner.clearNameTags();
		}
	}

	public void clear(Player player) {
		if (getType() == NameTagType.PACKET) {
			this.packetSpawner.clearNameTags(player);
		}
	}
}
