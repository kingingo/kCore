package eu.epicpvp.kcore.Permission;

import java.util.UUID;

import dev.wolveringer.dataclient.protocoll.DataBuffer;

public interface PermissionChannelListener {
	public void handle(UUID fromPacket,DataBuffer buffer);
}
